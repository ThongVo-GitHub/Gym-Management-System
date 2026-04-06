import React from 'react';
import { Search, Filter, Lock, Edit2, Eye, UserPlus, MoreHorizontal } from 'lucide-react';

const MemberManagement = () => {
  const members = [
    { id: "TW001", name: "Nguyễn Lâm Nhật", email: "nhat.lam@gmail.com", status: "Active", package: "Elite" },
    { id: "TW002", name: "Trần Thu Thảo", email: "thao.tran@outlook.com", status: "Expired", package: "Pro" },
    { id: "TW003", name: "Lê Minh Tâm", email: "tam.le@gmail.com", status: "Locked", package: "Basic" },
    { id: "TW004", name: "Hoàng Gia Bảo", email: "bao.gia@gmail.com", status: "Active", package: "Elite" },
  ];

  return (
    <main className="flex-1 flex flex-col h-screen overflow-hidden font-sans relative">
      
      {/* 1. NỀN ẢNH TỪ THƯ MỤC PUBLIC */}
      <div 
        className="absolute inset-0 z-0"
        style={{
          backgroundImage: "url('/bgmemdash.png')",
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat'
        }}
      >
        {/* Lớp phủ tối sâu hơn một chút để bảng dữ liệu dễ đọc hơn */}
        <div className="absolute inset-0 bg-black/80 backdrop-blur-[2px]"></div>
      </div>

      {/* --- NỘI DUNG CHÍNH (z-10) --- */}
      <div className="relative z-10 flex-1 overflow-auto p-8 lg:p-12">
        
        {/* Header & Button Thêm mới */}
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-10">
          <div>
            <h3 className="text-3xl font-black uppercase tracking-widest text-white italic m-0">
              Quản lý <span className="text-red-600">Hội Viên</span>
            </h3>
            <div className="h-1 w-20 bg-red-600 mt-2 rounded-full"></div>
          </div>
          
          <button className="flex items-center gap-2 bg-red-600 hover:bg-red-700 text-white px-6 py-3 rounded-2xl font-black text-xs uppercase tracking-widest transition-all shadow-[0_10px_20px_rgba(220,38,38,0.3)] active:scale-95">
            <UserPlus size={18} /> Thêm hội viên mới
          </button>
        </div>

        {/* Thanh Tìm kiếm & Bộ lọc (Glassmorphism) */}
        <div className="bg-white/5 backdrop-blur-xl border border-white/10 p-4 rounded-3xl mb-8 flex flex-wrap gap-4 items-center">
          <div className="flex-1 min-w-[300px] relative group">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500 group-focus-within:text-red-500 transition-colors" size={20} />
            <input 
              type="text" 
              className="w-full bg-black/20 border border-white/5 rounded-2xl py-3 pl-12 pr-4 text-sm text-white focus:outline-none focus:border-red-600/50 transition-all placeholder:text-gray-600" 
              placeholder="Tìm theo tên, ID hoặc Email..." 
            />
          </div>
          <button className="flex items-center gap-2 bg-white/5 hover:bg-white/10 text-gray-300 px-5 py-3 rounded-2xl text-sm font-bold border border-white/10 transition-all">
            <Filter size={18} /> Bộ lọc nâng cao
          </button>
        </div>

        {/* Bảng dữ liệu Hội viên */}
        <div className="bg-black/40 backdrop-blur-2xl border border-white/10 rounded-[2rem] overflow-hidden shadow-2xl">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-white/5 text-[11px] text-gray-400 uppercase font-black tracking-[0.2em] border-b border-white/10">
                <th className="p-6">Mã số</th>
                <th>Hội viên</th>
                <th>Gói dịch vụ</th>
                <th>Trạng thái</th>
                <th className="text-center p-6">Hành động</th>
              </tr>
            </thead>
            <tbody className="text-sm divide-y divide-white/5">
              {members.map((m) => (
                <tr key={m.id} className="hover:bg-white/[0.03] transition-colors group">
                  <td className="p-6 font-black text-red-500/80 group-hover:text-red-500 transition-colors">#{m.id}</td>
                  <td>
                    <div className="font-bold text-gray-100">{m.name}</div>
                    <div className="text-[11px] text-gray-500 italic">{m.email}</div>
                  </td>
                  <td>
                    <span className="bg-white/5 border border-white/10 px-3 py-1 rounded-lg text-xs font-bold text-gray-300">
                      {m.package}
                    </span>
                  </td>
                  <td>
                    <span className={`px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-wider shadow-sm ${
                      m.status === 'Active' ? 'bg-green-500/10 text-green-500 border border-green-500/20' : 
                      m.status === 'Locked' ? 'bg-red-500/10 text-red-500 border border-red-500/20' : 
                      'bg-gray-500/10 text-gray-400 border border-gray-500/20'
                    }`}>
                      {m.status}
                    </span>
                  </td>
                  <td className="p-6">
                    <div className="flex justify-center gap-3">
                      <ActionBtn icon={<Eye size={16}/>} color="hover:text-blue-400" />
                      <ActionBtn icon={<Edit2 size={16}/>} color="hover:text-yellow-400" />
                      <ActionBtn icon={<Lock size={16}/>} color="hover:text-red-500" />
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          
          {/* Pagination Tạm thời */}
          <div className="p-6 border-t border-white/5 flex justify-between items-center text-xs text-gray-500 font-bold uppercase tracking-widest">
            <span>Hiển thị 4 trên 1,254 hội viên</span>
            <div className="flex gap-2">
               <button className="px-3 py-1 bg-white/5 rounded-md hover:bg-red-600 hover:text-white transition-all">Trước</button>
               <button className="px-3 py-1 bg-red-600 text-white rounded-md shadow-lg shadow-red-600/20">1</button>
               <button className="px-3 py-1 bg-white/5 rounded-md hover:bg-red-600 hover:text-white transition-all">Sau</button>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

// Component con cho các nút hành động trong bảng
const ActionBtn = ({ icon, color }) => (
  <button className={`p-2.5 bg-white/5 rounded-xl border border-white/5 text-gray-400 transition-all active:scale-90 ${color} hover:bg-white/10 hover:shadow-lg`}>
    {icon}
  </button>
);

export default MemberManagement;