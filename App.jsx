import React from 'react';
import { Dumbbell, ArrowRight } from 'lucide-react';
import { motion } from 'framer-motion';

const MacBookLoginPage = () => {
  return (
    <div className="min-h-screen bg-[#fbfbfd] flex flex-col font-sans antialiased text-[#1d1d1f]">

      {/* Container chính tối ưu cho màn hình Retina */}
      <div className="flex-grow flex flex-col lg:flex-row items-center justify-between px-10 xl:px-32 max-w-[1728px] mx-auto w-full">

        {/* Bên trái: Brand & Slogan (Phong cách Apple-esque) */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, ease: "easeOut" }}
          className="w-full lg:w-3/5 pr-12"
        >
          <div className="flex items-center gap-4 mb-10 group">
            <div className="bg-[#d32f2f] p-3 rounded-2xl shadow-lg shadow-red-200 group-hover:scale-110 transition-transform">
              <Dumbbell size={32} className="text-white" />
            </div>
            <span className="text-3xl font-bold tracking-tight">TWELVE<span className="text-[#d32f2f]">FIT</span></span>
          </div>

          <h1 className="text-5xl xl:text-7xl font-semibold tracking-tight leading-[1.05] mb-8">
            Thay đổi cơ thể <br />
            <span className="text-gray-400">Thay đổi cuộc đời.</span> <br />
            Khám phá ngay, <span className="text-[#d32f2f]">TwelveFit.</span>
          </h1>

          <div className="h-1 w-24 bg-[#d32f2f] rounded-full"></div>
        </motion.div>

        {/* Bên phải: Form Đăng nhập (Glassmorphism chuẩn Mac) */}
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="w-full lg:w-[480px] mt-12 lg:mt-0"
        >
          <div className="bg-white/80 backdrop-blur-xl p-10 rounded-[32px] shadow-[0_20px_50px_rgba(0,0,0,0.1)] border border-white/20">
            <h2 className="text-2xl font-semibold mb-8 text-center">Đăng nhập thành viên</h2>

            <form className="space-y-5">
              <div className="space-y-1">
                <input
                  type="text"
                  placeholder="Email hoặc ID thành viên"
                  className="w-full p-4 bg-gray-100/50 border border-transparent rounded-2xl focus:bg-white focus:ring-4 focus:ring-red-100 focus:border-[#d32f2f] outline-none transition-all duration-300 placeholder:text-gray-400"
                />
              </div>

              <div className="space-y-1">
                <input
                  type="password"
                  placeholder="Mật khẩu"
                  className="w-full p-4 bg-gray-100/50 border border-transparent rounded-2xl focus:bg-white focus:ring-4 focus:ring-red-100 focus:border-[#d32f2f] outline-none transition-all duration-300 placeholder:text-gray-400"
                />
              </div>

              <div className="flex justify-end px-1">
                <a href="#" className="text-sm text-gray-500 hover:text-[#d32f2f] transition-colors">Quên mật khẩu?</a>
              </div>

              <button className="w-full bg-[#d32f2f] hover:bg-[#b71c1c] text-white font-bold py-4 rounded-2xl text-lg transition-all shadow-xl shadow-red-100 flex items-center justify-center gap-2 group">
                ĐĂNG NHẬP
                <ArrowRight size={20} className="group-hover:translate-x-1 transition-transform" />
              </button>
            </form>

            <div className="relative my-10">
              <div className="absolute inset-0 flex items-center"><span className="w-full border-t border-gray-100"></span></div>
              <div className="relative flex justify-center text-xs uppercase"><span className="bg-white px-4 text-gray-400">hoặc</span></div>
            </div>

            <button className="w-full bg-black text-white hover:bg-gray-800 font-bold py-4 rounded-2xl transition-all shadow-lg flex items-center justify-center">
              TẠO TÀI KHOẢN MỚI
            </button>

            <p className="text-center text-[10px] text-gray-400 mt-8 uppercase tracking-[0.2em] font-medium">
              © 2026 TWELVEFIT • PREMIUM GYM SYSTEM
            </p>
          </div>
        </motion.div>
      </div>

      {/* Footer Ngôn ngữ tối giản */}
      <footer className="py-8 text-center">
        <div className="flex justify-center gap-6 text-[13px] text-gray-400 font-medium">
          <span className="text-black">Tiếng Việt</span>
          <a href="#" className="hover:text-black transition-colors">English (US)</a>
          <a href="#" className="hover:text-black transition-colors">日本語</a>
          <a href="#" className="hover:text-black transition-colors">Français</a>
          <a href="#" className="hover:text-black transition-colors">Ngôn ngữ khác</a>
        </div>
      </footer>
    </div>
  );
};

export default MacBookLoginPage;