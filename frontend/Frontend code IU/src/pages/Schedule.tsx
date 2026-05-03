import { Calendar, Clock, User, Dumbbell, Target, HeartPulse, Flame, CheckCircle, Timer, CalendarDays, TrendingUp, Trash2, Loader2 } from "lucide-react";
import { useCountUp } from "@/hooks/useCountUp";
import { useAuth } from "@/hooks/useAuth";
import { api } from "@/lib/api";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

const iconMap: Record<string, typeof Dumbbell> = { Dumbbell, Target, HeartPulse, Flame };

const statusStyles: Record<string, { bg: string; color: string; glow?: string; icon: typeof CheckCircle }> = {
  "Hoàn thành": { bg: "hsla(152, 60%, 48%, 0.12)", color: "hsl(152, 60%, 55%)", icon: CheckCircle },
  "Sắp tới": { bg: "hsla(38, 92%, 50%, 0.12)", color: "hsl(38, 92%, 60%)", glow: "0 0 10px -3px hsla(38, 92%, 50%, 0.3)", icon: Timer },
  "Đã đặt": { bg: "hsla(280, 60%, 55%, 0.12)", color: "hsl(280, 60%, 65%)", icon: CalendarDays },
};

const StatCounter = ({ end, label, icon: Icon, color }: { end: number; label: string; icon: React.ElementType; color?: string }) => {
  const count = useCountUp(end, 2000);
  return (
    <div className="text-center group">
      <div className="w-12 h-12 mx-auto mb-3 rounded-2xl flex items-center justify-center transition-transform duration-500 group-hover:scale-110 group-hover:rotate-6"
        style={{ background: color ? `hsla(${color}, 0.15)` : undefined, boxShadow: color ? `0 0 16px -4px hsla(${color}, 0.25)` : undefined }}>
        <Icon className="w-5 h-5" style={{ color: color ? `hsl(${color})` : undefined }} />
      </div>
      <p className="text-3xl font-extrabold tracking-tight">
        <span style={{ background: color ? `linear-gradient(135deg, hsl(${color}), hsl(359, 65%, 50%))` : undefined, WebkitBackgroundClip: "text", WebkitTextFillColor: "transparent" }}>{count}</span>
      </p>
      <p className="text-[10px] uppercase tracking-[0.2em] text-muted-foreground font-semibold mt-1">{label}</p>
    </div>
  );
};

interface ScheduleRow {
  id: string;
  name: string;
  trainer: string;
  scheduled_date: string;
  start_time?: string;
  end_time?: string;
  status: string;
  icon_name?: string;
  accent?: string;
  class_id: number | null;
}

const Schedule = () => {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [deleting, setDeleting] = useState<string | null>(null);
  const [cancelTarget, setCancelTarget] = useState<{ id: string; name: string; class_id: number | null } | null>(null);

  const { data: schedules = [] } = useQuery<ScheduleRow[]>({
    queryKey: ["user-schedules", user?.id],
    queryFn: async () => {
      try { return await api.get<ScheduleRow[]>("/schedules"); } catch { return []; }
    },
    enabled: !!user,
  });

  const now = new Date();
  const startOfWeek = new Date(now);
  startOfWeek.setDate(now.getDate() - now.getDay() + 1);
  const endOfWeek = new Date(startOfWeek);
  endOfWeek.setDate(startOfWeek.getDate() + 6);

  const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1);
  const endOfMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0);

  const weeklyCount = useMemo(() => schedules.filter(s => {
    const d = new Date(s.scheduled_date);
    return d >= startOfWeek && d <= endOfWeek;
  }).length, [schedules]);

  const monthlyCount = useMemo(() => schedules.filter(s => {
    const d = new Date(s.scheduled_date);
    return d >= startOfMonth && d <= endOfMonth;
  }).length, [schedules]);

  const completedCount = useMemo(() => schedules.filter(s => s.status === "Hoàn thành").length, [schedules]);

  const handleCancel = async () => {
    if (!cancelTarget) return;
    const { id, name, class_id } = cancelTarget;
    setDeleting(id);
    setCancelTarget(null);
    try {
      await api.del(`/schedules/${id}`);

      if (class_id) {
        try { await api.post(`/classes/${class_id}/increment-slot`); } catch (e) { console.warn(e); }
        queryClient.invalidateQueries({ queryKey: ["classes"] });
      }

      queryClient.invalidateQueries({ queryKey: ["user-schedules"] });
      toast.success(`Đã hủy lớp: ${name}`);
    } catch (err: any) {
      toast.error("Hủy thất bại: " + err.message);
    } finally {
      setDeleting(null);
    }
  };

  return (
    <div className="p-6 lg:p-8 max-w-4xl mx-auto space-y-6">
      <div className="animate-slide-in-left">
        <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>Theo dõi lịch tập của bạn</p>
        <h1 className="text-2xl lg:text-3xl font-extrabold tracking-tight animate-fade-down" style={{ animationDelay: '200ms' }}>
          <Calendar className="w-7 h-7 inline-block mr-2" style={{ color: "hsl(280, 60%, 55%)" }} />
          Lịch của tôi
        </h1>
        <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">SCHEDULE</p>
      </div>

      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={weeklyCount} label="Buổi tập tuần này" icon={Dumbbell} color="359, 65%, 50%" />
          <StatCounter end={monthlyCount} label="Buổi tập tháng" icon={TrendingUp} color="280, 60%, 55%" />
          <StatCounter end={completedCount} label="Hoàn thành" icon={CheckCircle} color="152, 60%, 48%" />
        </div>
      </div>

      <div className="space-y-3">
        {schedules.length === 0 ? (
          <div className="glass-card p-8 text-center animate-fade-in">
            <Calendar className="w-12 h-12 mx-auto text-muted-foreground/30 mb-3" />
            <p className="text-muted-foreground">Bạn chưa có lịch tập nào. Hãy đặt lịch tập tại trang Đặt lịch tập!</p>
          </div>
        ) : schedules.map((s, i) => {
          const style = statusStyles[s.status] || statusStyles["Đã đặt"];
          const ClassIcon = iconMap[s.icon_name || "Dumbbell"] || Dumbbell;
          const StatusIcon = style.icon;
          const accent = s.accent || "38, 92%, 50%";
          return (
            <div key={s.id} className="glass-card-hover p-5 flex flex-col sm:flex-row sm:items-center gap-3 animate-slide-in-left" style={{ animationDelay: `${350 + i * 100}ms` }}>
              <div className="w-11 h-11 rounded-xl flex items-center justify-center shrink-0 animate-float"
                style={{ animationDelay: `${i * 200}ms`, background: `hsla(${accent}, 0.15)`, boxShadow: `0 0 12px -4px hsla(${accent}, 0.25)` }}>
                <ClassIcon className="w-5 h-5" style={{ color: `hsl(${accent})` }} />
              </div>
              <div className="flex-1 space-y-1">
                <h3 className="font-bold text-sm">{s.name}</h3>
                <div className="flex flex-wrap gap-3 text-xs text-muted-foreground">
                  <span className="flex items-center gap-1.5"><Calendar className="w-3 h-3" /> {new Date(s.scheduled_date).toLocaleDateString("vi-VN")}</span>
                  <span className="flex items-center gap-1.5"><Clock className="w-3 h-3" /> {s.start_time?.slice(0, 5)} - {s.end_time?.slice(0, 5)}</span>
                  <span className="flex items-center gap-1.5"><User className="w-3 h-3" /> {s.trainer}</span>
                </div>
              </div>
              <div className="flex items-center gap-2 shrink-0">
                <span className="text-xs font-semibold px-3 py-1 rounded-full flex items-center gap-1"
                  style={{ background: style.bg, color: style.color, boxShadow: style.glow }}>
                  <StatusIcon className="w-3 h-3" /> {s.status}
                </span>
                {s.status !== "Hoàn thành" && (
                  <Button
                    variant="ghost"
                    size="icon"
                    className="h-8 w-8 rounded-lg hover:bg-destructive/15 hover:text-destructive transition-colors"
                    onClick={() => setCancelTarget({ id: s.id, name: s.name, class_id: s.class_id })}
                    disabled={deleting === s.id}
                    title="Hủy lớp"
                  >
                    {deleting === s.id ? <Loader2 className="w-4 h-4 animate-spin" /> : <Trash2 className="w-4 h-4" />}
                  </Button>
                )}
              </div>
            </div>
          );
        })}
      </div>

      <AlertDialog open={!!cancelTarget} onOpenChange={(open) => !open && setCancelTarget(null)}>
        <AlertDialogContent className="glass-card border-border">
          <AlertDialogHeader>
            <AlertDialogTitle className="font-extrabold">Xác nhận hủy lớp</AlertDialogTitle>
            <AlertDialogDescription>
              Bạn có chắc muốn hủy lớp <span className="font-bold text-foreground">{cancelTarget?.name}</span>? Slot sẽ được trả lại cho lớp học.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className="rounded-xl">Giữ lại</AlertDialogCancel>
            <AlertDialogAction onClick={handleCancel} className="rounded-xl bg-destructive hover:bg-destructive/90 text-destructive-foreground">
              Hủy lớp
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <footer className="text-center text-xs text-muted-foreground pt-4 pb-2 animate-fade-in" style={{ animationDelay: "800ms" }}>
        © 2023 TwelveFit Gym. All rights reserved.
      </footer>
    </div>
  );
};

export default Schedule;
