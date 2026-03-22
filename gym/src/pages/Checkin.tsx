import { useState } from "react";
import { Button } from "@/components/ui/button";
import { QrCode, CheckCircle, MapPin, Clock, Sparkles, ShieldCheck, UserCheck, ScanLine, TrendingUp, Calendar } from "lucide-react";
import { toast } from "sonner";
import { useCountUp } from "@/hooks/useCountUp";

const history = [
  { date: "20/03/2026", time: "07:15", branch: "Chi nhánh Quận 1" },
  { date: "19/03/2026", time: "08:25", branch: "Chi nhánh Quận 1" },
  { date: "18/03/2026", time: "17:40", branch: "Chi nhánh Quận 7" },
  { date: "17/03/2026", time: "06:50", branch: "Chi nhánh Quận 1" },
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

const Checkin = () => {
  const [showQR, setShowQR] = useState(false);

  const handleGenerate = () => {
    setShowQR(true);
    toast.success("Mã QR đã được tạo thành công!");
  };

  return (
    <div className="p-6 lg:p-8 max-w-4xl mx-auto space-y-6">
      {/* Header with stroke text */}
      <div className="animate-slide-in-left">
        <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>
          Điểm danh nhanh chóng với mã QR
        </p>
        <h1 className="text-2xl lg:text-3xl font-extrabold tracking-tight animate-fade-down" style={{ animationDelay: '200ms' }}>
          <ScanLine className="w-7 h-7 inline-block mr-2 text-primary" />
          QR Check-in
        </h1>
        <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">
          CHECK-IN
        </p>
      </div>

      {/* Stats bar */}
      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={48} label="Lần check-in" icon={ScanLine} />
          <StatCounter end={12} label="Tháng liên tục" icon={TrendingUp} />
          <StatCounter end={3} label="Chi nhánh" icon={MapPin} />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* QR section */}
        <div className="glass-card p-6 text-center space-y-5 animate-slide-in-left" style={{ animationDelay: "350ms" }}>
          <h2 className="font-bold text-lg tracking-tight flex items-center justify-center gap-2">
            <QrCode className="w-5 h-5 text-primary animate-float" /> Mã QR của bạn
          </h2>

          <div
            className="rounded-xl p-4 text-left space-y-2.5"
            style={{
              background: "hsla(4, 18%, 12%, 0.45)",
              border: "1px solid hsla(4, 40%, 22%, 0.25)",
            }}
          >
            <p className="text-sm flex items-center gap-2">
              <UserCheck className="w-3.5 h-3.5 text-primary/70" />
              <span className="text-muted-foreground">Họ tên:</span>{" "}
              <span className="font-semibold">Nguyễn Minh Tuấn</span>
            </p>
            <p className="text-sm flex items-center gap-2">
              <ShieldCheck className="w-3.5 h-3.5 text-primary/70" />
              <span className="text-muted-foreground">Trạng thái:</span>{" "}
              <span className="status-active">Đang hoạt động</span>
            </p>
            <p className="text-sm">
              <span className="text-muted-foreground">ID:</span>{" "}
              <span className="font-mono font-bold">TF-9876</span>
            </p>
          </div>

          {showQR ? (
            <div className="space-y-3">
              <div
                className="w-44 h-44 mx-auto rounded-2xl flex items-center justify-center qr-float animate-glow-pulse"
                style={{
                  background: "linear-gradient(135deg, hsla(0,0%,95%,0.95), hsla(0,0%,90%,0.9))",
                }}
              >
                <div className="text-center">
                  <QrCode className="w-24 h-24 mx-auto text-[hsl(220,20%,10%)]" />
                  <p className="text-xs font-mono font-bold text-[hsl(220,20%,15%)] mt-1">
                    TF-9876
                  </p>
                </div>
              </div>
              <p className="text-xs text-muted-foreground">
                Đưa mã này trước scanner tại quầy lễ tân
              </p>
            </div>
          ) : (
            <div className="py-8">
              <QrCode className="w-16 h-16 mx-auto text-muted-foreground/20 mb-3" />
              <p className="text-sm text-muted-foreground">
                Nhấn nút bên dưới để tạo mã QR
              </p>
            </div>
          )}

          <Button onClick={handleGenerate} className="w-full rounded-xl btn-gradient">
            <span className="relative z-10 flex items-center gap-2">
              <Sparkles className="w-4 h-4" />
              {showQR ? "Tạo lại mã QR" : "Tạo mã QR"}
            </span>
          </Button>
        </div>

        {/* History */}
        <div className="glass-card p-6 animate-slide-in-right" style={{ animationDelay: "400ms" }}>
          <h2 className="font-bold text-lg mb-4 tracking-tight flex items-center gap-2">
            <Clock className="w-5 h-5 text-primary animate-float" /> Lịch sử Check-in
          </h2>
          <div className="space-y-3">
            {history.map((h, i) => (
              <div
                key={i}
                className="flex items-center gap-3 p-3 rounded-xl activity-row cursor-default"
                style={{
                  background: "hsla(4, 18%, 12%, 0.4)",
                  animationDelay: `${450 + i * 80}ms`,
                }}
              >
                <div className="w-9 h-9 icon-glow-green shrink-0">
                  <CheckCircle className="w-4 h-4 text-success" />
                </div>
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 text-sm">
                    <span className="flex items-center gap-1 text-muted-foreground">
                      <Calendar className="w-3 h-3" /> {h.date} - {h.time}
                    </span>
                  </div>
                  <p className="text-xs text-muted-foreground flex items-center gap-1 mt-0.5">
                    <MapPin className="w-3 h-3" /> {h.branch}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div
        className="glass-card p-4 text-sm text-muted-foreground animate-fade-in flex items-center gap-2"
        style={{ animationDelay: "600ms" }}
      >
        💡 Lưu ý: Mã QR có hiệu lực trong 5 phút. Vui lòng tạo mã mới nếu hết hạn.
      </div>
    </div>
  );
};

export default Checkin;
