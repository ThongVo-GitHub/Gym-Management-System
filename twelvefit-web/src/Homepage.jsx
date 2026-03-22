//import React from 'react';
import { Link } from 'react-router-dom';

const Homepage = () => {
  return (
    <div className="bg-white text-dark min-vh-100 font-sans">
      
      {/* 1. NAVBAR */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-black fixed-top py-3 border-bottom border-dark shadow-lg">
        <div className="container d-flex justify-content-between align-items-center">
          
          <Link to="/" className="navbar-brand d-flex align-items-center text-decoration-none group">
            <div className="bg-[#d03030] w-[45px] h-[45px] rounded-[12px] flex items-center justify-center me-3 shadow-[0_5px_15px_rgba(208,48,48,0.25)] overflow-hidden transition-all duration-300 hover:scale-105 hover:rotate-3">
              <img src="/logogym.png" alt="TwelveFit Logo" className="w-full h-full object-cover" />
            </div>
            <span className="fs-3 fw-bolder tracking-wider text-white m-0">
              TWELVE<span className="text-danger">FIT</span>
            </span>
          </Link>
          
          <div className="d-none d-lg-flex gap-5 text-uppercase fw-semibold" style={{ fontSize: '0.85rem', letterSpacing: '1px' }}>
            <a href="#about" className="text-white text-decoration-none hover:text-danger transition">Về Chúng Tôi</a>
            <a href="#packages" className="text-white text-decoration-none hover:text-danger transition">Gói Tập</a>
            <a href="#classes" className="text-white text-decoration-none hover:text-danger transition">Dịch Vụ</a>
            <a href="#trainers" className="text-white text-decoration-none hover:text-danger transition">Huấn Luyện Viên</a>
          </div>

          <div className="d-flex gap-3">
            {/* Nút Đăng Nhập truyền tín hiệu mode: 'login' */}
            <Link to="/login" state={{ mode: 'login' }} className="btn btn-outline-light rounded-0 px-4 py-2 fw-semibold text-uppercase transition-all hover:bg-white hover:text-black" style={{ fontSize: '0.85rem', letterSpacing: '1px' }}>
              Đăng Nhập
            </Link>
            {/* Nút Đăng Ký truyền tín hiệu mode: 'register' */}
            <Link to="/login" state={{ mode: 'register' }} className="btn btn-danger rounded-0 px-4 py-2 fw-semibold text-uppercase transition-all hover:bg-[#b52a2a]" style={{ fontSize: '0.85rem', letterSpacing: '1px' }}>
              Đăng Ký
            </Link>
          </div>
        </div>
      </nav>

      {/* 2. BANNER CHÍNH */}
      <section className="vh-100 d-flex align-items-center position-relative" style={{ backgroundImage: "url('https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=2070&auto=format&fit=crop')", backgroundSize: 'cover', backgroundPosition: 'center', backgroundAttachment: 'fixed' }}>
        <div className="position-absolute top-0 start-0 w-100 h-100 bg-black opacity-50"></div>
        <div className="container position-relative z-10 text-center text-white mt-5">
          <h1 className="display-1 fw-bolder text-uppercase mb-3" style={{ letterSpacing: '2px' }}>
            Sức Mạnh <br/> <span className="text-danger">Đích Thực</span>
          </h1>
          <p className="fs-4 fw-light mb-5 max-w-2xl mx-auto" style={{ letterSpacing: '1px' }}>
            Trải nghiệm không gian tập luyện đẳng cấp 5 sao với thiết bị tối tân nhất.
          </p>
          <Link to="/login" state={{ mode: 'register' }} className="btn btn-danger btn-lg rounded-0 px-5 py-3 text-uppercase fw-bold transition-all hover:scale-105" style={{ letterSpacing: '2px' }}>
            Bắt Đầu Hành Trình
          </Link>
        </div>
      </section>

      {/* 3. BẢNG GIÁ GÓI TẬP (Vừa được thêm vào) */}
      <section id="packages" className="py-5 bg-black text-white">
        <div className="container py-5">
          <div className="text-center mb-5">
            <h2 className="display-5 fw-bold text-uppercase">Bảng Giá <span className="text-danger">Hội Viên</span></h2>
            <div className="mx-auto bg-danger mt-3" style={{ width: '60px', height: '3px' }}></div>
          </div>
          <div className="row g-4">
            {/* Gói Cơ Bản */}
            <div className="col-md-4">
              <div className="card h-100 bg-dark text-white rounded-0 border-0 border-top border-4 border-secondary transition-all duration-300 hover:-translate-y-2">
                <div className="card-body text-center p-5">
                  <h3 className="card-title fw-bold text-uppercase mb-4">Khởi Động</h3>
                  <h4 className="display-4 fw-bolder text-danger my-4">500k<span className="fs-6 text-muted fw-normal">/tháng</span></h4>
                  <p className="card-text text-gray-400 mb-4 px-3">Phù hợp cho người mới bắt đầu làm quen với thể hình cơ bản.</p>
                  <Link to="/login" state={{ mode: 'register' }} className="btn btn-outline-light w-100 fw-bold py-2 rounded-0 text-uppercase">Chọn Gói Này</Link>
                </div>
              </div>
            </div>
            {/* Gói Pro (Bán chạy) */}
            <div className="col-md-4">
              <div className="card h-100 bg-dark text-white rounded-0 border-0 border-top border-4 border-danger position-relative transition-all duration-300 hover:-translate-y-2 scale-105 shadow-lg">
                <span className="position-absolute top-0 start-50 translate-middle badge bg-danger fs-6 px-4 py-2 rounded-0 tracking-wider">BÁN CHẠY NHẤT</span>
                <div className="card-body text-center p-5 mt-3">
                  <h3 className="card-title fw-bold text-uppercase mb-4">Đam Mê</h3>
                  <h4 className="display-4 fw-bolder text-danger my-4">800k<span className="fs-6 text-muted fw-normal">/tháng</span></h4>
                  <p className="card-text text-gray-400 mb-4 px-3">Tập luyện không giới hạn thời gian. Tham gia tất cả các lớp Yoga, HIIT, Boxing.</p>
                  <Link to="/login" state={{ mode: 'register' }} className="btn btn-danger w-100 fw-bold py-3 rounded-0 text-uppercase">Đăng Ký Ngay</Link>
                </div>
              </div>
            </div>
            {/* Gói Elite */}
            <div className="col-md-4">
              <div className="card h-100 bg-dark text-white rounded-0 border-0 border-top border-4 border-secondary transition-all duration-300 hover:-translate-y-2">
                <div className="card-body text-center p-5">
                  <h3 className="card-title fw-bold text-uppercase mb-4">Tinh Anh</h3>
                  <h4 className="display-4 fw-bolder text-danger my-4">1.2M<span className="fs-6 text-muted fw-normal">/tháng</span></h4>
                  <p className="card-text text-gray-400 mb-4 px-3">Kèm 1-1 với Huấn luyện viên cá nhân chuyên nghiệp và dịch vụ khăn tắm.</p>
                  <Link to="/login" state={{ mode: 'register' }} className="btn btn-outline-light w-100 fw-bold py-2 rounded-0 text-uppercase">Chọn Gói Này</Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 4. DỊCH VỤ NỔI BẬT */}
      <section id="classes" className="py-5 bg-white">
        <div className="container-fluid px-0 py-5">
          <div className="text-center mb-5">
            <h2 className="display-5 fw-bold text-uppercase">Dịch Vụ <span className="text-danger">Nổi Bật</span></h2>
            <div className="mx-auto bg-danger mt-3" style={{ width: '60px', height: '3px' }}></div>
          </div>
          <div className="row g-0">
            <div className="col-md-4 position-relative overflow-hidden group" style={{ height: '500px' }}>
              <img src="https://images.unsplash.com/photo-1599901860904-17e6ed7083a0?q=80&w=2070&auto=format&fit=crop" className="w-100 h-100 object-fit-cover transition-transform duration-700 group-hover:scale-110" alt="Yoga" />
              <div className="position-absolute top-0 start-0 w-100 h-100 bg-black/30 group-hover:bg-black/50 transition-all duration-500"></div>
              <div className="position-absolute bottom-0 start-0 p-5 text-white w-100 bg-gradient-to-t from-black/90 to-transparent">
                <h3 className="fw-bold text-uppercase mb-2">Lớp Học Yoga</h3>
                <p className="fw-light mb-4">Cân bằng tâm trí và cơ thể.</p>
                <Link to="/login" state={{ mode: 'register' }} className="text-white text-decoration-none fw-bold text-uppercase border-bottom border-danger pb-1 hover:text-danger transition-colors">Tham Gia Ngay</Link>
              </div>
            </div>
            <div className="col-md-4 position-relative overflow-hidden group" style={{ height: '500px' }}>
              <img src="https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=2070&auto=format&fit=crop" className="w-100 h-100 object-fit-cover transition-transform duration-700 group-hover:scale-110" alt="Gym" />
              <div className="position-absolute top-0 start-0 w-100 h-100 bg-black/30 group-hover:bg-black/50 transition-all duration-500"></div>
              <div className="position-absolute bottom-0 start-0 p-5 text-white w-100 bg-gradient-to-t from-black/90 to-transparent">
                <h3 className="fw-bold text-uppercase mb-2">Thể Hình (Gym)</h3>
                <p className="fw-light mb-4">Thiết bị hiện đại chuẩn Châu Âu.</p>
                <Link to="/login" state={{ mode: 'register' }} className="text-white text-decoration-none fw-bold text-uppercase border-bottom border-danger pb-1 hover:text-danger transition-colors">Tham Gia Ngay</Link>
              </div>
            </div>
            <div className="col-md-4 position-relative overflow-hidden group" style={{ height: '500px' }}>
              <img src="https://images.unsplash.com/photo-1518611012118-696072aa579a?q=80&w=2070&auto=format&fit=crop" className="w-100 h-100 object-fit-cover transition-transform duration-700 group-hover:scale-110" alt="Group X" />
              <div className="position-absolute top-0 start-0 w-100 h-100 bg-black/30 group-hover:bg-black/50 transition-all duration-500"></div>
              <div className="position-absolute bottom-0 start-0 p-5 text-white w-100 bg-gradient-to-t from-black/90 to-transparent">
                <h3 className="fw-bold text-uppercase mb-2">Lớp Tập Nhóm</h3>
                <p className="fw-light mb-4">Bùng nổ năng lượng với âm nhạc.</p>
                <Link to="/login" state={{ mode: 'register' }} className="text-white text-decoration-none fw-bold text-uppercase border-bottom border-danger pb-1 hover:text-danger transition-colors">Tham Gia Ngay</Link>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 5. HUẤN LUYỆN VIÊN */}
      <section id="trainers" className="py-5 bg-light">
        <div className="container py-5">
          <div className="d-flex justify-content-between align-items-end mb-5">
            <div>
              <h2 className="display-6 fw-bold text-uppercase mb-2">Đội Ngũ <span className="text-danger">Chuyên Gia</span></h2>
              <p className="text-muted fs-5">Những người đồng hành dẫn dắt bạn tới thành công.</p>
            </div>
          </div>
          <div className="row g-4">
            {[
              { name: "Marcus Lee", role: "HUẤN LUYỆN VIÊN TRƯỞNG", desc: "Chuyên gia thể hình & Dinh dưỡng.", img: "1567013127542-490d757e51fc" },
              { name: "Elena Silva", role: "CHUYÊN GIA YOGA", desc: "Hơn 10 năm kinh nghiệm giảng dạy Hatha.", img: "1594381898411-846e7d193883" },
              { name: "David Tran", role: "CHUYÊN GIA GIẢM MỠ", desc: "Thiết kế chương trình độ body cấp tốc.", img: "1534438097549-3174526f8d30" },
              { name: "Sarah Nguyen", role: "GIÁO VIÊN NHẢY", desc: "Tràn đầy năng lượng với Zumba, BodyCombat.", img: "1548690312-e3b507d8c110" }
            ].map((trainer, index) => (
              <div key={index} className="col-md-3 col-sm-6 group cursor-pointer">
                <div className="card border-0 bg-transparent">
                  <div className="overflow-hidden" style={{ height: '400px' }}>
                    <img src={`https://images.unsplash.com/photo-${trainer.img}?q=80&w=1974&auto=format&fit=crop`} className="card-img-top w-100 h-100 object-fit-cover transition-all duration-700 grayscale group-hover:grayscale-0 group-hover:scale-105" alt={trainer.name} />
                  </div>
                  <div className="card-body px-0 pt-4 text-center">
                    <h4 className="card-title fw-bold text-uppercase mb-1">{trainer.name}</h4>
                    <p className="text-danger fw-semibold mb-2" style={{ letterSpacing: '1px', fontSize: '0.9rem' }}>{trainer.role}</p>
                    <p className="text-muted small px-3">{trainer.desc}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* 6. FOOTER */}
      <footer className="bg-black text-white py-5 border-top border-secondary">
        <div className="container py-4">
          <div className="row g-5">
            <div className="col-md-4">
              <h2 className="fs-3 fw-bolder tracking-wider text-white mb-4">
                TWELVE<span className="text-danger">FIT</span>
              </h2>
              <p className="text-gray-400 pe-4 leading-relaxed">Hệ thống phòng tập thể thao cao cấp. Nơi đánh thức sức mạnh tiềm ẩn và kiến tạo phiên bản tốt nhất của bạn.</p>
            </div>
            <div className="col-md-2">
              <h5 className="text-uppercase fw-bold mb-4 text-gray-200">Khám Phá</h5>
              <ul className="list-unstyled">
                <li className="mb-3"><a href="#" className="text-gray-400 text-decoration-none hover:text-white transition-colors">Về chúng tôi</a></li>
                <li className="mb-3"><a href="#" className="text-gray-400 text-decoration-none hover:text-white transition-colors">Tin tức & Sự kiện</a></li>
                <li className="mb-3"><a href="#" className="text-gray-400 text-decoration-none hover:text-white transition-colors">Tuyển dụng</a></li>
              </ul>
            </div>
            <div className="col-md-3">
              <h5 className="text-uppercase fw-bold mb-4 text-gray-200">Liên Hệ</h5>
              <ul className="list-unstyled text-gray-400">
                <li className="mb-3">📍 Tầng 12, Tòa nhà Bitexco, Q1, TP.HCM</li>
                <li className="mb-3">📞 Hotline: 1900 1234</li>
                <li className="mb-3">✉️ Email: info@twelvefit.com</li>
              </ul>
            </div>
            <div className="col-md-3">
              <h5 className="text-uppercase fw-bold mb-4 text-gray-200">Nhận Bản Tin</h5>
              <div className="input-group mb-3">
                <input type="text" className="form-control bg-dark border-secondary text-white rounded-0 px-4 py-3" placeholder="Email của bạn" />
                <button className="btn btn-danger rounded-0 px-4 fw-bold hover:bg-[#b52a2a] transition-colors" type="button">→</button>
              </div>
            </div>
          </div>
          <div className="border-top border-gray-800 mt-5 pt-4 text-center text-gray-500 small uppercase tracking-widest">
            &copy; 2026 TWELVEFIT Premium Fitness. Đã đăng ký bản quyền.
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Homepage;