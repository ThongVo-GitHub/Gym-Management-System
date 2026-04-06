# 10. Đặc tả Use Case chi tiết (MVP)

Phần này đặc tả chi tiết các luồng nghiệp vụ cốt lõi của hệ thống, bao phủ đầy đủ 3 nhóm người dùng: Member, Gym Manager và Admin.

---

## PHÂN HỆ 1: END USER (MEMBER)

### UC-01: Đăng ký và Đăng nhập tài khoản (Authentication)
**Actor:** Member (Hội viên)

**Luồng chính (Happy Path):**
- Người dùng chưa có tài khoản chọn "Đăng ký".
- Điền các thông tin cá nhân bắt buộc (Họ tên, Email, Mật khẩu, SĐT).
- Hệ thống kiểm tra hợp lệ (Validation) và tạo tài khoản mới.
- Người dùng sử dụng Email và Mật khẩu để "Đăng nhập" vào hệ thống.

**Luồng ngoại lệ (Exception Flows):**
- **Email đã tồn tại:** Hệ thống báo lỗi "Email đã được sử dụng" và yêu cầu nhập email khác hoặc đăng nhập.
- **Bỏ trống trường dữ liệu:** Hệ thống chặn submit và báo đỏ các trường bắt buộc.

### UC-02: Đăng ký và Thanh toán gói tập (Membership Purchase)
**Actor:** Member (Hội viên)

**Luồng chính (Happy Path - Thanh toán online):**
- Hội viên xem danh sách các gói tập đang mở bán.
- Chọn gói tập mong muốn và tiến hành thanh toán qua cổng VNPay.
- VNPay trả về kết quả thành công.
- Hệ thống tạo hóa đơn, chuyển trạng thái gói tập của hội viên thành Active.

**Luồng thay thế (Alternative Flow - Thanh toán thủ công):**
- Hội viên chọn phương thức "Chuyển khoản ngân hàng / Tiền mặt". Hệ thống tạo hóa đơn với trạng thái Pending. Hội viên cần chờ Gym Manager xác nhận (UC-07) để gói tập chuyển sang Active.

**Luồng ngoại lệ (Exception Flows):**
- **Thanh toán VNPay thất bại/Hủy giao dịch:** Hệ thống ghi nhận trạng thái hóa đơn là Failed hoặc Canceled, gói tập không được kích hoạt.

### UC-03: Đặt lớp tập (Class Booking)
**Actor:** Member (Hội viên)

**Luồng chính (Happy Path):**
- Hội viên xem danh sách lớp học và PT có lịch trống.
- Chọn một lớp học phù hợp và gửi yêu cầu đặt chỗ.
- Hệ thống tạo booking request với trạng thái Pending.

**Luồng ngoại lệ (Alternative/Exception Flows):**
- **Lớp đã đầy:** Hệ thống hiển thị thông báo "Lớp học đã đủ số lượng", ẩn nút đặt chỗ.
- **Gói tập hết hạn:** Hệ thống chặn hành động và yêu cầu hội viên gia hạn gói tập.
- **Trùng lịch:** Hệ thống phát hiện hội viên đã đặt lớp này rồi, hiển thị báo lỗi "Bạn đã đặt chỗ cho lớp này".

### UC-04: Check-in bằng mã QR (QR Check-in)
**Actor:** Member (Hội viên), Gym Manager (hoặc Thiết bị quét tại quầy)

**Luồng chính (Happy Path):**
- Hội viên mở ứng dụng, hệ thống tạo và hiển thị mã QR định danh cá nhân của hội viên đó.
- Hội viên đưa mã QR này cho Gym Manager quét (hoặc đưa vào máy quét tại quầy lễ tân).
- Hệ thống giải mã QR, kiểm tra trạng thái gói tập đang Active.
- Hệ thống ghi nhận lịch sử thời gian check-in và hiển thị thông báo "Check-in thành công".

**Luồng ngoại lệ (Exception Flows):**
- **Mã QR hết hạn/sai:** Hệ thống báo "Mã QR không hợp lệ", từ chối check-in.
- **Gói tập không hợp lệ:** Hội viên không có gói tập Active (chưa đóng tiền hoặc đã hết hạn), hệ thống từ chối check-in và yêu cầu gia hạn.

---

## PHÂN HỆ 2: BUSINESS USER (GYM MANAGER)

### UC-05: Quản lý lớp tập và Duyệt Booking
**Actor:** Gym Manager

**Luồng chính (Happy Path):**
- Manager tạo lớp học mới, phân công PT, thiết lập thời gian và số lượng tối đa.
- Manager xem danh sách các yêu cầu đặt chỗ (Pending) của hội viên.
- Manager chọn "Xác nhận", trạng thái booking đổi thành Approved.

**Luồng thay thế/Ngoại lệ (Alternative/Exception Flows):**
- **Từ chối duyệt:** Manager chọn "Từ chối" (do PT kẹt lịch đột xuất), trạng thái đổi thành Rejected và hệ thống tự động hoàn lại lượt đặt cho hội viên.
- **Sửa/Xóa lớp học đã có người đặt:** Nếu Manager xóa hoặc đổi giờ một lớp học đã có booking Approved, hệ thống phải cảnh báo và tự động chuyển các booking đó sang Canceled, đồng thời thông báo cho hội viên.

### UC-06: Quản lý Hội viên (Member Management)
**Actor:** Gym Manager

**Luồng chính (Happy Path):**
- Manager truy cập danh sách toàn bộ hội viên của chi nhánh.
- Manager xem chi tiết thông tin, lịch sử mua gói tập, và lịch sử check-in của một hội viên cụ thể.

**Luồng thay thế (Alternative Flow):**
- **Khóa tài khoản:** Manager phát hiện hội viên vi phạm quy định, tiến hành đổi trạng thái tài khoản của hội viên sang Inactive/Locked. Hội viên này sẽ không thể đăng nhập hoặc check-in được nữa.

### UC-07: Duyệt thanh toán thủ công (Manual Payment Approval)
**Actor:** Gym Manager

**Luồng chính (Happy Path):**
- Manager kiểm tra danh sách các gói tập đang ở trạng thái Pending (do hội viên chọn thanh toán chuyển khoản/tiền mặt).
- Sau khi đối soát biến động số dư ngân hàng hoặc nhận tiền mặt tại quầy, Manager nhấn "Xác nhận đã thanh toán".
- Hệ thống đổi trạng thái hóa đơn thành Paid và kích hoạt gói tập (Active) cho hội viên.

---

## PHÂN HỆ 3: ADMIN (QUẢN TRỊ HỆ THỐNG)

### UC-08: Quản lý Chi nhánh (Branch Management)
**Actor:** Admin

**Luồng chính (Happy Path):**
- Admin thêm mới một chi nhánh phòng Gym (Tên, Địa chỉ, Hotline).
- Admin cập nhật thông tin chi nhánh khi có sự thay đổi.

**Luồng ngoại lệ (Exception Flow):**
- **Xóa chi nhánh đang hoạt động:** Nếu Admin cố gắng xóa một chi nhánh đang có Gym Manager quản lý và có lịch sử hội viên, hệ thống sẽ báo lỗi và ngăn chặn hành động xóa để bảo toàn toàn vẹn dữ liệu (Chỉ cho phép vô hiệu hóa - Deactivate).

### UC-09: Quản lý Gói tập và Bảng giá (Pricing Management)
**Actor:** Admin

**Luồng chính (Happy Path):**
- Admin tạo một danh mục gói tập mới (VD: "Gói VIP 6 Tháng").
- Admin cấu hình giá tiền, thời hạn sử dụng và mô tả đặc quyền của gói.
- Lưu lại và hiển thị (Publish) để hội viên có thể nhìn thấy và mua.

**Luồng thay thế (Alternative Flow):**
- **Cập nhật giá:** Admin thay đổi giá tiền của gói tập hiện tại. Việc đổi giá này chỉ áp dụng cho các lượt mua mới trong tương lai, không làm thay đổi lịch sử giá của các hóa đơn cũ mà hội viên đã thanh toán.

### UC-10: Quản lý Tài khoản Nhân viên và HLV (Staff/Trainer Account Management)
**Actor:** Admin

**Luồng chính (Happy Path):**
- Admin tạo tài khoản mới cấp cho Gym Manager (Quản lý chi nhánh) hoặc PT (Huấn luyện viên).
- Admin phân quyền (Assign Role) và gắn nhân viên đó vào làm việc tại một Chi nhánh cụ thể.
- Hệ thống lưu trữ, gửi email thông báo thông tin đăng nhập cấp ban đầu cho nhân viên.

**Luồng thay thế (Alternative Flow):**
- **Khóa/Thu hồi quyền truy cập:** Khi Gym Manager hoặc PT nghỉ việc, Admin tiến hành đổi trạng thái tài khoản thành "Deactivated". Tài khoản này lập tức mất quyền đăng nhập vào hệ thống, nhưng các lịch sử duyệt lớp hay dạy học cũ của họ vẫn được giữ nguyên.
