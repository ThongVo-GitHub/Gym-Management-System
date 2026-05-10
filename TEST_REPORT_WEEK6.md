BÁO CÁO KIỂM THỬ \& PHỤC HỒI HỆ THỐNG - TUẦN 6

Dự án: Gym Management System



Người thực hiện: Trần Hoàng Anh Khuê



Vai trò: Senior QA / Tester



Trạng thái: Hoàn thành (Đã Fix lỗi \& Verify)



I. TỔNG QUAN (OVERVIEW)

Tuần này tập trung kiểm thử luồng Membership (Mua gói tập). Qua quá trình thực hiện Regression Test và Negative Test, đã phát hiện các lỗi nghiêm trọng về cả Logic lẫn Codebase khiến hệ thống không thể vận hành thực tế.



Ghi chú: QA đã chủ động can thiệp vào Source Code để vá lỗi (Hotfix) nhằm đảm bảo tiến độ dự án.

II. DANH SÁCH BUG NGHIÊM TRỌNG (CRITICAL BUGS)

ID,Tên Lỗi,Mức độ,Trạng thái,Mô tả

BUG-01,Server Crash do sai tên Method,Blocker,✅ Fixed,"Sai lỗi chính tả tại UserRepository, khiến Server không thể khởi tạo Bean."

BUG-02,Cho phép mua trùng gói tập,High,✅ Fixed,Hệ thống cho phép User mua gói mới khi gói cũ vẫn đang ACTIVE.

BUG-03,Thiếu API Thu ngân (Payment),High,✅ Fixed,User có thể tạo hóa đơn nhưng không có cổng để xác nhận thanh toán (Active gói).

II. CHI TIẾT CÁC BƯỚC XỬ LÝ (TECHNICAL FIXES)

1\. Fix Bug-01: UserRepository

Vấn đề: Method tìm kiếm User bị sai tên theo quy tắc của Spring Data JPA.



Giải pháp: Đã cập nhật lại đúng chuẩn findByUsername để Server có thể chạy.



2\. Fix Bug-02: Chặn mua trùng gói tập

Vấn đề: Lỗ hổng logic gây thất thoát doanh thu và sai lệch dữ liệu hội viên

Code đã sửa tại MembershipService.java

3\. Fix Bug-03: Triển khai API Thanh toán (Put Method)

Vấn đề: Luồng nghiệp vụ bị đứt đoạn sau khi tạo hóa đơn.

Giải pháp: Đã viết thêm Endpoint @PutMapping("/pay/{invoiceId}") trong MembershipController để kích hoạt gói tập ngay sau khi thanh toán thành công.

IV. KẾT QUẢ KIỂM THỬ (TEST RESULTS)

✅ Luồng chính (Happy Path)

Login: Lấy Token thành công (200 OK).



Buy Package: Tạo hóa đơn thành công (201 Created).



Payment: Gọi API thanh toán mới viết -> Gói tập kích hoạt thành công (200 OK).



Profile Check: Kiểm tra users/me -> Hiện đúng gói tập và ngày hết hạn.



✅ Luồng lỗi (Negative Path)

Thử mua trùng: Hệ thống văng lỗi 400 và thông báo chặn thành công.



Token hết hạn: Hệ thống trả về 401 Unauthorized (Đã kiểm chứng bảo mật).

