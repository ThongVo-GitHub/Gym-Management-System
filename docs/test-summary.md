# BÁO CÁO TỔNG KẾT KIỂM THỬ
*Báo cáo cung cấp bức tranh toàn cảnh về chất lượng hệ thống Gym Management System trước khi nghiệm thu.*

## 1. Tổng quan số liệu kiểm thử 
Dựa trên tập Kịch bản kiểm thử (Test Cases) đã thiết kế và thực thi:
- **Tổng số lượng Test Case:** 50
- **Số case Đạt (Pass):** 45
- **Số case Lỗi (Fail):** 5
- **Số case Chưa chạy (Not Run):** 0
- **Tỷ lệ Pass (Pass Rate):** 90.0%

## 2. Tình trạng Unit Test (Team Dev)
*Theo báo cáo tổng hợp từ bộ phận Developer:*
- Các logic tính toán tiền gói tập và thuật toán validate thời gian Check-in đã được cover bởi Unit Test.
- **Coverage Report:** Đạt **~78%** (Vượt mức quy định tối thiểu > 70% của dự án).

## 3. Thống kê Lỗi 
Trong giai đoạn kiểm thử cuối cùng (Regression Test), tình trạng lỗi được ghi nhận như sau:
- **Tổng số lỗi (Bugs) tồn đọng:** 3 cụm lỗi chính (Tương ứng với 5 Failed Test Cases).
- **Lỗi đã Fix:** Các lỗi về UI/UX và logic giờ giấc cơ bản (như giờ kết thúc trước giờ bắt đầu) đã được Dev xử lý xong.
- **Known Issues (Lỗi mang đi bảo vệ):** - 1 lỗi Critical: Khuyết thiếu Role-based Access Control (Admin mua gói).
  - 2 lỗi High: Trainer không thể sửa lớp (403) và lỗi Unhandled Exception khi xóa dữ liệu (500).

## 4. Đánh giá tình trạng hệ thống & Rủi ro khi Demo 
**Trạng thái chung:** Các luồng nghiệp vụ cốt lõi (End User -> Business User -> Admin) cơ bản đã thông suốt. Hội viên có thể đăng nhập, đặt lớp và check-in bình thường. Quản trị viên duyệt hóa đơn mượt mà.

**Đánh giá Rủi ro khi Demo trực tiếp trước Hội đồng:**
- **Mức độ rủi ro:** Trung bình (Medium).
- **Lưu ý kịch bản Demo:** - Hệ thống rất dễ bị "bắt bài" nếu Giảng viên yêu cầu kịch bản: *"Hãy dùng tài khoản Trainer để vào chỉnh sửa sức chứa của lớp học"*. (Sẽ văng lỗi 403).
  - Không thực hiện thao tác Xóa (Delete) các dữ liệu đã phát sinh giao dịch (như xóa lớp đã có người đăng ký) trong lúc demo để tránh việc UI bị treo do API trả về lỗi 500.
- **Khuyến nghị:** Team Dev cần ưu tiên fix gấp cấu hình Security (Phân quyền API) trước giờ ra hội đồng để đảm bảo kịch bản thao tác của Role Trainer diễn ra trơn tru nhất.