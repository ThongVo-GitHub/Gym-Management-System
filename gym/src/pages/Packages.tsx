import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Search, Clock, ArrowRight, Dumbbell, Crown, GraduationCap, Gem, Award, Sparkles, Users, Zap } from "lucide-react";
import { toast } from "sonner";
import { useCountUp } from "@/hooks/useCountUp";

const packages = [
  {
    id: 1,
    name: "TWELVE LITE",
    duration: "1 tháng",
    price: "500.000",
    description: "Gói cơ bản dành cho người mới bắt đầu. Tự do tập gym không giới hạn thời gian trong ngày, sử dụng toàn bộ thiết bị tại phòng tập.",
    popular: false,
    label: "",
    icon: Dumbbell,
  },
  {
    id: 2,
    name: "TWELVE STUDENT",
    duration: "3 tháng",
    price: "1.200.000",
    description: "Ưu đãi dành cho học sinh, sinh viên. Tập gym không giới hạn, được hướng dẫn lịch tập cơ bản và tham gia 4 buổi group training miễn phí.",
    popular: false,
    label: "ƯU ĐÃI",
    icon: GraduationCap,
  },
  {
    id: 3,
    name: "TWELVE ELITE",
    duration: "6 tháng",
    price: "2.400.000",
    description: "Gói phổ biến nhất. Tập gym không giới hạn, 12 buổi group training, 3 buổi PT cá nhân, tư vấn dinh dưỡng cơ bản và ưu tiên đặt lịch.",
    popular: true,
    label: "PHỔ BIẾN",
    icon: Award,
  },
  {
    id: 4,
    name: "TWELVE PLATINUM",
    duration: "12 tháng",
    price: "4.200.000",
    description: "Gói cao cấp nhất. Full quyền lợi, PT riêng hàng tuần, group training không giới hạn, tư vấn dinh dưỡng chuyên sâu, locker riêng và khăn tập miễn phí.",
    popular: false,
    label: "",
    icon: Crown,
  },
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

const Packages = () => {
  const [search, setSearch] = useState("");

  const filtered = packages.filter((p) =>
    p.name.toLowerCase().includes(search.toLowerCase())
  );

  const handleRegister = (pkg: (typeof packages)[0]) => {
    toast.success(`Đăng ký thành công gói ${pkg.name}!`);
  };

  return (
    <div className="p-6 lg:p-8 max-w-6xl mx-auto space-y-6">
      {/* Header with stroke text */}
      <div className="animate-slide-in-left">
        <p className="text-muted-foreground text-sm font-medium animate-fade-down" style={{ animationDelay: '100ms' }}>
          Khám phá các gói tập phù hợp
        </p>
        <h1 className="text-2xl lg:text-3xl font-extrabold tracking-tight uppercase animate-fade-down" style={{ animationDelay: '200ms' }}>
          Các gói hội viên TwelveFit
        </h1>
        <p className="stroke-text-subtle text-4xl lg:text-5xl font-black uppercase tracking-wider mt-1 select-none" aria-hidden="true">
          PACKAGES
        </p>
      </div>

      {/* Stats bar */}
      <div className="glass-card p-6 animate-scale-up" style={{ animationDelay: '250ms' }}>
        <div className="grid grid-cols-3 gap-6">
          <StatCounter end={4} label="Gói tập luyện" icon={Sparkles} />
          <StatCounter end={978} label="Hội viên tin dùng" icon={Users} />
          <StatCounter end={98} label="% Hài lòng" icon={Zap} />
        </div>
      </div>

      {/* Search */}
      <div className="relative max-w-sm animate-slide-in-left" style={{ animationDelay: "300ms" }}>
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
        <Input
          placeholder="Tìm kiếm gói tập..."
          className="pl-9 bg-transparent border-border rounded-xl"
          style={{
            background: 'hsla(4, 15%, 10%, 0.5)',
            backdropFilter: 'blur(12px)',
          }}
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      {/* Package cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {filtered.map((pkg, i) => {
          const IconComp = pkg.icon;
          return (
            <div
              key={pkg.id}
              className="glass-card-hover p-6 relative flex flex-col animate-slide-in-left"
              style={{ animationDelay: `${350 + i * 100}ms` }}
            >
              <div
                className="absolute top-0 left-6 right-6 h-[3px] rounded-b-full"
                style={{
                  background: pkg.popular
                    ? "linear-gradient(90deg, hsl(4, 80%, 56%), hsl(24, 95%, 55%))"
                    : "hsla(4, 40%, 40%, 0.4)",
                }}
              />

              <div className="w-10 h-10 icon-glow-red mb-3 mt-1 animate-float" style={{ animationDelay: `${i * 200}ms` }}>
                <IconComp className="w-5 h-5 text-primary" />
              </div>

              <div className="flex items-center gap-2 mb-3">
                <span className="text-xs font-bold uppercase tracking-wider text-primary flex items-center gap-1">
                  <Clock className="w-3 h-3" /> {pkg.duration}
                </span>
                {pkg.label && (
                  <span
                    className="text-[9px] font-bold uppercase px-2 py-0.5 rounded-full animate-pulse-glow"
                    style={{
                      background: pkg.popular
                        ? "hsla(4, 80%, 56%, 0.15)"
                        : "hsla(24, 60%, 50%, 0.15)",
                      color: pkg.popular
                        ? "hsl(4, 80%, 65%)"
                        : "hsl(24, 70%, 60%)",
                    }}
                  >
                    {pkg.label}
                  </span>
                )}
              </div>

              <h3 className="text-lg font-extrabold tracking-tight leading-tight">
                {pkg.name}
              </h3>

              <div className="mt-3 mb-1">
                <span className="text-2xl font-extrabold tracking-tight bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">{pkg.price}</span>
                <span className="text-sm text-muted-foreground ml-1">VNĐ</span>
              </div>

              <p className="text-xs text-muted-foreground leading-relaxed mt-2 flex-1">
                {pkg.description}
              </p>

              <Button
                size="sm"
                onClick={() => handleRegister(pkg)}
                className="w-full rounded-xl btn-gradient mt-5"
              >
                <span className="relative z-10 flex items-center gap-1 font-bold uppercase tracking-wide text-xs">
                  Đăng ký <ArrowRight className="w-3.5 h-3.5" />
                </span>
              </Button>
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

export default Packages;
