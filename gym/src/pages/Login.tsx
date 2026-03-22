import { useState } from "react";
import { Dumbbell, Eye, EyeOff } from "lucide-react";
import gymBg from "@/assets/gym-bg.jpg";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    // Navigate to dashboard (no real auth)
    window.location.href = "/dashboard";
  };

  return (
    <div className="min-h-screen relative flex flex-col">
      {/* Background */}
      <div
        className="fixed inset-0 z-0 bg-cover bg-center"
        style={{
          backgroundImage: `url(${gymBg})`,
          filter: "blur(6px) brightness(0.35) saturate(1.2)",
          transform: "scale(1.05)",
        }}
      />
      <div className="fixed inset-0 z-[1]" style={{
        background: "linear-gradient(135deg, hsla(220, 30%, 4%, 0.7) 0%, hsla(4, 20%, 6%, 0.5) 50%, hsla(220, 25%, 4%, 0.7) 100%)",
      }} />

      {/* Animated blobs */}
      <div className="fixed inset-0 z-[2] pointer-events-none overflow-hidden">
        <div className="absolute w-[500px] h-[500px] rounded-full animate-blob-1" style={{
          background: "radial-gradient(circle, hsla(4, 80%, 45%, 0.12) 0%, transparent 70%)",
          top: "20%", left: "0%",
        }} />
        <div className="absolute w-[400px] h-[400px] rounded-full animate-blob-2" style={{
          background: "radial-gradient(circle, hsla(24, 95%, 50%, 0.08) 0%, transparent 70%)",
          bottom: "10%", right: "10%",
        }} />
      </div>

      {/* Content */}
      <div className="flex-1 relative z-10 flex items-center justify-center px-4 py-8">
        <div className="w-full max-w-5xl grid grid-cols-1 lg:grid-cols-2 gap-8 lg:gap-16 items-center">
          {/* Left - Branding */}
          <div className="animate-slide-in-left space-y-8">
            <div className="flex items-center gap-3">
              <div className="w-14 h-14 rounded-2xl btn-gradient flex items-center justify-center">
                <Dumbbell className="w-7 h-7 text-white relative z-10" />
              </div>
              <span className="text-2xl font-extrabold tracking-tight">
                TWELVE<span className="text-primary">FIT</span>
              </span>
            </div>

            <div>
              <h1 className="text-4xl lg:text-5xl font-extrabold leading-tight tracking-tight" style={{ lineHeight: 1.15 }}>
                Thay đổi cơ thể,
                <br />
                Thay đổi cuộc đời.
                <br />
                <span className="text-foreground">Thử ngay, </span>
                <span className="text-primary">TwelveFit.</span>
              </h1>
            </div>

            <div className="flex items-center gap-3 glass-card px-4 py-3 w-fit animate-fade-up" style={{ animationDelay: "400ms" }}>
              <div className="flex -space-x-2">
                <div className="w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold" style={{ background: "hsla(4, 80%, 56%, 0.8)" }}>MT</div>
                <div className="w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold" style={{ background: "hsla(24, 95%, 55%, 0.8)" }}>HL</div>
                <div className="w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold btn-gradient relative z-10">
                  <span className="relative z-10 text-[10px]">9k+</span>
                </div>
              </div>
              <p className="text-sm text-muted-foreground">Đã thay đổi thể hình thành công.</p>
            </div>
          </div>

          {/* Right - Login form */}
          <div className="animate-slide-in-right">
            <div className="glass-card p-8 lg:p-10 space-y-6" style={{
              background: "hsla(220, 18%, 10%, 0.65)",
              backdropFilter: "blur(32px) saturate(1.4)",
              border: "1px solid hsla(220, 15%, 25%, 0.25)",
            }}>
              <h2 className="text-xl font-bold text-center tracking-tight">
                Đăng nhập thành viên
              </h2>

              <form onSubmit={handleLogin} className="space-y-4">
                <div>
                  <input
                    type="text"
                    placeholder="Email hoặc ID"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full px-4 py-3.5 rounded-xl text-sm font-medium outline-none transition-all duration-300 focus:ring-2 focus:ring-primary/40"
                    style={{
                      background: "hsla(220, 15%, 14%, 0.7)",
                      border: "1px solid hsla(4, 40%, 25%, 0.25)",
                      color: "hsl(0, 0%, 96%)",
                    }}
                  />
                </div>
                <div className="relative">
                  <input
                    type={showPassword ? "text" : "password"}
                    placeholder="Mật khẩu"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full px-4 py-3.5 rounded-xl text-sm font-medium outline-none transition-all duration-300 focus:ring-2 focus:ring-primary/40 pr-12"
                    style={{
                      background: "hsla(220, 15%, 14%, 0.7)",
                      border: "1px solid hsla(4, 40%, 25%, 0.25)",
                      color: "hsl(0, 0%, 96%)",
                    }}
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                  >
                    {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                </div>

                <div className="text-right">
                  <button type="button" className="text-xs text-muted-foreground hover:text-primary transition-colors">
                    Quên mật khẩu?
                  </button>
                </div>

                <button
                  type="submit"
                  className="w-full py-3.5 rounded-xl text-sm font-bold uppercase tracking-wider btn-gradient"
                >
                  <span className="relative z-10">Đăng nhập</span>
                </button>
              </form>

              <div className="flex items-center gap-3">
                <div className="flex-1 h-px" style={{ background: "hsla(220, 15%, 30%, 0.4)" }} />
                <span className="text-xs text-muted-foreground uppercase tracking-wider">hoặc</span>
                <div className="flex-1 h-px" style={{ background: "hsla(220, 15%, 30%, 0.4)" }} />
              </div>

              <button
                type="button"
                className="w-full py-3.5 rounded-xl text-sm font-bold uppercase tracking-wider transition-all duration-300 active:scale-[0.97]"
                style={{
                  background: "hsla(220, 15%, 16%, 0.6)",
                  border: "1px solid hsla(220, 15%, 28%, 0.35)",
                  color: "hsl(0, 0%, 90%)",
                }}
              >
                Tạo tài khoản mới
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="relative z-10 text-center py-6 space-y-1 animate-fade-up" style={{ animationDelay: "600ms" }}>
        <p className="text-xs italic text-primary/70">
          "Buổi tập khó nhất chính là buổi tập bạn không bắt đầu."
        </p>
        <p className="text-xs font-bold text-foreground tracking-wider">
          HOTLINE: 1900 3434
        </p>
      </footer>
    </div>
  );
};

export default Login;
