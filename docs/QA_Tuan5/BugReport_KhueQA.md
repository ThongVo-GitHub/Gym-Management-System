# BUG LOG & FIX REPORT - TUẦN 5
**Người báo cáo & Xử lý:** Trần Hoàng Anh Khuê (QA)

1. Bug: Sai lệch mapping Entity và Repository.
   - Chi tiết: Dev viết `findByFullname` sai chuẩn camelCase so với biến `fullName` trong Entity, gây lỗi Crash Server (Compile Error) khi khởi động.
   - Status: Đã bắt Dev sửa / Tự fix thành `findByFullName`.

2. Bug: Luồng thanh toán thiếu tính toàn vẹn dữ liệu (Data Integrity).
   - Chi tiết: Ghi dữ liệu Hóa đơn (Invoice) và cập nhật User không được bọc trong cùng một Transaction. Nếu lỗi giữa chừng sẽ dẫn đến sinh rác dữ liệu (trừ tiền nhưng thẻ không kích hoạt).
   - Status: Đã fix. Áp dụng `@Transactional` vào `MembershipService`.

3. Bug: Lỗi 500 Internal Server Error & Type Mismatch.
   - Chi tiết: Hardcode trạng thái bằng String, dễ sai chính tả. Truyền sai kiểu dữ liệu ID (String vào Long).
   - Status: Đã fix. Đập đi xây lại luồng POST `/buy` sang chuẩn RESTful, dùng DTO (`BuyMembershipRequest`) và Enum (`InvoiceStatus`).