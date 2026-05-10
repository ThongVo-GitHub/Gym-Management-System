# HƯỚNG DẪN SỬ DỤNG HỆ THỐNG GYM MANAGEMENT
*Phiên bản: 1.0 | Phân hệ: User, Trainer, Admin*

---

## 1. Dành cho Hội viên (End User)
Đây là các bước giúp bạn bắt đầu hành trình tập luyện tại hệ thống của chúng tôi.

### 1.1. Đăng ký & Đăng nhập
- **Bước 1:** Truy cập trang chủ, nhấn vào nút **Đăng ký** ở góc phải màn hình. Điền đầy đủ Email, Username và Mật khẩu (tối thiểu 8 ký tự).
- **Bước 2:** Sau khi đăng ký, quay lại trang **Đăng nhập** và nhập tài khoản vừa tạo để vào hệ thống.

### 1.2. Mua gói tập
- **Bước 1:** Vào mục **Gói tập (Membership)** trên thanh menu để xem bảng giá.
- **Bước 2:** Nhấn nút **Mua ngay** tại gói tập bạn mong muốn.
- **Bước 3:** Chọn phương thức thanh toán là **Tiền mặt (CASH)** và nhấn Xác nhận. Hệ thống sẽ tạo một hóa đơn ở trạng thái *PENDING*. Vui lòng thanh toán tại quầy để Admin kích hoạt thẻ.

### 1.3. Đặt lịch lớp học (Booking)
- **Bước 1:** Vào mục **Lịch tập**.
- **Bước 2:** Tìm kiếm các lớp học đang ở trạng thái *OPEN* và chưa bị đầy (Full Capacity).
- **Bước 3:** Nhấn nút **Đăng ký lớp**. Hệ thống sẽ giữ chỗ cho bạn. *(Lưu ý: Bạn không thể đăng ký 2 lớp bị trùng khung giờ).*

### 1.4. Check-in điểm danh
- Khi đến phòng tập, mở mục **Thẻ của tôi**, hệ thống sẽ cung cấp một mã QR.
- Đưa mã QR này vào máy quét tại quầy lễ tân để vào cổng. *(Lưu ý: Chỉ được điểm danh tối đa 30 phút trước khi lớp học bắt đầu).*

---

## 2. Dành cho Huấn luyện viên (Trainer)
Chức năng dành cho HLV để quản lý lịch dạy của cá nhân.

### 2.1. Tạo lớp học mới
- **Bước 1:** Đăng nhập tài khoản Trainer, vào mục **Quản lý Lớp dạy**.
- **Bước 2:** Nhấn nút **Tạo lớp mới**.
- **Bước 3:** Điền các thông tin: Tên lớp, Ngày học, Giờ bắt đầu, Giờ kết thúc và Số lượng học viên tối đa (Sức chứa).
- **Bước 4:** Nhấn **Lưu**. Lớp học sẽ hiển thị cho hội viên đăng ký.

### 2.2. Hủy lớp học
- Bạn có thể xóa/hủy lớp học bằng cách nhấn nút **Xóa** ở cạnh tên lớp. 
- *Lưu ý quan trọng:* Hệ thống chỉ cho phép xóa lớp khi **chưa có bất kỳ hội viên nào đăng ký**. Nếu đã có người đăng ký, bạn không thể thao tác xóa.

---

## 3. Dành cho Quản trị viên (Admin)
Chức năng vận hành tổng thể hệ thống phòng Gym.

### 3.1. Quản lý Gói tập (Packages)
- **Bước 1:** Vào mục **Quản lý Gói tập** -> Nhấn **Thêm mới**.
- **Bước 2:** Điền Tên gói, Mức giá (bắt buộc > 0) và Thời hạn (số tháng).
- **Bước 3:** Nhấn **Lưu** để cập nhật bảng giá công khai cho toàn bộ hội viên.

### 3.2. Duyệt hóa đơn thanh toán
- **Bước 1:** Vào mục **Quản lý Giao dịch / Hóa đơn**.
- **Bước 2:** Lọc các hóa đơn đang ở trạng thái **PENDING** (Chờ thanh toán).
- **Bước 3:** Sau khi thu tiền mặt từ hội viên tại quầy, nhấn nút **Xác nhận (Confirm)**. 
- Hệ thống sẽ tự động đổi trạng thái hóa đơn sang **PAID** và kích hoạt trạng thái thẻ Active cho hội viên đó.

### 3.3. Quản lý Tài khoản User
- Admin có quyền xem danh sách người dùng và thực hiện thao tác xóa tài khoản (Deactivate/Delete) đối với các tài khoản vi phạm hoặc không còn sử dụng.