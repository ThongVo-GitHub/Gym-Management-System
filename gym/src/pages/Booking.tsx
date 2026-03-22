import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Search, Clock, User, Calendar, AlertCircle, Dumbbell, Flame, Target, Zap, HeartPulse, TrendingUp, Users } from "lucide-react";
import { toast } from "sonner";
import { useCountUp } from "@/hooks/useCountUp";

const classIcons = [Dumbbell, Target, HeartPulse, Flame, Zap];

const classes = [
  { id: 1, name: "Full Body Power", trainer: "Quốc Dũng", schedule: "Thứ 2 - 4 - 6", time: "18:00 - 19:30", slots: 5 },
  { id: 2, name: "Upper Body Sculpt", trainer: "Tuấn Anh", schedule: "Thứ 3 - 5 - 7", time: "10:00 - 11:30", slots: 0 },
  { id: 3, name: "Lower Body Strength", trainer: "Minh Tú", schedule: "Thứ 3 - 5 - 7", time: "17:00 - 18:30", slots: 12 },
  { id: 4, name: "Cardio HIIT Blast", trainer: "Hoàng Nam", schedule: "Thứ 2 - 4 - 6", time: "19:30 - 20:30", slots: 0 },
  { id: 5, name: "Core & Abs Intense", trainer: "Bích Phương", schedule: "Thứ 2 - 4 - 6", time: "07:00 - 08:00", slots: 8 },
];

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

const Booking = () => {
  const [search, setSearch] = useState("");
  const [trainerFilter, setTrainerFilter] = useState("all");

  const trainers = [...new Set(classes.map((c) => c.trainer))];

  const filtered = classes.filter((c) => {
    const matchSearch = c.name.toLowerCase().includes(search.toLowerCase()) || c.trainer.toLowerCase().includes(search.toLowerCase());
    const matchTrainer = trainerFilter === "all" || c.trainer === trainerFilter;
    return matchSearch && matchTrainer;
  });

  const handleBook = (cls: (typeof classes)[0]) => {
    if (cls.slots === 0) {
      toast.error("Lớp đã đầy! Vui lòng chọn lớp khác.");
    } else {
      toast.success(`Đăng ký lớp thành công: ${cls.name}`);
    }
  };

  return (
    <div className="p-6 lg:p-8 max-w-6xl mx-auto space-y-6">
      {/* Header with stroke text */}
      <div className="animate-slide-in-left">
        <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>
          Chọn lớp phù hợp với lịch trình
        </p>
        <h1 className="text-2xl lg:text-3xl font-extrabold tracking-tight uppercase text-primary animate-fade-down" style={{ animationDelay: '200ms' }}>
          Danh sách lớp học nhóm
        </h1>
        <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">
          BOOKING
        </p>
      </div>

      {/* Stats bar */}
      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={5} label="Lớp học nhóm" icon={Dumbbell} />
          <StatCounter end={25} label="Slot trống" icon={TrendingUp} />
          <StatCounter end={5} label="Huấn luyện viên" icon={Users} />
        </div>
      </div>

      {/* Filters */}
      <div className="flex flex-wrap gap-3 animate-slide-in-left" style={{ animationDelay: "300ms" }}>
        <div className="relative flex-1 min-w-[200px] max-w-sm">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <Input
            placeholder="Tìm lớp hoặc HLV..."
            className="pl-9 rounded-xl bg-transparent border-border"
            style={{ background: "hsla(4, 15%, 10%, 0.5)", backdropFilter: "blur(12px)" }}
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <Select value={trainerFilter} onValueChange={setTrainerFilter}>
          <SelectTrigger
            className="w-44 rounded-xl border-border"
            style={{ background: "hsla(4, 15%, 10%, 0.5)", backdropFilter: "blur(12px)" }}
          >
            <SelectValue placeholder="Huấn luyện viên" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">Tất cả HLV</SelectItem>
            {trainers.map((t) => (
              <SelectItem key={t} value={t}>HLV {t}</SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      {/* Class cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {filtered.map((cls, i) => {
          const IconComp = classIcons[i % classIcons.length];
          return (
            <div
              key={cls.id}
              className="glass-card-hover p-5 flex flex-col relative animate-slide-in-left"
              style={{ animationDelay: `${350 + i * 100}ms` }}
            >
              <div
                className="absolute top-0 left-5 right-5 h-[3px] rounded-b-full"
                style={{
                  background: cls.slots > 0
                    ? "linear-gradient(90deg, hsl(4, 80%, 56%), hsl(24, 95%, 55%))"
                    : "hsla(0, 0%, 40%, 0.4)",
                }}
              />

              <div className="flex items-center gap-3 mt-1 mb-3">
                <div className="w-10 h-10 icon-glow-red shrink-0 animate-float" style={{ animationDelay: `${i * 150}ms` }}>
                  <IconComp className="w-5 h-5 text-primary" />
                </div>
                <h3 className="font-extrabold text-base uppercase tracking-tight">
                  {cls.name}
                </h3>
              </div>

              <div className="space-y-2 text-sm text-muted-foreground flex-1">
                <p className="flex items-center gap-2">
                  <Calendar className="w-3.5 h-3.5 text-primary/70" />
                  Lịch tập: {cls.schedule}
                </p>
                <p className="flex items-center gap-2">
                  <User className="w-3.5 h-3.5 text-primary/70" />
                  HLV: {cls.trainer}
                </p>
                <p className="flex items-center gap-2">
                  <Clock className="w-3.5 h-3.5 text-primary/70" />
                  Giờ: {cls.time}
                </p>
              </div>

              <div className="mt-3">
                {cls.slots > 0 ? (
                  <p className="text-sm font-bold" style={{ color: "hsl(4, 80%, 56%)" }}>
                    Trống: {cls.slots} slot
                  </p>
                ) : (
                  <p className="text-sm font-bold flex items-center gap-1" style={{ color: "hsl(0, 60%, 50%)" }}>
                    <AlertCircle className="w-3.5 h-3.5" /> HẾT SLOT
                  </p>
                )}
              </div>

              <Button
                size="sm"
                onClick={() => handleBook(cls)}
                disabled={cls.slots === 0}
                className={`w-full rounded-xl mt-4 font-bold uppercase tracking-wide text-xs ${cls.slots === 0 ? "opacity-50" : "btn-gradient"}`}
              >
                <span className="relative z-10">
                  {cls.slots === 0 ? "HẾT CHỖ" : "ĐĂNG KÝ LỚP"}
                </span>
              </Button>
            </div>
          );
        })}
      </div>

      <footer className="text-center text-xs text-muted-foreground pt-4 pb-2 animate-fade-in" style={{ animationDelay: "900ms" }}>
        © 2023 TwelveFit Gym. All rights reserved.
      </footer>
    </div>
  );
};

export default Booking;
