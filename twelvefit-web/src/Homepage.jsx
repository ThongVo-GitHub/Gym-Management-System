import React from 'react';
import { Link } from 'react-router-dom';

const Homepage = () => {
  return (
    <div className="bg-[#121212] text-white min-vh-100">
      
      {/* 1. NAVBAR - Kính mờ (Glassmorphism) sang trọng */}
      <nav className="navbar navbar-expand-lg fixed-top bg-black/80 backdrop-blur-md border-b border-white/10 py-3 transition-all duration-300">
        <div className="container d-flex justify-content-between align-items-center">
          <Link to="/" className="navbar-brand d-flex align-items-center text-decoration-none">
            <span className="fs-3 fw-bolder tracking-widest text-white m-0">
              TWELVE<span className="text-[#d03030]">FIT</span>
            </span>
          </Link>
          
          <div className="d-none d-lg-flex gap-5 text-uppercase fw-semibold" style={{ fontSize: '0.8rem', letterSpacing: '2px' }}>
            <a href="#about" className="text-gray-300 text-decoration-none hover:text-[#d03030] transition">Triết lý</a>
            <a href="#services" className="text-gray-300 text-decoration-none hover:text-[#d03030] transition">Dịch vụ</a>
            <a href="#packages" className="text-gray-300 text-decoration-none hover:text-[#d03030] transition">Hội viên</a>
          </div>

          <div className="d-flex gap-3">
            <Link to="/login" state={{ mode: 'login' }} className="btn text-white text-uppercase tracking-widest text-sm hover:text-[#d03030]">
              Đăng Nhập
            </Link>
            <Link to="/login" state={{ mode: 'register' }} className="btn bg-[#d03030] text-white rounded-0 px-4 py-2 text-uppercase tracking-widest text-sm hover:bg-white hover:text-black transition-colors">
              Trải Nghiệm
            </Link>
          </div>
        </div>
      </nav>

      {/* 2. HERO BANNER - Đậm chất Wellness/Elite */}
      <section className="vh-100 position-relative d-flex align-items-center justify-content-center text-center">
        {/* LƯU Ý HÌNH ẢNH: Thêm ảnh background ở đây */}
        <div 
          className="position-absolute top-0 start-0 w-100 h-100"
          style={{ 
            backgroundImage: "url('https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=2070')", 
            backgroundSize: 'cover', 
            backgroundPosition: 'center',
            backgroundAttachment: 'fixed' // Tạo hiệu ứng Parallax nhẹ
          }}
        ></div>
        
        {/* HIỆU ỨNG OVERLAY: Làm tối và mờ nền để nổi bật chữ */}
        <div className="position-absolute top-0 start-0 w-100 h-100 bg-black/60 backdrop-blur-[2px]"></div>
        <div className="position-absolute top-0 start-0 w-100 h-100 bg-gradient-to-t from-[#121212] via-transparent to-transparent"></div>

        <div className="container position-relative z-10 mt-5">
          <h2 className="font-serif-luxury fst-italic text-gray-300 mb-3 fs-3">Khởi nguồn của sự cân bằng</h2>
          <h1 className="display-1 fw-bolder text-uppercase mb-4 tracking-tighter">
            ĐÁNH THỨC <br/> <span className="text-[#d03030]">BẢN THỂ TỐT NHẤT</span>
          </h1>
          <p className="font-tagline fs-5 fw-light mb-5 max-w-2xl mx-auto text-gray-300" style={{ letterSpacing: '1px' }}>
            Không gian tập luyện tiêu chuẩn quốc tế. Tích hợp Gym, Yoga và các liệu pháp phục hồi Wellness chuyên sâu.
          </p>
          <Link to="/login" className="btn bg-white text-black rounded-0 px-5 py-3 text-uppercase fw-bold transition-all hover:bg-[#d03030] hover:text-white" style={{ letterSpacing: '2px' }}>
            Khám Phá Đặc Quyền
          </Link>
        </div>
      </section>

      {/* 3. TRANG GIỚI THIỆU (ABOUT) - Theo yêu cầu SEO & Chuẩn nội dung */}
      <section id="about" className="py-5 bg-[#121212]">
        <div className="container py-5">
          <div className="row align-items-center g-5">
            <div className="col-lg-6">
              <h4 className="text-[#d03030] font-serif-luxury fst-italic mb-2">Về TwelveFit</h4>
              <h2 className="display-5 fw-bold text-uppercase mb-4">Hơn cả một <br/>phòng tập</h2>
              <p className="text-gray-400 mb-4 leading-relaxed text-justify font-light">
                Trong quá trình xây dựng và phát triển, TwelveFit định vị mình không chỉ là nơi cung cấp thiết bị tạ. Chúng tôi là một hệ sinh thái Wellness toàn diện. Với trang thiết bị Technogym hiện đại bậc nhất, không gian Yoga tĩnh tại và đội ngũ Huấn luyện viên chuẩn quốc tế, TwelveFit cam kết đồng hành cùng bạn trên hành trình thay đổi vóc dáng và nâng cao chất lượng sống.
              </p>
              <img src="/signature.png" alt="CEO Signature" className="h-12 opacity-50" /> {/* Chữ ký ảo tạo độ trust */}
            </div>
            <div className="col-lg-6">
              <div className="position-relative">
                {/* Ảnh giới thiệu */}
                <img src="https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=2070" className="w-100 grayscale hover:grayscale-0 transition-all duration-700" alt="Không gian phòng tập" />
                {/* Khung viền offset tạo điểm nhấn thiết kế */}
                <div className="position-absolute border border-[#d03030] w-100 h-100" style={{ top: '20px', left: '-20px', zIndex: -1 }}></div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 4. DỊCH VỤ (SERVICES) - Layout dạng Grid Banner */}
      <section id="services" className="py-0">
        <div className="row g-0">
          {/* Box 1: Gym */}
          <div className="col-md-4 position-relative overflow-hidden group" style={{ height: '600px' }}>
            <img src="https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=2070" className="w-100 h-100 object-cover transition-transform duration-700 group-hover:scale-110" alt="Gym" />
            {/* OVERLAY: Gradient đen từ dưới lên để chữ hiển thị rõ */}
            <div className="position-absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent opacity-80 group-hover:opacity-90 transition-opacity"></div>
            <div className="position-absolute bottom-0 start-0 p-5 w-100">
              <h3 className="font-serif-luxury text-[#d03030] fs-4 italic mb-1">01</h3>
              <h2 className="fw-bold text-uppercase mb-3 tracking-widest">Fitness & Strength</h2>
              <p className="text-gray-300 font-light mb-4 opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform translate-y-4 group-hover:translate-y-0">Hệ thống máy tập nhập khẩu 100%, phân khu rõ ràng theo nhóm cơ.</p>
            </div>
          </div>
          
          {/* Box 2: Yoga (Tương tự) */}
          <div className="col-md-4 position-relative overflow-hidden group" style={{ height: '600px' }}>
            <img src="https://images.unsplash.com/photo-1599901860904-17e6ed7083a0?q=80&w=2070" className="w-100 h-100 object-cover transition-transform duration-700 group-hover:scale-110" alt="Yoga" />
            <div className="position-absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent opacity-80 group-hover:opacity-90 transition-opacity"></div>
            <div className="position-absolute bottom-0 start-0 p-5 w-100">
              <h3 className="font-serif-luxury text-[#d03030] fs-4 italic mb-1">02</h3>
              <h2 className="fw-bold text-uppercase mb-3 tracking-widest">Yoga & Mindful</h2>
              <p className="text-gray-300 font-light mb-4 opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform translate-y-4 group-hover:translate-y-0">Studio tĩnh lặng, cân bằng thân - tâm - trí cùng Master Ấn Độ.</p>
            </div>
          </div>

          {/* Box 3: Group X */}
          <div className="col-md-4 position-relative overflow-hidden group" style={{ height: '600px' }}>
            <img src="https://images.unsplash.com/photo-1518611012118-696072aa579a?q=80&w=2070" className="w-100 h-100 object-cover transition-transform duration-700 group-hover:scale-110" alt="Group X" />
            <div className="position-absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent opacity-80 group-hover:opacity-90 transition-opacity"></div>
            <div className="position-absolute bottom-0 start-0 p-5 w-100">
              <h3 className="font-serif-luxury text-[#d03030] fs-4 italic mb-1">03</h3>
              <h2 className="fw-bold text-uppercase mb-3 tracking-widest">Group Classes</h2>
              <p className="text-gray-300 font-light mb-4 opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform translate-y-4 group-hover:translate-y-0">Hơn 50+ lớp BodyPump, Zumba, Cycling mỗi tuần với âm nhạc bùng nổ.</p>
            </div>
          </div>
        </div>
      </section>

      {/* FOOTER ĐƯỢC TỐI GIẢN & SANG TRỌNG */}
      <footer className="bg-[#0a0a0a] text-white py-5 border-t border-white/10">
        <div className="container py-5 text-center">
          <h2 className="fs-2 fw-bolder tracking-widest text-white mb-4">
            TWELVE<span className="text-[#d03030]">FIT</span>
          </h2>
          <p className="text-gray-500 mb-5 font-light">Tầng 12, Tòa nhà Bitexco, Quận 1, TP.HCM | Hotline: 1900 3434</p>
          <div className="flex justify-center gap-4">
            {/* Placeholder cho MXH */}
            <div className="w-10 h-10 rounded-full border border-gray-600 flex items-center justify-center hover:bg-white hover:text-black transition cursor-pointer">FB</div>
            <div className="w-10 h-10 rounded-full border border-gray-600 flex items-center justify-center hover:bg-white hover:text-black transition cursor-pointer">IG</div>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Homepage;