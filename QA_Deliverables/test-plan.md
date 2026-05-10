# KẾ HOẠCH KIỂM THỬ (TEST PLAN)
**Dự án:** Hệ thống Quản lý Phòng Gym (Gym Management System)

## 1. Tổng quan (Introduction)
Tài liệu này xác định phạm vi, chiến lược, môi trường và các tiêu chí đánh giá cho quy trình kiểm thử dự án Gym Management System. Mục đích nhằm đảm bảo hệ thống vận hành đúng nghiệp vụ, phân quyền chặt chẽ và không xảy ra rủi ro mất mát dữ liệu trước khi nghiệm thu và bàn giao.

## 2. Phạm vi kiểm thử (Test Scope)
### In-scope (Hạng mục nằm trong phạm vi)
Kiểm thử 4 phân hệ cốt lõi của 3 Role (User, Trainer, Admin) bao gồm:
- **Xác thực (Authentication):** Luồng đăng ký, đăng nhập và kiểm soát quyền truy cập (RBAC).
- **Hội viên (Membership):** Chức năng Admin tạo gói tập, User mua gói (CASH) và Admin duyệt hóa đơn.
- **Lớp học (Gym Class):** Chức năng Trainer tạo lớp, xóa lớp, và User đặt chỗ .
- **Điểm danh (Check-in):** Thuật toán xác thực QR Code và validate logic thời gian khi vào cổng/vào lớp.
- *Loại hình test:* Functional Testing , UI/UX Testing, và API Testing.

### Out-of-scope (Hạng mục nằm ngoài phạm vi)
- Không kiểm thử tích hợp Cổng thanh toán trực tuyến (VNPay/Momo) do hệ thống hiện tại đang sử dụng luồng thanh toán Tiền mặt (CASH) giả lập.
- Không kiểm thử bảo mật chuyên sâu (Penetration Test, SQL Injection) hay kiểm thử tải (Performance/Stress Test).

## 3. Chiến lược kiểm thử (Test Strategy)
- **Kiểm thử Thủ công (Manual Testing):** Áp dụng kỹ thuật kiểm thử Hộp đen (Black-box Testing) như phân vùng tương đương và phân tích giá trị biên để đánh giá UI và flow nghiệp vụ.
- **Kiểm thử Tự động (API Automation Testing):** Xây dựng kịch bản tự động cho 29 API cốt lõi trên Postman. Chạy tự động (Runner) bằng Newman để phục vụ Regression Test, đảm bảo API không bị gãy khi Dev cập nhật code.

## 4. Môi trường kiểm thử (Test Environment)
- **Hệ điều hành:** Windows 11 / macOS.
- **Trình duyệt (Manual):** Google Chrome (v124.0) và Microsoft Edge.
- **Môi trường Server (Automation):** Localhost (Cổng 8081).
- **Database:** Postgresql (Sử dụng script để teardown/làm sạch dữ liệu tự động trước mỗi lần chạy test).

## 5. Tiêu chí Bắt đầu & Kết thúc (Entry & Exit Criteria)
- **Entry Criteria (Tiêu chí bắt đầu):** - Đội ngũ Dev đã hoàn thiện code các tính năng in-scope và deploy lên môi trường Test.
  - **Exit Criteria (Tiêu chí kết thúc):** - 100% Test Cases (50 kịch bản) đã được thực thi và cập nhật trạng thái.
  - Không còn tồn đọng lỗi mức độ Blocker. Các lỗi Critical/High (nếu có) phải được ghi nhận rõ ràng vào file Report (Known Issues) để mang đi bảo vệ.