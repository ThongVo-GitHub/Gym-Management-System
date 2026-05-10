-- =========================================================================
-- FILE: reset_data.sql
-- MỤC ĐÍCH: Làm sạch CSDL và tạo dữ liệu mồi (Mock Data) cho kiểm thử
-- MẬT KHẨU CHUNG CHO CÁC TÀI KHOẢN: 12345678
-- =========================================================================

-- 1. XÓA DỮ LIỆU CŨ VÀ RESET ID TỰ TĂNG (CASCADE để xóa các bảng có khóa ngoại)
TRUNCATE TABLE check_ins, class_bookings, invoices, gym_classes, packages, users RESTART IDENTITY CASCADE;

-- 2. THÊM TÀI KHOẢN (USERS)
-- Mật khẩu "12345678" mã hóa BCrypt: $2a$10$X16M7tYnXZg.d.uM3q.44.bB0V2XN65nQ/08/j81N6W5UoN.w9rIq
INSERT INTO users (username, email, password_hash, role, full_name, status, group_sessions, pt_sessions, specialization, bio, created_at, updated_at) VALUES 
-- [ID: 1] Admin
('admin', 'admin@gymsystem.com', '$2a$10$X16M7tYnXZg.d.uM3q.44.bB0V2XN65nQ/08/j81N6W5UoN.w9rIq', 'ADMIN', 'Quản Trị Viên', 'ACTIVE', 0, 0, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- [ID: 2 & 3] Trainers
('trainer_yoga', 'yoga@gymsystem.com', '$2a$10$X16M7tYnXZg.d.uM3q.44.bB0V2XN65nQ/08/j81N6W5UoN.w9rIq', 'TRAINER', 'Nguyễn HLV Yoga', 'ACTIVE', 0, 0, 'Yoga Master', '10 năm kinh nghiệm Hatha Yoga', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('trainer_gym', 'gym@gymsystem.com', '$2a$10$X16M7tYnXZg.d.uM3q.44.bB0V2XN65nQ/08/j81N6W5UoN.w9rIq', 'TRAINER', 'Trần HLV Thể Hình', 'ACTIVE', 0, 0, 'Bodybuilding & CrossFit', 'Vô địch thể hình 2023', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- [ID: 4, 5 & 6] Users (Hội viên)
('user_active', 'active@gmail.com', '$2a$10$X16M7tYnXZg.d.uM3q.44.bB0V2XN65nQ/08/j81N6W5UoN.w9rIq', 'USER', 'Lê Hội Viên (Có Gói)', 'ACTIVE', 10, 5, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user_pending', 'pending@gmail.com', '$2a$10$X16M7tYnXZg.d.uM3q.44.bB0V2XN65nQ/08/j81N6W5UoN.w9rIq', 'USER', 'Phạm Hội Viên (Chờ Duyệt)', 'ACTIVE', 0, 0, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user_expired', 'expired@gmail.com', '$2a$10$X16M7tYnXZg.d.uM3q.44.bB0V2XN65nQ/08/j81N6W5UoN.w9rIq', 'USER', 'Hoàng Hội Viên (Hết Hạn)', 'ACTIVE', 0, 0, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- 3. THÊM GÓI TẬP (PACKAGES)
INSERT INTO packages (package_name, price, duration_months, description, status) VALUES 
('Gói Trải Nghiệm 1 Tháng', 500000.00, 1, 'Trải nghiệm toàn bộ cơ sở vật chất trong 1 tháng.', 'ACTIVE'),
('Gói Vàng 6 Tháng', 2500000.00, 6, 'Tặng kèm 2 buổi PT miễn phí.', 'ACTIVE'),
('Gói Kim Cương 1 Năm', 4500000.00, 12, 'Tặng kèm 5 buổi PT và khăn tập miễn phí cả năm.', 'ACTIVE'),
('Gói Sinh Viên', 300000.00, 1, 'Dành riêng cho sinh viên (Yêu cầu thẻ SV).', 'INACTIVE');


-- 4. THÊM HÓA ĐƠN ĐĂNG KÝ GÓI (INVOICES)
INSERT INTO invoices (user_id, package_id, payment_method, payment_date, expired_date, status, txn_ref, version, created_at, updated_at) VALUES 
-- Hóa đơn hợp lệ, đang active (Dành cho user 4)
(4, 2, 'VNPAY', CURRENT_DATE - INTERVAL '10 days', CURRENT_DATE + INTERVAL '170 days', 'PAID', 'TXN001VNP', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Hóa đơn đang chờ Admin duyệt (Dành cho user 5)
(5, 1, 'CASH', CURRENT_DATE, CURRENT_DATE, 'PENDING', 'TXN002CASH', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Hóa đơn đã hết hạn (Dành cho user 6)
(6, 1, 'CASH', CURRENT_DATE - INTERVAL '40 days', CURRENT_DATE - INTERVAL '10 days', 'PAID', 'TXN003CASH', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- 5. THÊM LỚP HỌC (GYM_CLASSES)
INSERT INTO gym_classes (name, schedule, class_date, start_time, end_time, studio, max_capacity, current_capacity, trainer_id, status, created_at, updated_at) VALUES 
-- Lớp mở bình thường, đang có 1 người đăng ký
('Yoga Buổi Sáng', 'Thứ 2 - 4 - 6', CURRENT_DATE + INTERVAL '1 day', '06:00:00', '07:30:00', 'Studio A', 20, 1, 2, 'OPEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Lớp sắp diễn ra hôm nay (Để test điểm danh)
('Zumba Dance', 'Thứ 3 - 5 - 7', CURRENT_DATE, '18:00:00', '19:00:00', 'Studio B', 30, 0, 2, 'OPEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Lớp đã đầy (Max = 2, Curent = 2) -> Để test báo lỗi không cho đăng ký thêm
('HIIT Đốt Mỡ Siêu Tốc', 'Thứ 7 - CN', CURRENT_DATE + INTERVAL '2 days', '17:00:00', '18:30:00', 'Phòng Tạ Tầng 2', 2, 2, 3, 'FULL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- 6. THÊM LỊCH SỬ ĐẶT CHỖ (CLASS_BOOKINGS)
INSERT INTO class_bookings (user_id, gym_class_id, booked_at) VALUES 
-- User 4 đặt lớp Yoga (Class 1)
(4, 1, CURRENT_TIMESTAMP),

-- 2 User đặt kín lớp HIIT (Class 3)
(4, 3, CURRENT_TIMESTAMP),
(5, 3, CURRENT_TIMESTAMP);


-- 7. THÊM LỊCH SỬ ĐIỂM DANH (CHECK_INS)
INSERT INTO check_ins (user_id, gym_class_id, type, method, check_in_time) VALUES 
-- Điểm danh vào cổng phòng Gym chung bằng QR
(4, NULL, 'GYM_ENTRY', 'QR_CODE', CURRENT_TIMESTAMP - INTERVAL '2 hours'),

-- Điểm danh vào lớp Yoga hôm qua bằng nhân viên quẹt
(4, 1, 'CLASS_SESSION', 'STAFF_CONFIRM', CURRENT_TIMESTAMP - INTERVAL '1 day');