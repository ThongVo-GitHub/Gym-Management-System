import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axiosClient from './api/axiosClient';

export default function Login() {
  const location = useLocation();
  const navigate = useNavigate();
  const [mode, setMode] = useState(location.state?.mode || 'login');

  // LOGIC: State quản lý dữ liệu nhập vào
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState(''); // Thêm để đăng ký
  const [error, setError] = useState('');

  // HÀM XỬ LÝ CHUNG CHO CẢ ĐĂNG KÝ VÀ ĐĂNG NHẬP
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      if (mode === 'login') {
        // GỌI API LOGIN
        const response = await axiosClient.post('/auth/login', {
          usernameOrEmail: username,
          password: password
        });
        const token = response.data.token || response.data.accessToken;
        if (!token) {
          throw new Error('Không nhận được token từ server');
        }
        localStorage.setItem('token', token);
        alert("Chào mừng bạn đến với TwelveFit!");
        navigate('/dashboard');
      } else {
        // GỌI API REGISTER (Dựa trên UserController bạn gửi nãy)
        await axiosClient.post('/users/register', {
          username: username,
          password: password,
          fullName: fullName,
          email: username, // Tạm lấy username làm email nếu chưa có ô email riêng
          role: "USER"
        });
        alert("Đăng ký thành công! Giờ thì hãy đăng nhập nhé.");
        setMode('login'); // Chuyển về mode login sau khi đký xong
      }
    } catch (err) {
      console.error("Lỗi:", err.response?.data);
      setError(mode === 'login' ? 'Sai tài khoản hoặc mật khẩu' : 'Đăng ký thất bại, thử lại sau!');
    }
  };

  return (
    <div className="min-h-screen flex flex-col relative text-white">
      {/* BACKGROUND */}
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
              {/* CHỈNH KÍCH THƯỚC LOGO TẠI ĐÂY */}
              <div className="bg-[#d03030] w-[50px] h-[50px] rounded-[15px] flex items-center justify-center mr-4 shadow-lg">
                <img src="/logogym.png" alt="Logo" className="w-8 h-8 object-contain" />
              </div>
              <h3 className="font-bold text-[2rem] tracking-widest m-0 font-serif-luxury">
                TWELVE<span className="text-[#d03030]">FIT</span>
              </h3>
            </div>
            <div className="font-bold text-4xl md:text-[3.5rem] lg:text-[4rem] leading-[1.15] tracking-tight drop-shadow-2xl">
              <div className="text-white font-tagline">Thay đổi cơ thể,</div>
              <div className="text-gray-300 font-tagline">Thay đổi cuộc đời.</div>
              <div className="flex flex-row whitespace-nowrap mt-4">
                <span className="text-white mr-4">Thử ngay,</span>
                <span className="text-[#d03030] font-serif-luxury">TwelveFit.</span>
              </div>
            </div>
          </div>

          {/* CỘT BÊN PHẢI: FORM */}
          <div className="lg:w-[45%] w-full max-w-[440px]">
            <div className="bg-white/5 backdrop-blur-xl rounded-[24px] p-8 lg:p-10 shadow-[0_8px_32px_0_rgba(0,0,0,0.6)] border border-white/10">
              <h5 className="text-center font-bold text-xl mb-8 text-white tracking-wide">
                {mode === 'login' ? 'Đăng nhập thành viên' : 'Đăng ký hội viên mới'}
              </h5>

              {error && <p className="text-[#d03030] text-center text-sm mb-4 font-bold">{error}</p>}

              <form className="space-y-4" onSubmit={handleSubmit}>
                {mode === 'register' && (
                  <input
                    type="text"
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    className="w-full bg-[#1a1a1a]/80 border border-gray-700 rounded-xl px-5 py-4 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-[#d03030] transition-all"
                    placeholder="Họ và tên"
                    required
                  />
                )}

                <input
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  className="w-full bg-[#1a1a1a]/80 border border-gray-700 rounded-xl px-5 py-4 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-[#d03030] transition-all"
                  placeholder="Username hoặc Email"
                  required
                />

                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full bg-[#1a1a1a]/80 border border-gray-700 rounded-xl px-5 py-4 text-sm text-white placeholder-gray-500 focus:outline-none focus:border-[#d03030] transition-all"
                  placeholder="Mật khẩu"
                  required
                />

                <button
                  type="submit"
                  className="w-full bg-[#d03030] text-white font-bold py-4 rounded-xl shadow-lg hover:bg-[#b52a2a] hover:scale-[1.02] active:scale-95 transition-all mt-2 tracking-widest uppercase text-sm"
                >
                  {mode === 'login' ? 'ĐĂNG NHẬP' : 'HOÀN TẤT ĐĂNG KÝ'}
                </button>

                <div className="flex items-center py-2">
                  <hr className="flex-grow border-gray-700" />
                  <span className="px-4 text-[10px] text-gray-500 uppercase tracking-widest">Hoặc</span>
                  <hr className="flex-grow border-gray-700" />
                </div>

                <button
                  type="button"
                  onClick={() => {
                    setMode(mode === 'login' ? 'register' : 'login');
                    setError(''); // Xóa lỗi khi chuyển mode
                  }}
                  className="w-full bg-[#2a2a2a]/80 text-white font-bold py-4 rounded-xl border border-gray-600 hover:bg-[#333] hover:scale-[1.02] active:scale-95 transition-all tracking-widest uppercase text-sm"
                >
                  {mode === 'login' ? 'TẠO TÀI KHOẢN MỚI' : 'ĐÃ CÓ TÀI KHOẢN'}
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}