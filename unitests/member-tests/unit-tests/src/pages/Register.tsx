// Standalone Register component used purely for unit tests.

import { useState } from "react";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { api } from "../lib/api";

export default function Register() {
  const [username, setUsername] = useState("");
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<{ username?: string; fullName?: string; email?: string; password?: string }>({});
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const next: typeof errors = {};
    if (!username) next.username = "Vui lòng nhập username";
    if (!fullName) next.fullName = "Vui lòng nhập họ và tên";
    if (!email) next.email = "Vui lòng nhập email";
    else if (!/^\S+@\S+\.\S+$/.test(email)) next.email = "Email không hợp lệ";
    if (!password) next.password = "Vui lòng nhập mật khẩu";
    else if (password.length < 6) next.password = "Mật khẩu tối thiểu 6 ký tự";
    setErrors(next);
    if (Object.keys(next).length > 0) {
      toast.error("Vui lòng nhập đầy đủ thông tin");
      return;
    }
    setLoading(true);
    try {
      await api.post("/auth/register", { username, email, password, fullName });
      toast.success("Đăng ký thành công!");
      navigate("/login", { replace: true });
    } catch (err) {
      toast.error(err instanceof Error ? err.message : "Đăng ký thất bại");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} noValidate>
      <div>
        <input aria-label="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
        {errors.username && <p role="alert" className="text-destructive">{errors.username}</p>}
      </div>
      <div>
        <input aria-label="Họ và tên" value={fullName} onChange={(e) => setFullName(e.target.value)} />
        {errors.fullName && <p role="alert" className="text-destructive">{errors.fullName}</p>}
      </div>
      <div>
        <input type="email" aria-label="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        {errors.email && <p role="alert" className="text-destructive">{errors.email}</p>}
      </div>
      <div>
        <input type="password" aria-label="Mật khẩu" value={password} onChange={(e) => setPassword(e.target.value)} />
        {errors.password && <p role="alert" className="text-destructive">{errors.password}</p>}
      </div>
      <button type="submit" disabled={loading}>Đăng ký</button>
    </form>
  );
}
