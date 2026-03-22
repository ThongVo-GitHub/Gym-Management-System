import { NavLink, useLocation } from "react-router-dom";
import {
  LayoutDashboard,
  CreditCard,
  CalendarPlus,
  ScanLine,
  Calendar,
  User,
  LogOut,
  Dumbbell,
  X,
} from "lucide-react";
import { useIsMobile } from "@/hooks/use-mobile";

const navItems = [
  { to: "/dashboard", label: "Tổng quan", icon: LayoutDashboard },
  { to: "/packages", label: "Gói hội viên", icon: CreditCard },
  { to: "/booking", label: "Đặt lịch tập", icon: CalendarPlus },
  { to: "/checkin", label: "Điểm danh", icon: ScanLine },
  { to: "/schedule", label: "Lịch của tôi", icon: Calendar },
  { to: "/profile", label: "Trang cá nhân", icon: User },
];

interface AppSidebarProps {
  onClose?: () => void;
}

const AppSidebar = ({ onClose }: AppSidebarProps) => {
  const location = useLocation();
  const isMobile = useIsMobile();

  return (
    <aside
      className="w-64 min-h-screen flex flex-col p-4 relative z-10"
      style={{
        background: 'hsla(4, 12%, 7%, 0.88)',
        backdropFilter: 'blur(24px) saturate(1.3)',
        borderRight: '1px solid hsla(4, 30%, 18%, 0.3)',
      }}
    >
      {/* Logo + close button on mobile */}
      <div className="flex items-center justify-between px-3 py-4 mb-8">
        <div className="flex items-center gap-2.5">
          <div className="w-10 h-10 rounded-xl flex items-center justify-center btn-gradient">
            <Dumbbell className="w-5 h-5 text-white" />
          </div>
          <span className="text-lg font-extrabold tracking-tight text-foreground">
            TWELVE<span className="text-primary">FIT</span>
          </span>
        </div>
        {isMobile && onClose && (
          <button
            onClick={onClose}
            className="p-2 rounded-xl transition-colors hover:bg-white/10"
          >
            <X className="w-5 h-5 text-foreground" />
          </button>
        )}
      </div>

      {/* Navigation */}
      <nav className="flex-1 space-y-1">
        {navItems.map((item) => {
          const isActive = location.pathname === item.to;
          return (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={isMobile ? onClose : undefined}
              className={`flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all duration-200 ${
                isActive
                  ? "btn-gradient shadow-lg text-white"
                  : "text-[hsl(220,10%,60%)] hover:text-foreground hover:bg-[hsla(220,15%,18%,0.5)]"
              }`}
              style={isActive ? {
                boxShadow: '0 4px 20px -4px hsla(4, 80%, 56%, 0.35)',
              } : undefined}
            >
              <item.icon className="w-[18px] h-[18px]" />
              {item.label}
            </NavLink>
          );
        })}
      </nav>

      {/* Logout */}
      <button
        className="flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium mt-4 transition-all duration-200 active:scale-[0.97]"
        style={{
          background: 'hsla(4, 80%, 56%, 0.1)',
          border: '1px solid hsla(4, 80%, 56%, 0.2)',
          color: 'hsl(4, 80%, 65%)',
        }}
      >
        <LogOut className="w-[18px] h-[18px]" />
        Đăng xuất
      </button>
    </aside>
  );
};

export default AppSidebar;
