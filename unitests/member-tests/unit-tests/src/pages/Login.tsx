// Standalone Login component (mirrors the real Login page) used purely for
// unit tests. Replace this file with the real one if you want to test
// production code directly.

import { useState } from "react";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { api } from "../lib/api";

interface AuthResponse {
  accessToken: string;
}

export default function Login() {
  const [usernameOrEmail, setUsernameOrEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<{ usernameOrEmail?: string; password?: string }>({});
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const next: typeof errors = {};
    if (!usernameOrEmail) next.usernameOrEmail = "Vui lòng nhập email hoặc username";
    if (!password) next.password = "Vui lòng nhập mật khẩu";
    setErrors(next);
    if (Object.keys(next).length > 0) {
      toast.error("Vui lòng nhập đầy đủ thông tin");
      return;
    }
    setLoading(true);
    try {
      const data = await api.post<AuthResponse>("/auth/login", { usernameOrEmail, password });
      if (data?.accessToken) {
        localStorage.setItem("token", data.accessToken);
        toast.success("Đăng nhập thành công!");
        navigate("/dashboard", { replace: true });
      }
    } catch (err) {
      toast.error(err instanceof Error ? err.message : "Đăng nhập thất bại");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} noValidate>
      <div>
        <input
          type="text"
          aria-label="Email hoặc Username"
          value={usernameOrEmail}
          onChange={(e) => setUsernameOrEmail(e.target.value)}
        />
        {errors.usernameOrEmail && (
          <p role="alert" className="text-destructive">{errors.usernameOrEmail}</p>
        )}
      </div>
      <div>
        <input
          type="password"
          aria-label="Mật khẩu"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {errors.password && (
          <p role="alert" className="text-destructive">{errors.password}</p>
        )}
      </div>
      <button type="submit" disabled={loading}>Đăng nhập</button>
    </form>
  );
}
