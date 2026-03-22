import { Calendar, Clock, User, Dumbbell, Target, HeartPulse, Flame, CheckCircle, Timer, CalendarDays, TrendingUp } from "lucide-react";
import { useCountUp } from "@/hooks/useCountUp";

const schedule = [
  { date: "20/03/2026", time: "18:00 - 19:30", name: "Full Body Power", trainer: "HLV Quốc Dũng", status: "Sắp tới", icon: Dumbbell },
  { date: "21/03/2026", time: "07:00 - 08:00", name: "Core & Abs Intense", trainer: "HLV Bích Phương", status: "Sắp tới", icon: Target },
  { date: "22/03/2026", time: "10:00 - 11:30", name: "Upper Body Sculpt", trainer: "HLV Tuấn Anh", status: "Đã đặt", icon: HeartPulse },
  { date: "18/03/2026", time: "17:00 - 18:30", name: "Lower Body Strength", trainer: "HLV Minh Tú", status: "Hoàn thành", icon: Flame },
];

const statusStyles: Record<string, { bg: string; color: string; glow?: string; icon: typeof CheckCircle }> = {
  "Hoàn thành": {
    bg: "hsla(152, 60%, 48%, 0.12)",
    color: "hsl(152, 60%, 55%)",
    icon: CheckCircle,
  },
  "Sắp tới": {
    bg: "hsla(38, 92%, 50%, 0.12)",
    color: "hsl(38, 92%, 60%)",
    glow: "0 0 10px -3px hsla(38, 92%, 50%, 0.3)",
    icon: Timer,
  },
  "Đã đặt": {
    bg: "hsla(4, 80%, 56%, 0.12)",
    color: "hsl(4, 80%, 65%)",
    icon: CalendarDays,
  },
};

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

const Schedule = () => {
  return (
    <div className="p-6 lg:p-8 max-w-4xl mx-auto space-y-6">
      {/* Header with stroke text */}
      <div className="animate-slide-in-left">
        <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>
          Theo dõi lịch tập của bạn
        </p>
        <h1 className="text-2xl lg:text-3xl font-extrabold tracking-tight animate-fade-down" style={{ animationDelay: '200ms' }}>
          <Calendar className="w-7 h-7 inline-block mr-2 text-primary" />
          Lịch của tôi
        </h1>
        <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">
          SCHEDULE
        </p>
      </div>

      {/* Stats bar */}
      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={4} label="Buổi tập tuần này" icon={Dumbbell} />
          <StatCounter end={18} label="Buổi tập tháng" icon={TrendingUp} />
          <StatCounter end={1} label="Hoàn thành" icon={CheckCircle} />
        </div>
      </div>

      {/* Schedule list */}
      <div className="space-y-3">
        {schedule.map((s, i) => {
          const style = statusStyles[s.status] || statusStyles["Đã đặt"];
          const ClassIcon = s.icon;
          const StatusIcon = style.icon;
          return (
            <div
              key={i}
              className="glass-card-hover p-5 flex flex-col sm:flex-row sm:items-center gap-3 animate-slide-in-left"
              style={{ animationDelay: `${350 + i * 100}ms` }}
            >
              <div className="w-11 h-11 icon-glow-red shrink-0 animate-float" style={{ animationDelay: `${i * 200}ms` }}>
                <ClassIcon className="w-5 h-5 text-primary" />
              </div>
              <div className="flex-1 space-y-1">
                <h3 className="font-bold text-sm">{s.name}</h3>
                <div className="flex flex-wrap gap-3 text-xs text-muted-foreground">
                  <span className="flex items-center gap-1.5">
                    <Calendar className="w-3 h-3" /> {s.date}
                  </span>
                  <span className="flex items-center gap-1.5">
                    <Clock className="w-3 h-3" /> {s.time}
                  </span>
                  <span className="flex items-center gap-1.5">
                    <User className="w-3 h-3" /> {s.trainer}
                  </span>
                </div>
              </div>
              <span
                className="text-xs font-semibold px-3 py-1 rounded-full flex items-center gap-1"
                style={{
                  background: style.bg,
                  color: style.color,
                  boxShadow: style.glow,
                }}
              >
                <StatusIcon className="w-3 h-3" />
                {s.status}
              </span>
            </div>
          );
        })}
      </div>

      <footer className="text-center text-xs text-muted-foreground pt-4 pb-2 animate-fade-in" style={{ animationDelay: "800ms" }}>
        © 2023 TwelveFit Gym. All rights reserved.
      </footer>
    </div>
  );
};

export default Schedule;
