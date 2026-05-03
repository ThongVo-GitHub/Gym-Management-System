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
import { Search, Clock, User, Calendar, AlertCircle, Dumbbell, Flame, Target, Zap, HeartPulse, TrendingUp, Users, Loader2 } from "lucide-react";
import { toast } from "sonner";
import { useCountUp } from "@/hooks/useCountUp";
import { api } from "@/lib/api";
import { useAuth } from "@/hooks/useAuth";
import { useQuery, useQueryClient } from "@tanstack/react-query";

const iconMap: Record<string, typeof Dumbbell> = { Dumbbell, Target, HeartPulse, Flame, Zap };

const StatCounter = ({ end, label, icon: Icon, color }: { end: number; label: string; icon: React.ElementType; color?: string }) => {
  const count = useCountUp(end, 2000);
  return (
    <div className="text-center group">
      <div
        className="w-12 h-12 mx-auto mb-3 rounded-2xl flex items-center justify-center transition-transform duration-500 group-hover:scale-110 group-hover:rotate-6"
        style={{
          background: color ? `hsla(${color}, 0.15)` : undefined,
          boxShadow: color ? `0 0 16px -4px hsla(${color}, 0.25)` : undefined,
        }}
      >
        <Icon className="w-5 h-5" style={{ color: color ? `hsl(${color})` : undefined }} />
      </div>
      <p className="text-3xl font-extrabold tracking-tight">
        <span style={{
          background: color
            ? `linear-gradient(135deg, hsl(${color}), hsl(359, 65%, 50%))`
            : undefined,
          WebkitBackgroundClip: "text",
          WebkitTextFillColor: "transparent",
        }}>{count}</span>
      </p>
      <p className="text-[10px] uppercase tracking-[0.2em] text-muted-foreground font-semibold mt-1">{label}</p>
    </div>
  );
};

interface ClassRow {
  id: number;
  name: string;
  trainer: string;
  schedule: string;
  time: string;
  slots: number;
  icon_name?: string;
  accent?: string;
}

const Booking = () => {
  const [search, setSearch] = useState("");
  const [trainerFilter, setTrainerFilter] = useState("all");
  const [booking, setBooking] = useState<number | null>(null);
  const { user } = useAuth();
  const queryClient = useQueryClient();

  const { data: classes = [] } = useQuery<ClassRow[]>({
    queryKey: ["classes"],
    queryFn: async () => {
      try { return await api.get<ClassRow[]>("/classes"); } catch { return []; }
    },
  });

  const trainers = [...new Set(classes.map((c) => c.trainer))];
  const totalSlots = classes.reduce((sum, c) => sum + c.slots, 0);

  const filtered = classes.filter((c) => {
    const matchSearch = c.name.toLowerCase().includes(search.toLowerCase()) || c.trainer.toLowerCase().includes(search.toLowerCase());
    const matchTrainer = trainerFilter === "all" || c.trainer === trainerFilter;
    return matchSearch && matchTrainer;
  });

  const handleBook = async (cls: ClassRow) => {
    if (cls.slots === 0) {
      toast.error("Lớp đã đầy! Vui lòng chọn lớp khác.");
      return;
    }
    if (!user) {
      toast.error("Vui lòng đăng nhập trước!");
      return;
    }
    setBooking(cls.id);
    try {
      const today = new Date();
      const scheduledDate = today.toISOString().split("T")[0];
      const [startTime, endTime] = cls.time.split(" - ");

      await api.post("/schedules", {
        name: cls.name,
        scheduled_date: scheduledDate,
        scheduledDate,
        start_time: startTime.trim(),
        startTime: startTime.trim(),
        end_time: endTime.trim(),
        endTime: endTime.trim(),
        trainer: cls.trainer,
        status: "Sắp tới",
        icon_name: cls.icon_name || "Dumbbell",
        iconName: cls.icon_name || "Dumbbell",
        accent: cls.accent || "38, 92%, 50%",
        class_id: cls.id,
        classId: cls.id,
      });

      try { await api.post(`/classes/${cls.id}/decrement-slot`); } catch (e) { console.warn(e); }
      queryClient.invalidateQueries({ queryKey: ["classes"] });
      toast.success(`Đăng ký lớp thành công: ${cls.name}! Xem tại Lịch của tôi.`);
    } catch (err: any) {
      toast.error("Đăng ký thất bại: " + err.message);
    } finally {
      setBooking(null);
    }
  };

  return (
    <div className="p-6 lg:p-8 max-w-6xl mx-auto space-y-6">
      <div className="animate-slide-in-left">
        <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>
          Chọn lớp phù hợp với lịch trình
        </p>
        <h1 className="text-2xl lg:text-3xl font-extrabold tracking-tight uppercase animate-fade-down" style={{ animationDelay: '200ms' }}>
          <Dumbbell className="w-7 h-7 inline-block mr-2" style={{ color: "hsl(200, 70%, 50%)" }} />
          Danh sách lớp học nhóm
        </h1>
        <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">
          BOOKING
        </p>
      </div>

      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={classes.length} label="Lớp học nhóm" icon={Dumbbell} color="200, 70%, 50%" />
          <StatCounter end={totalSlots} label="Slot trống" icon={TrendingUp} color="152, 60%, 48%" />
          <StatCounter end={trainers.length} label="Huấn luyện viên" icon={Users} color="280, 60%, 55%" />
        </div>
      </div>

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

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {filtered.map((cls, i) => {
          const IconComp = iconMap[cls.icon_name || "Dumbbell"] || Dumbbell;
          const accent = cls.accent || "200, 70%, 50%";
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
                    ? `linear-gradient(90deg, hsl(${accent}), hsl(359, 65%, 50%))`
                    : "hsla(0, 0%, 40%, 0.4)",
                }}
              />

              <div className="flex items-center gap-3 mt-1 mb-3">
                <div
                  className="w-10 h-10 rounded-xl flex items-center justify-center shrink-0 animate-float"
                  style={{
                    animationDelay: `${i * 150}ms`,
                    background: `hsla(${accent}, 0.15)`,
                    boxShadow: `0 0 12px -4px hsla(${accent}, 0.25)`,
                  }}
                >
                  <IconComp className="w-5 h-5" style={{ color: `hsl(${accent})` }} />
                </div>
                <h3 className="font-extrabold text-base uppercase tracking-tight">
                  {cls.name}
                </h3>
              </div>

              <div className="space-y-2 text-sm text-muted-foreground flex-1">
                <p className="flex items-center gap-2">
                  <Calendar className="w-3.5 h-3.5" style={{ color: `hsl(${accent})` }} />
                  Lịch tập: {cls.schedule}
                </p>
                <p className="flex items-center gap-2">
                  <User className="w-3.5 h-3.5" style={{ color: `hsl(${accent})` }} />
                  HLV: {cls.trainer}
                </p>
                <p className="flex items-center gap-2">
                  <Clock className="w-3.5 h-3.5" style={{ color: `hsl(${accent})` }} />
                  Giờ: {cls.time}
                </p>
              </div>

              <div className="mt-3">
                {cls.slots > 0 ? (
                  <p className="text-sm font-bold" style={{ color: "hsl(152, 60%, 55%)" }}>
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
                disabled={cls.slots === 0 || booking === cls.id}
                className={`w-full rounded-xl mt-4 font-bold uppercase tracking-wide text-xs ${cls.slots === 0 ? "opacity-50" : "btn-gradient"}`}
              >
                <span className="relative z-10">
                  {booking === cls.id ? <Loader2 className="w-4 h-4 animate-spin" /> : cls.slots === 0 ? "HẾT CHỖ" : "ĐĂNG KÝ LỚP"}
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
