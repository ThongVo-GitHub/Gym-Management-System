import React from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Users, 
  LayoutDashboard, 
  ShieldCheck, 
  ChevronRight 
} from 'lucide-react';
// IMPORT CÁC SERVICE BẠN ĐÃ TẠO
import userService from '@/services/userService';
import membershipService from '@/services/membershipService';

const SettingPage = () => {
  const navigate = useNavigate();

  const settingsOptions = [
    {
      id: 'members',
      title: "Quản lý Hội viên",
      desc: "Chỉnh sửa thông tin cá nhân và tài khoản thành viên",
      icon: <Users size={24} />,
      path: "/members",
      service: userService.getAllUsers // Gắn service tương ứng
    },
    {
      id: 'billing',
      title: "Thống kê Doanh thu",
      desc: "Xem báo cáo, hóa đơn và biểu đồ tại Dashboard",
      icon: <LayoutDashboard size={24} />,
      path: "/dashboard",
      service: membershipService.getIncomeStats
    },
    {
      id: 'security',
      title: "Phân quyền & Bảo mật",
      desc: "Quản lý quyền truy cập và an toàn dữ liệu hội viên",
      icon: <ShieldCheck size={24} />,
      path: "/members",
      service: userService.getAllUsers
    },
  ];

  // Hàm xử lý khi click card
  const handleItemClick = async (item) => {
    try {
      // Gọi thử API trước khi chuyển trang để đảm bảo server đang sống/có quyền
      console.log(`Đang gọi API cho: ${item.title}...`);
      await item.service(); 
      
      // Nếu API ok, thực hiện chuyển trang
      navigate(item.path);
    } catch (error) {
      console.error("Lỗi khi kết nối API:", error);
      // Bạn có thể thêm toast thông báo lỗi ở đây
      navigate(item.path); // Vẫn cho qua trang nếu bạn muốn
    }
  };

  return (
    <main className="flex-1 flex flex-col h-screen overflow-hidden font-sans relative">
      <div 
        className="absolute inset-0 z-0"
        style={{
          backgroundImage: "url('/bgmemdash.png')",
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat'
        }}
      >
        <div className="absolute inset-0 bg-black/75 backdrop-blur-[4px]"></div>
      </div>

      <div className="relative z-10 flex-1 overflow-auto p-8 lg:p-16">
        <header className="mb-12">
          <h1 className="text-4xl font-black tracking-widest text-white uppercase italic">
            Cài Đặt <span className="text-red-600">Điều Hướng</span>
          </h1>
          <div className="h-1.5 w-24 bg-red-600 mt-3 rounded-full shadow-[0_0_15px_rgba(220,38,38,0.5)]"></div>
          <p className="text-gray-400 mt-4 text-sm font-medium italic">
            Truy cập nhanh các phân vùng quản trị TwelveFit.
          </p>
        </header>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-6xl">
          {settingsOptions.map((item, index) => (
            <div 
              key={index}
              onClick={() => handleItemClick(item)} // Gọi hàm xử lý mới
              className="group relative bg-white/5 backdrop-blur-2xl border border-white/10 p-8 rounded-[2rem] 
                         flex items-center justify-between cursor-pointer 
                         hover:bg-white/10 hover:border-red-600/40 transition-all duration-500 shadow-2xl"
            >
              <div className="flex items-center gap-8">
                <div className="w-16 h-16 bg-red-600 rounded-2xl flex items-center justify-center 
                              shadow-[0_10px_20px_rgba(220,38,38,0.3)] group-hover:scale-110 group-hover:rotate-3 transition-all duration-300">
                  {React.cloneElement(item.icon, { className: "text-white" })}
                </div>

                <div>
                  <h3 className="text-xl font-black text-white group-hover:text-red-500 transition-colors uppercase tracking-tight">
                    {item.title}
                  </h3>
                  <p className="text-xs text-gray-400 mt-2 font-medium tracking-wide">
                    {item.desc}
                  </p>
                </div>
              </div>

              <div className="bg-white/5 p-2 rounded-full group-hover:bg-red-600 transition-colors">
                <ChevronRight 
                  size={20} 
                  className="text-gray-500 group-hover:text-white group-hover:translate-x-1 transition-all" 
                />
              </div>
            </div>
          ))}
        </div>

        <footer className="mt-16 border-t border-white/5 pt-6 text-gray-500 text-[10px] font-black uppercase tracking-[0.2em]">
          TwelveFit Management System • Version 4.0.1
        </footer>
      </div>
    </main>
  );
};

export default SettingPage;