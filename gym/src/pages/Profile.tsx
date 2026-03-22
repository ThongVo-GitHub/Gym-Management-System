import { User, Mail, Phone, MapPin, CreditCard, Edit3, Shield, IdCard, Trophy, Dumbbell, TrendingUp } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCountUp } from "@/hooks/useCountUp";

const StatCounter = ({ end, label, icon: Icon }: { end: number; label: string; icon: React.ElementType }) => {
  const count = useCountUp(end, 2000);
  return (
    <div className="text-center group">
      <div className="w-12 h-12 mx-auto mb-3 icon-glow-red rounded-2xl flex items-center justify-center transition-transform duration-500 group-hover:scale-110 group-hover:rotate-6">
        <Icon className="w-5 h-5 text-primary" />
      </div>
      <p className="text-3xl font-extrabold tracking-tight">
        <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">{count}</span>
      </p>
      <p className="text-[10px] uppercase tracking-[0.2em] text-muted-foreground font-semibold mt-1">{label}</p>
    </div>
  );
};

const Profile = () => {
  return (
    <div className="p-6 lg:p-8 max-w-3xl mx-auto space-y-6">
      {/* Header with stroke text */}
      <div className="animate-slide-in-left">
        <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>
          Quản lý thông tin cá nhân
        </p>
        <h1 className="text-2xl lg:text-3xl font-extrabold tracking-tight animate-fade-down" style={{ animationDelay: '200ms' }}>
          <IdCard className="w-7 h-7 inline-block mr-2 text-primary" />
          Trang cá nhân
        </h1>
        <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">
          PROFILE
        </p>
      </div>

      {/* Stats bar */}
      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={48} label="Buổi tập" icon={Dumbbell} />
          <StatCounter end={12} label="Tháng thành viên" icon={Trophy} />
          <StatCounter end={96} label="% Chuyên cần" icon={TrendingUp} />
        </div>
      </div>

      {/* Profile card */}
      <div className="glass-card p-6 animate-slide-in-left" style={{ animationDelay: "350ms" }}>
        <div className="flex items-center gap-4 mb-6">
          <div
            className="w-16 h-16 rounded-2xl flex items-center justify-center text-2xl font-extrabold text-white btn-gradient animate-glow-pulse"
          >
            <span className="relative z-10">NT</span>
          </div>
          <div>
            <h2 className="text-xl font-extrabold tracking-tight">Nguyễn Minh Tuấn</h2>
            <p className="text-sm text-muted-foreground flex items-center gap-1">
              <Shield className="w-3.5 h-3.5 text-primary/70" />
              Hội viên Gold • <span className="font-mono">TF-9876</span>
            </p>
          </div>
        </div>

        <div className="space-y-3">
          {[
            { icon: Mail, label: "Email", value: "tuan.nguyen@email.com" },
            { icon: Phone, label: "Số điện thoại", value: "0912 345 678" },
            { icon: MapPin, label: "Địa chỉ", value: "123 Nguyễn Huệ, Q.1, TP.HCM" },
            { icon: CreditCard, label: "Gói hiện tại", value: "Gold - Hết hạn 31/12/2024" },
          ].map((item, i) => (
            <div
              key={i}
              className="flex items-center gap-3 p-3.5 rounded-xl activity-row cursor-default"
              style={{
                background: "hsla(4, 18%, 12%, 0.4)",
                border: "1px solid hsla(4, 40%, 22%, 0.2)",
                animationDelay: `${400 + i * 80}ms`,
              }}
            >
              <div className="w-9 h-9 icon-glow-red shrink-0">
                <item.icon className="w-4 h-4 text-primary" />
              </div>
              <div>
                <p className="text-xs text-muted-foreground">{item.label}</p>
                <p className="text-sm font-semibold">{item.value}</p>
              </div>
            </div>
          ))}
        </div>

        <Button
          variant="outline"
          className="mt-6 w-full rounded-xl border-border hover:border-primary/30 hover:bg-primary/5"
        >
          <Edit3 className="w-4 h-4 mr-2" />
          Chỉnh sửa thông tin
        </Button>
      </div>

      <footer className="text-center text-xs text-muted-foreground pt-4 pb-2 animate-fade-in" style={{ animationDelay: "600ms" }}>
        © 2023 TwelveFit Gym. All rights reserved.
      </footer>
    </div>
  );
};

export default Profile;
