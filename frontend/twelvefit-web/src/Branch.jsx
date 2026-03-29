import React from 'react';
import { 
  MapPin, Phone, Users, Activity, Edit3, Trash2, Plus, 
  ExternalLink, CheckCircle2, AlertCircle, Clock 
} from 'lucide-react';

const Branch = () => {
  // Dữ liệu danh sách chi nhánh
  const branches = [
    { 
      id: 1, 
      name: 'TwelveFit Quận 1', 
      address: '123 Lê Lợi, Phường Bến Thành, Quận 1, TP.HCM',
      manager: 'Nguyễn Văn A',
      phone: '0901.234.567',
      status: 'Đang hoạt động',
      load: '85%', // Độ lấp đầy
      members: 450,
      image: 'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=500&auto=format&fit=crop'
    },
    { 
      id: 2, 
      name: 'TwelveFit Quận 7', 
      address: '456 Nguyễn Văn Linh, Tân Phong, Quận 7, TP.HCM',
      manager: 'Trần Thị B',
      phone: '0902.345.678',
      status: 'Bảo trì',
      load: '0%',
      members: 320,
      image: 'https://images.unsplash.com/photo-1540497077202-7c8a3999166f?q=80&w=500&auto=format&fit=crop'
    },
    { 
      id: 3, 
      name: 'TwelveFit Thủ Đức', 
      address: '789 Võ Văn Ngân, Linh Chiểu, Thủ Đức, TP.HCM',
      manager: 'Lê Minh C',
      phone: '0903.456.789',
      status: 'Đang hoạt động',
      load: '40%',
      members: 210,
      image: 'https://images.unsplash.com/photo-1571902943202-507ec2618e8f?q=80&w=500&auto=format&fit=crop'
    },
  ];

  return (
    <main className="flex-1 flex flex-col h-screen overflow-hidden font-sans relative text-white">
      
      {/* 1. NỀN ẢNH TỪ PUBLIC (Đồng bộ hệ thống) */}
      <div 
        className="absolute inset-0 z-0"
        style={{
          backgroundImage: "url('/bgmemdash.png')",
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      >
        <div className="absolute inset-0 bg-black/85 backdrop-blur-[4px]"></div>
      </div>

      {/* --- NỘI DUNG CHÍNH (z-10) --- */}
      <div className="relative z-10 flex-1 overflow-auto p-8 lg:p-12">
        
        {/* Header Section */}
        <div className="flex flex-col md:flex-row justify-between items-end mb-10 gap-6">
          <div>
            <h2 className="text-3xl font-black uppercase italic tracking-wider">
              Hệ Thống <span className="text-red-600">Chi Nhánh</span>
            </h2>
            <div className="h-1 w-24 bg-red-600 mt-2 rounded-full mb-4 shadow-[0_0_15px_rgba(220,38,38,0.5)]"></div>
            <p className="text-gray-400 text-[10px] font-black uppercase tracking-[0.2em]">Quản lý vận hành và mạng lưới cơ sở TwelveFit</p>
          </div>

          <button className="flex items-center gap-2 bg-red-600 hover:bg-red-700 text-white px-8 py-3.5 rounded-2xl font-black text-xs uppercase tracking-widest transition-all shadow-xl active:scale-95 group">
            <Plus size={18} className="group-hover:rotate-90 transition-transform" /> Khai trương chi nhánh mới
          </button>
        </div>

        {/* Grid Chi Nhánh */}
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-8 max-w-7xl mx-auto">
          {branches.map((branch) => (
            <div 
              key={branch.id} 
              className="group bg-white/5 backdrop-blur-2xl border border-white/10 rounded-[2.5rem] overflow-hidden flex flex-col md:flex-row shadow-2xl transition-all hover:border-red-600/30"
            >
              {/* Ảnh Chi Nhánh */}
              <div className="w-full md:w-48 h-48 md:h-auto relative">
                <img 
                  src={branch.image} 
                  alt={branch.name} 
                  className="w-full h-full object-cover grayscale group-hover:grayscale-0 transition-all duration-500"
                />
                <div className="absolute inset-0 bg-gradient-to-t md:bg-gradient-to-r from-black/80 to-transparent"></div>
              </div>

              {/* Thông tin Chi Nhánh */}
              <div className="flex-1 p-6 flex flex-col justify-between">
                <div>
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="text-xl font-black uppercase tracking-tight text-white group-hover:text-red-500 transition-colors">
                      {branch.name}
                    </h3>
                    <div className="flex gap-2">
                       <button className="p-2 bg-white/5 rounded-xl hover:bg-blue-600/20 hover:text-blue-400 transition-all"><Edit3 size={14}/></button>
                       <button className="p-2 bg-white/5 rounded-xl hover:bg-red-600 transition-all"><Trash2 size={14}/></button>
                    </div>
                  </div>
                  
                  <p className="flex items-center gap-2 text-[11px] text-gray-400 mb-4 font-medium italic">
                    <MapPin size={12} className="text-red-600" /> {branch.address}
                  </p>

                  {/* Chỉ số vận hành */}
                  <div className="grid grid-cols-3 gap-4 mb-6">
                    <div className="bg-white/5 p-3 rounded-2xl border border-white/5">
                        <p className="text-[8px] text-gray-500 font-black uppercase mb-1">Hội viên</p>
                        <div className="flex items-center gap-1.5 font-black text-sm">
                            <Users size={14} className="text-red-500" /> {branch.members}
                        </div>
                    </div>
                    <div className="bg-white/5 p-3 rounded-2xl border border-white/5 text-center">
                        <p className="text-[8px] text-gray-500 font-black uppercase mb-1">Tình trạng</p>
                        <span className={`text-[9px] font-black uppercase ${branch.status === 'Đang hoạt động' ? 'text-green-500' : 'text-yellow-500'}`}>
                            {branch.status}
                        </span>
                    </div>
                    <div className="bg-white/5 p-3 rounded-2xl border border-white/5">
                        <p className="text-[8px] text-gray-500 font-black uppercase mb-1">Độ lấp đầy</p>
                        <div className="flex items-center gap-1.5 font-black text-sm">
                            <Activity size={14} className="text-blue-400" /> {branch.load}
                        </div>
                    </div>
                  </div>
                </div>

                {/* Footer Card */}
                <div className="flex items-center justify-between pt-4 border-t border-white/5">
                   <div className="flex items-center gap-2 text-[10px] text-gray-500 font-bold">
                      <Phone size={12} /> {branch.phone}
                   </div>
                   <button className="flex items-center gap-1 text-[10px] font-black uppercase tracking-widest text-red-500 hover:text-white transition-colors">
                      Chi tiết vận hành <ExternalLink size={12} />
                   </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Thống kê nhanh footer */}
        <div className="mt-12 flex gap-8 justify-center opacity-50">
           <div className="flex items-center gap-2 text-[10px] font-black uppercase tracking-[0.2em]">
              <CheckCircle2 size={14} className="text-green-500"/> 02 Chi nhánh Online
           </div>
           <div className="flex items-center gap-2 text-[10px] font-black uppercase tracking-[0.2em]">
              <Clock size={14} className="text-yellow-500"/> 01 Đang bảo trì
           </div>
           <div className="flex items-center gap-2 text-[10px] font-black uppercase tracking-[0.2em]">
              <AlertCircle size={14} className="text-red-500"/> 00 Sự cố kỹ thuật
           </div>
        </div>
      </div>
    </main>
  );
};

export default Branch;