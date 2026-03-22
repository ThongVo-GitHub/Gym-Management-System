import { Badge } from "@/components/ui/badge";
import {
  CalendarPlus,
  CreditCard,
  QrCode,
  MapPin,
  Dumbbell,
  Trophy,
  Quote,
  ChevronRight,
  Heart,
  Flame,
  TrendingUp,
} from "lucide-react";
import { useCountUp } from "@/hooks/useCountUp";

const memberInfo = {
  name: "Nguyễn Minh Tuấn",
  id: "TF-9876",
  rank: "Hội viên Gold",
  expiry: "31/12/2024",
  groupSessions: 12,
  ptSessions: 5,
  status: "ĐANG HOẠT ĐỘNG",
};

const recentActivities = [
  {
    icon: Dumbbell,
    iconClass: "icon-glow-red",
    title: "Full Body Power – HLV Quốc Dũng",
    subtitle: "Hôm nay, 18:00",
  },
  {
    icon: MapPin,
    iconClass: "icon-glow-amber",
    title: "Điểm danh tại Chi nhánh Quận 1",
    subtitle: "Hôm qua, 08:25",
  },
];

const quickActions = [
  {
    icon: CalendarPlus,
    title: "Đặt lịch tập ngay",
    desc: "Chọn lớp, HLV phù hợp",
    gradient: true,
  },
  {
    icon: CreditCard,
    title: "Đổi dịch vụ",
    desc: "Gia hạn hoặc mua thêm",
    gradient: false,
  },
  {
    icon: QrCode,
    title: "Mã Check-in (QR)",
    desc: "Dùng để vào phòng tập",
    gradient: false,
  },
];

const StatCounter = ({ end, label, icon: Icon }: { end: number; label: string; icon: React.ElementType }) => {
  const count = useCountUp(end, 2200);
  return (
    <div className="text-center group">
      <div className="w-12 h-12 mx-auto mb-3 icon-glow-red rounded-2xl flex items-center justify-center transition-transform duration-500 group-hover:scale-110 group-hover:rotate-6" style={{ transitionTimingFunction: 'cubic-bezier(0.16, 1, 0.3, 1)' }}>
        <Icon className="w-5 h-5 text-primary" />
      </div>
      <p className="text-3xl font-extrabold tracking-tight">
        <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">+{count}</span>
      </p>
      <p className="text-[10px] uppercase tracking-[0.2em] text-muted-foreground font-semibold mt-1">{label}</p>
    </div>
  );
};

const Dashboard = () => {
  return (
    <div className="p-6 lg:p-8 max-w-7xl mx-auto space-y-6">
      {/* Header with stroke text effect */}
      <div className="flex items-center justify-between animate-slide-in-left">
        <div>
          <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>
            Chào mừng quay trở lại,
          </p>
          <h1 className="text-2xl lg:text-3xl font-extrabold mt-1 tracking-tight animate-fade-down" style={{ animationDelay: '200ms' }}>
            {memberInfo.name} 👋
          </h1>
          <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">
            TWELVEFIT
          </p>
        </div>
        <div className="glass-card-hover px-5 py-3 flex items-center gap-3 animate-fade-down" style={{ animationDelay: '300ms' }}>
          <div className="w-11 h-11 icon-glow-red animate-float">
            <Trophy className="w-5 h-5 text-primary" />
          </div>
          <div className="text-right">
            <p className="text-sm font-bold">{memberInfo.rank}</p>
            <p className="text-xs text-muted-foreground font-mono">
              ID: {memberInfo.id}
            </p>
          </div>
        </div>
      </div>

      {/* Animated stats bar - FitClub style counters */}
      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={140} label="Huấn luyện viên" icon={Dumbbell} />
          <StatCounter end={978} label="Hội viên đang tập" icon={Heart} />
          <StatCounter end={42} label="Chương trình tập" icon={Flame} />
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left column */}
        <div className="lg:col-span-2 space-y-6">
          {/* Member info card */}
          <div className="glass-card p-6 animate-slide-in-left" style={{ animationDelay: "350ms" }}>
            <div className="flex items-center justify-between mb-5">
              <h2 className="text-lg font-bold tracking-tight">
                Thông tin thẻ hội viên
              </h2>
              <span className="status-active animate-pulse-glow">{memberInfo.status}</span>
            </div>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {[
                { label: "HẠNG THẺ", value: memberInfo.rank, emoji: "🏅" },
                { label: "NGÀY HẾT HẠN", value: memberInfo.expiry, emoji: "📅" },
                { label: "BUỔI TẬP NHÓM CÒN", value: `${memberInfo.groupSessions} buổi`, emoji: "💪" },
                { label: "BUỔI PT CÒN", value: `${memberInfo.ptSessions} buổi`, emoji: "🏋️" },
              ].map((item, i) => (
                <div
                  key={i}
                  className="rounded-xl p-4 text-center space-y-2 card-lift cursor-default"
                  style={{
                    background: "hsla(4, 20%, 12%, 0.5)",
                    border: "1px solid hsla(4, 40%, 22%, 0.25)",
                    animationDelay: `${400 + i * 80}ms`,
                  }}
                >
                  <span className="text-2xl block transition-transform duration-500 hover:scale-125">{item.emoji}</span>
                  <p className="text-[10px] uppercase tracking-widest text-muted-foreground font-semibold">
                    {item.label}
                  </p>
                  <p className="text-sm font-bold">{item.value}</p>
                </div>
              ))}
            </div>
          </div>

          {/* QR Code */}
          <div className="glass-card p-6 text-center animate-slide-in-left" style={{ animationDelay: "450ms" }}>
            <h2 className="text-lg font-bold mb-2 tracking-tight">
              Mã QR Điểm Danh
            </h2>
            <p className="text-sm text-muted-foreground mb-5">
              Đưa mã này trước scanner để check-in tại phòng tập
            </p>
            <div className="w-44 h-44 mx-auto rounded-2xl flex items-center justify-center mb-4 animate-glow-pulse qr-float" style={{
              background: "linear-gradient(135deg, hsla(0,0%,95%,0.95), hsla(0,0%,90%,0.9))",
            }}>
              <div className="text-center">
                <QrCode className="w-24 h-24 mx-auto text-[hsl(220,20%,10%)]" />
                <p className="text-xs font-mono font-bold text-[hsl(220,20%,15%)] mt-1">
                  TwelveFit • {memberInfo.id}
                </p>
              </div>
            </div>
            <p className="text-sm font-semibold text-muted-foreground font-mono">
              ID: {memberInfo.id}
            </p>
          </div>

          {/* Recent activities */}
          <div className="glass-card p-6 animate-slide-in-left" style={{ animationDelay: "550ms" }}>
            <h2 className="text-lg font-bold mb-4 tracking-tight">
              Hoạt động gần đây
            </h2>
            <div className="space-y-3">
              {recentActivities.map((a, i) => (
                <div
                  key={i}
                  className="flex items-center gap-3 p-3 rounded-xl activity-row cursor-default"
                  style={{
                    background: "hsla(4, 18%, 12%, 0.45)",
                    animationDelay: `${600 + i * 100}ms`,
                  }}
                >
                  <div className={`w-10 h-10 ${a.iconClass}`}>
                    <a.icon className="w-4 h-4 text-current" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm font-semibold">{a.title}</p>
                    <p className="text-xs text-muted-foreground">{a.subtitle}</p>
                  </div>
                  <Badge
                    variant="outline"
                    className="text-xs border-none"
                    style={{
                      background: "hsla(152, 60%, 48%, 0.12)",
                      color: "hsl(152, 60%, 55%)",
                    }}
                  >
                    Thành công
                  </Badge>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Right column */}
        <div className="space-y-6">
          {/* Floating heart rate widget - FitClub style */}
          <div className="glass-card-hover p-4 animate-slide-in-right relative overflow-hidden" style={{ animationDelay: '300ms' }}>
            <div className="absolute -top-6 -right-6 w-20 h-20 rounded-full" style={{ background: 'radial-gradient(circle, hsla(4, 80%, 56%, 0.2), transparent)' }} />
            <div className="flex items-center gap-3">
              <Heart className="w-8 h-8 text-primary animate-heartbeat" />
              <div>
                <p className="text-xs text-muted-foreground">Nhịp tim trung bình</p>
                <p className="text-2xl font-extrabold">116 <span className="text-xs font-normal text-muted-foreground">bpm</span></p>
              </div>
            </div>
          </div>

          {/* Calories widget - FitClub style */}
          <div className="glass-card-hover p-4 animate-slide-in-right relative overflow-hidden" style={{ animationDelay: '400ms' }}>
            <div className="absolute -bottom-4 -left-4 w-16 h-16 rounded-full" style={{ background: 'radial-gradient(circle, hsla(24, 95%, 55%, 0.2), transparent)' }} />
            <div className="flex items-center gap-3">
              <Flame className="w-8 h-8 text-accent animate-flicker" />
              <div>
                <p className="text-xs text-muted-foreground">Calories đốt hôm nay</p>
                <p className="text-2xl font-extrabold">
                  <span className="bg-gradient-to-r from-accent to-primary bg-clip-text text-transparent">480</span>
                  <span className="text-xs font-normal text-muted-foreground ml-1">kcal</span>
                </p>
              </div>
            </div>
          </div>

          {/* Progress widget */}
          <div className="glass-card-hover p-4 animate-slide-in-right" style={{ animationDelay: '450ms' }}>
            <div className="flex items-center gap-3">
              <TrendingUp className="w-8 h-8 text-[hsl(var(--success))]" />
              <div className="flex-1">
                <p className="text-xs text-muted-foreground">Mục tiêu tuần</p>
                <p className="text-sm font-bold">4/5 buổi tập</p>
                <div className="w-full h-2 rounded-full mt-2 overflow-hidden" style={{ background: 'hsla(220, 15%, 18%, 0.8)' }}>
                  <div className="h-full rounded-full animate-progress-fill" style={{
                    width: '80%',
                    background: 'linear-gradient(90deg, hsl(var(--success)), hsl(152, 60%, 60%))',
                  }} />
                </div>
              </div>
            </div>
          </div>

          {/* Quick actions */}
          <div className="glass-card p-5 animate-slide-in-right" style={{ animationDelay: "500ms" }}>
            <h2 className="text-lg font-bold mb-4 tracking-tight">
              Thao tác nhanh
            </h2>
            <div className="space-y-3">
              {quickActions.map((action, i) => (
                <div key={i} className="quick-action-card" style={{ animationDelay: `${550 + i * 80}ms` }}>
                  <div
                    className={`w-11 h-11 rounded-xl flex items-center justify-center shrink-0 ${action.gradient ? "btn-gradient" : ""}`}
                    style={!action.gradient ? { background: "hsla(220, 15%, 18%, 0.8)", border: "1px solid hsla(220, 15%, 25%, 0.3)" } : undefined}
                  >
                    <action.icon className="w-5 h-5 text-white relative z-10" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-bold">{action.title}</p>
                    <p className="text-xs text-muted-foreground">{action.desc}</p>
                  </div>
                  <ChevronRight className="w-4 h-4 text-muted-foreground transition-transform duration-300 group-hover:translate-x-1" />
                </div>
              ))}
            </div>
          </div>

          {/* Quote */}
          <div className="glass-card p-5 animate-slide-in-right relative overflow-hidden" style={{ animationDelay: "600ms" }}>
            <div className="absolute -top-8 -right-8 w-24 h-24 rounded-full opacity-20" style={{ background: "radial-gradient(circle, hsl(4, 80%, 56%), transparent)" }} />
            <Quote className="w-8 h-8 text-primary mb-3 animate-float" />
            <p className="text-sm italic text-muted-foreground leading-relaxed">
              "Từng giọt mồ hôi là từng giọt sức khoẻ, từng bước đi là từng
              bước xây dựng cơ thể mơ ước."
            </p>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="text-center text-xs text-muted-foreground pt-4 pb-2 animate-fade-in" style={{ animationDelay: "700ms" }}>
        © 2023 TwelveFit Gym. All rights reserved. Hotline: 1900 2434
      </footer>
    </div>
  );
};

export default Dashboard;
