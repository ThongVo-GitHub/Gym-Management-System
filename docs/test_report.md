# BÁO CÁO CHI TIẾT KẾT QUẢ KIỂM THỬ (TEST REPORT)

## 1. Môi trường kiểm thử (Test Environment)
- **Trình duyệt (UI Test):** Google Chrome (Phiên bản 124.0) và Microsoft Edge. Độ phân giải tiêu chuẩn 1920x1080.
- **Công cụ kiểm thử API (Automation Test):** Postman (v11.0) và Newman CLI.
- **Cơ sở dữ liệu:** SQL Server (sử dụng script `reset_data.sql` để clear data trước khi chạy).
- **Công cụ Unit Test (Backend):** JUnit 5, Mockito, JaCoCo.

## 2. Nhật ký chạy test (Log Run Test)
- **Ngày thực thi:** 09/05/2026
- **Người thực thi (Tester):** Trần Hoàng Anh Khuê
- **Phạm vi kiểm thử (Scope):** Thực thi toàn bộ 50 kịch bản cho 4 modules: Xác thực (Auth), Hội viên (Membership), Lớp học (Gym Class), Điểm danh (Check-in).
- **Trạng thái:** Hoàn tất 100% việc chạy test.

## 3. Danh sách lỗi (Bug Tracking / Defect Log)
Dưới đây là chi tiết các lỗi có mức độ ảnh hưởng cao được phát hiện trong lần chạy này:

### [BUG-01] Lỗ hổng Phân quyền: Admin thực hiện giao dịch mua gói tập (TC_MEM_14)
- **Mức độ (Severity):** Critical (Nghiêm trọng - Bảo mật)
- **Các bước tái hiện:** 1. Login Admin -> Gọi API `POST /api/membership/buy`.
- **Kết quả thực tế:** Trả về `201 Created` thay vì `403 Forbidden`.
- **Bằng chứng:** ![Bug Admin mua gói](<./screenshots/(8)Lỗi Admin thực hiện mua gói tập thành công trái phép (TC_MEM_14).png>)

### [BUG-02] Lỗi Phân quyền: Trainer bị chặn cập nhật lớp học (TC_CLS_06)
- **Mức độ (Severity):** High (Cao)
- **Kết quả thực tế:** Hệ thống văng lỗi `403 Forbidden` khi Trainer sửa lớp của chính mình.
- **Bằng chứng:** ![Bug Trainer bị 403](<./screenshots/(9) Lỗi Trainer không có quyền chỉnh sửa lớp(TC_CLS_06).png>)

### [BUG-03] Lỗi sập Server (500) khi xóa dữ liệu chứa Khóa ngoại
Hệ thống không xử lý ngoại lệ `DataIntegrityViolationException`, dẫn đến sập server khi xóa các bản ghi có ràng buộc:
- **Trường hợp xóa Gói tập (TC_MEM_05):** ![Bug 500 Gói tập](<./screenshots/(10) Lỗi sập Server (500) khi thực hiện xóa dữ liệu có ràng buộc khóa ngoại(TC_MEM_05).png>)
- **Trường hợp xóa Lớp học (TC_CLS_09):** ![Bug 500 Lớp học](<./screenshots/(11) Lỗi sập Server (500) khi thực hiện xóa lớp học có ràng buộc khóa ngoại(TC_CLS_09).png>)
- **Trường hợp xóa User (TC_AUTH_12):** ![Bug 500 User](<./screenshots/(12) Lỗi sập Server (500) khi thực hiện xóa User có ràng buộc khóa ngoại(TC_AUTH_12).png>)

## 4. Bằng chứng kiểm thử thủ công (Manual Test Evidences)

### 4.1. Luồng tính năng hoạt động chuẩn (Happy Path)
- Đăng nhập hệ thống thành công (TC_AUTH_01): 
![Login](<./screenshots/(1)Đăng nhập hệ thống thành công (TC_AUTH_01).png>)

- User thực hiện mua gói tập thành công (TC_MEM_06): 
![Buy Package](<./screenshots/(2)User thực hiện mua gói tập thành công - Trạng thái PENDING (TC_MEM_06).png>)

- Admin duyệt hóa đơn thành công (TC_MEM_10): 
![Confirm Invoice](<./screenshots/(3) Admin thực hiện duyệt hóa đơn thành công - Trạng thái PAID (TC_MEM_10).png>)

- Trainer tạo lớp học thành công (TC_CLS_01): 
![Create Class](<./screenshots/(4) Trainer tạo lớp học mới thành công - Trạng thái OPEN (TC_CLS_01).png>)

- Hội viên quét mã QR vào cổng thành công (TC_CHK_01): 
![Checkin](<./screenshots/(5) Hội viên thực hiện quét mã QR vào cổng thành công (TC_CHK_01).png>)

### 4.2. Kiểm tra logic nghiệp vụ phức tạp
- Bắt lỗi logic tạo lớp sai giờ (TC_CLS_03): 
![Logic Time](<./screenshots/(6) Hệ thống bắt lỗi logic khi giờ kết thúc diễn ra trước giờ bắt đầu (TC_CLS_03).png>)

- Bắt lỗi điểm danh trùng lặp (TC_CHK_07): 
![Duplicate Checkin](<./screenshots/(7) Hệ thống bắt lỗi khi hội viên cố tình điểm danh trùng lặp (TC_CHK_07).png>)

## 5. Đánh giá độ bao phủ mã nguồn (Unit Test & Code Coverage)
Bên cạnh kiểm thử hộp đen, hệ thống được đảm bảo chất lượng từ tầng lõi Backend thông qua kiểm thử mức đơn vị (Unit Test).
- **Tỷ lệ bao phủ (Coverage): 79%**
- **Đánh giá:** Hệ thống đạt tỷ lệ bao phủ 79%, vượt mức mục tiêu 70% đề ra ban đầu. Toàn bộ các tầng xử lý cốt lõi bao gồm Xác thực bảo mật (AuthService), Phân quyền (UserService), và Quản lý giao dịch được bao phủ chặt chẽ. Việc sử dụng Mockito giúp các hàm test chạy độc lập, tốc độ cao và không làm rác Database.
- **Bằng chứng Coverage:** ![Unit Test Coverage](<./screenshots/(13) Bao phu code Unit Test Backend.png>)

## 6. Kiểm thử Tự động (API Automation Test)
Để lấp đầy 21% mã nguồn chưa được cover bởi Unit Test và đóng vai trò như một lớp Integration Test, nhóm đã triển khai kiểm thử tự động API.
- **Chiến lược:** Xây dựng các kịch bản tích hợp liên hoàn trên Postman: `Đăng nhập -> Trích xuất Token vào Biến môi trường -> Mua gói -> Đặt lớp -> Điểm danh`.
- **Validation:** Sử dụng JavaScript để tự động kiểm tra Status Code (200/201), Response Time và tính toàn vẹn của JSON trả về. 
- **Đoạn script minh họa bắt JWT Token:**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has valid JWT Token", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('accessToken');
    pm.environment.set("jwt_token", jsonData.accessToken); 
});