import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';

export default function Login() {
  const location = useLocation();
  const [mode, setMode] = useState(location.state?.mode || 'login');

  return (
    <div className="min-h-screen flex flex-col relative font-['Montserrat',sans-serif]">
      <style>
        {`@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap');`}
      </style>

      {/* BACKGROUND ĐEN SANG TRỌNG */}
      <div className="fixed inset-0 z-[-1] overflow-hidden bg-black">
        <div
          className="absolute -inset-5 bg-cover bg-center filter blur-sm opacity-50"
          style={{ backgroundImage: 'url(/anh.avif)' }}
        ></div>
        <div className="absolute inset-0 bg-gradient-to-r from-black/95 via-black/80 to-black/40"></div>
      </div>

      <div className="flex-grow flex items-center justify-center p-4">
        <div className="max-w-[1200px] w-full flex flex-col lg:flex-row items-center justify-between gap-12 z-10">
          
          {/* CỘT BÊN TRÁI: LOGO & SLOGAN */}
          <div className="lg:w-[55%] w-full lg:pr-10">
            <div className="flex items-center mb-8">
              <div className="bg-[#d03030] w-[65px] h-[65px] rounded-[20px] flex items-center justify-center mr-5 shadow-lg">
                <img src="/logogym.png" alt="Logo" className="w-10 h-10 object-contain" />
              </div>
              <h3 className="font-bold text-[2.2rem] tracking-widest text-white m-0">TWELVE<span className="text-[#d03030]">FIT</span></h3>
            </div>
            
            <div className="font-bold text-4xl md:text-[3.5rem] lg:text-[4rem] leading-[1.15] tracking-tight drop-shadow-2xl">
              <div className="text-white">Thay đổi cơ thể,</div>
              <div className="text-gray-300">Thay đổi cuộc đời.</div>
              <div className="flex flex-row whitespace-nowrap mt-4">
                <span className="text-white mr-4">Thử ngay,</span>
                <span className="text-[#d03030]">TwelveFit.</span>
              </div>
            </div>
            
            <div className="mt-10 flex items-center bg-white/5 backdrop-blur-sm p-2 pr-6 rounded-full border border-white/10 w-max">
              <div className="flex -space-x-3">
                <img className="w-10 h-10 rounded-full border-2 border-[#111]" src="https://i.pravatar.cc/100?img=12" alt="User" />
                <img className="w-10 h-10 rounded-full border-2 border-[#111]" src="https://i.pravatar.cc/100?img=33" alt="User" />
                <img className="w-10 h-10 rounded-full border-2 border-[#111]" src="https://i.pravatar.cc/100?img=47" alt="User" />
                <div className="w-10 h-10 rounded-full border-2 border-[#111] bg-[#d03030] flex items-center justify-center text-[10px] font-bold text-white z-10">9k+</div>
              </div>
              <p className="ml-4 text-xs text-gray-300 font-medium tracking-wide">Đã thay đổi thể hình thành công.</p>
            </div>
          </div>

          {/* CỘT BÊN PHẢI: FORM ĐEN NHÁM (DARK PREMIUM) */}
          <div className="lg:w-[45%] w-full max-w-[440px]">
            <div className="bg-white/5 backdrop-blur-xl rounded-[24px] p-8 lg:p-10 shadow-[0_8px_32px_0_rgba(0,0,0,0.6)] border border-white/10">
              <h5 className="text-center font-bold text-xl mb-8 text-white tracking-wide">
                {mode === 'login' ? 'Đăng nhập thành viên' : 'Đăng ký hội viên mới'}
              </h5>
              
              <form className="space-y-4">
                {mode === 'register' && (
                  <input 
                    type="text" 
                    className="w-full bg-[#1a1a1a]/80 border border-gray-700 rounded-xl px-5 py-4 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-[#d03030] transition-all" 
                    placeholder="Họ và tên" 
                  />
                )}
                
                <input 
                  type="text" 
                  className="w-full bg-[#1a1a1a]/80 border border-gray-700 rounded-xl px-5 py-4 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-[#d03030] transition-all" 
                  placeholder="Email hoặc ID" 
                />
                
                <input 
                  type="password" 
                  className="w-full bg-[#1a1a1a]/80 border border-gray-700 rounded-xl px-5 py-4 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-[#d03030] transition-all" 
                  placeholder="Mật khẩu" 
                />
                
                {mode === 'login' && (
                  <div className="text-right">
                    <button type="button" className="text-gray-400 hover:text-white text-[12px] font-medium transition-colors">
                      Quên mật khẩu?
                    </button>
                  </div>
                )}
                
                {mode === 'register' && (
                  <input 
                    type="password" 
                    className="w-full bg-[#1a1a1a]/80 border border-gray-700 rounded-xl px-5 py-4 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-[#d03030] transition-all" 
                    placeholder="Xác nhận mật khẩu" 
                  />
                )}
                
                <button type="button" className="w-full bg-[#d03030] text-white font-bold py-4 rounded-xl shadow-lg hover:bg-[#b52a2a] hover:scale-[1.02] active:scale-95 transition-all mt-2 tracking-widest uppercase text-sm">
                  {mode === 'login' ? 'ĐĂNG NHẬP' : 'HOÀN TẤT ĐĂNG KÝ'}
                </button>
                
                <div className="flex items-center py-2">
                  <hr className="flex-grow border-gray-700" />
                  <span className="px-4 text-[10px] text-gray-500 uppercase tracking-widest">Hoặc</span>
                  <hr className="flex-grow border-gray-700" />
                </div>
                
                <button 
                  type="button" 
                  onClick={() => setMode(mode === 'login' ? 'register' : 'login')}
                  className="w-full bg-[#2a2a2a]/80 text-white font-bold py-4 rounded-xl border border-gray-600 hover:bg-[#333] hover:scale-[1.02] active:scale-95 transition-all tracking-widest uppercase text-sm"
                >
                  {mode === 'login' ? 'TẠO TÀI KHOẢN MỚI' : 'ĐÃ CÓ TÀI KHOẢN'}
                </button>
              </form>
            </div>
          </div>

        </div>
      </div>

      <footer className="py-6 text-center relative z-10 flex flex-col items-center">
        <p className="text-gray-500 italic text-xs drop-shadow-sm mb-2">
          "Buổi tập khó nhất chính là buổi tập bạn không bắt đầu."
        </p>
        <span className="text-gray-300 font-bold text-xs hover:text-white transition-colors cursor-pointer tracking-widest uppercase">
          HOTLINE: 1900 3434
        </span>
      </footer>
    </div>
  );
}