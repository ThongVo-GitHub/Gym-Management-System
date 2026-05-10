import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import { api } from "@/api/api";

const AuthContext = createContext({
  user: null,
  session: null,
  profile: null,
  loading: true,
  refreshProfile: async () => {},
  logout: () => {},
});

function decodeJwt(token) {
  try {
    const payload = token.split(".")[1];
    return JSON.parse(atob(payload.replace(/-/g, "+").replace(/_/g, "/")));
  } catch {
    return null;
  }
}

function normalizeRole(role) {
  if (!role) return null;
  // Handle arrays - take the first element
  if (Array.isArray(role)) {
    role = role[0];
  }
  // Convert to string and normalize
  let normalized = String(role).toUpperCase().trim();
  // Remove ROLE_ prefix if present
  normalized = normalized.replace(/^ROLE_/, '');
  return normalized || null;
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [session, setSession] = useState(null);
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchProfile = useCallback(async () => {
    try {
      // BE: GET /api/auth/me -> UserResponse
      const data = await api.get("/users/me");
      setProfile(data);
      setUser((prev) =>
        prev
          ? {
              ...prev,
              email: data.email || prev.email,
              fullName: data.fullName || prev.fullName,
              username: data.username,
            }
          : prev,
      );
    } catch (e) {
      console.warn("fetchProfile failed:", e);
    }
  }, []);

  const checkAuth = useCallback(async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      setSession(null);
      setUser(null);
      setProfile(null);
      setLoading(false);
      return;
    }

    const payload = decodeJwt(token);
    if (payload?.exp && payload.exp * 1000 < Date.now()) {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      setSession(null);
      setUser(null);
      setProfile(null);
      setLoading(false);
      return;
    }

    // Try to get role from localStorage first (set by Login), then from JWT payload
    let role = localStorage.getItem("role") || normalizeRole(payload?.role ?? payload?.roles);

    setSession({ access_token: token });
    setUser({
      id: String(payload?.sub ?? payload?.userId ?? payload?.id ?? "unknown"),
      email: payload?.email,
      username: payload?.username,
      fullName: payload?.fullName ?? payload?.name,
      role: role,
    });

    await fetchProfile();
    setLoading(false);
  }, [fetchProfile]);

  const logout = () => {
    localStorage.removeItem("token");
    window.dispatchEvent(new Event("auth-changed"));
  };

  useEffect(() => {
    checkAuth();
    const handler = () => checkAuth();
    window.addEventListener("storage", handler);
    window.addEventListener("auth-changed", handler);
    return () => {
      window.removeEventListener("storage", handler);
      window.removeEventListener("auth-changed", handler);
    };
  }, [checkAuth]);

  return (
    <AuthContext.Provider
      value={{
        user,
        session,
        profile,
        loading,
        refreshProfile: fetchProfile,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
