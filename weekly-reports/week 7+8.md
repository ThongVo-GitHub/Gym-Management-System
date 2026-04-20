# **Báo cáo Tuần 7 \+ 8** 

**Tuần:** 6  (06/04/2026 \- 19/04/2026)  
**Nhóm:** Nhóm 9  
**Đề tài:** 12- Gym Management System  
**Nhóm trưởng:** Võ Minh Thông \- 2354050128  
		      Lê Dư Như Ý \- 2354050175  
		      Lê Nhật Vy \- 2354050171  
 		      Trần Hoàng Anh Khuê \- 2354050057

# **1\. Công việc đã hoàn thành**

| Thành viên | MSSV | Công việc | Link Commit/PR |  
|------------|------|-----------|----------------|  
| Võ Minh Thông | 2354050128|    
Hoàn thiện backend: xây dựng hệ thống Authentication & Authorization (JWT), xử lý business logic, hoàn chỉnh các API chính (membership, user, package)  
\[link\]  
[[https://github.com/ThongVo-GitHub/Gym-Management-System/commit/615ee118475bb191ee664bfee844b1cad77bfd64](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/615ee118475bb191ee664bfee844b1cad77bfd64)](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/1c092cb1fdcccec054b9bad527516d8771d16a84)

| Lê Dư Như Ý | 2354050175| Kết nối BE với FE, test các api chức năng.   
\[link\] [https://github.com/ThongVo-GitHub/Gym-Management-System/commit/ab399509595ddaa8679f3ba6aa10e73fb92d8cb0](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/ab399509595ddaa8679f3ba6aa10e73fb92d8cb0) 

| Lê Nhật Vy | 2354050171| Kết nối FE với BE \[link\]   
[https://github.com/ThongVo-GitHub/Gym-Management-System/tree/44a33b3a2fa7ab20c571353fcf512ba7d3e57a11/frontend/twelvefit-web](https://github.com/ThongVo-GitHub/Gym-Management-System/tree/44a33b3a2fa7ab20c571353fcf512ba7d3e57a11/frontend/twelvefit-web) 

| Trần Hoàng Anh Khuê | 2354050057 |   
    TUẦN 6:

### **1\. Nhiệm vụ đã hoàn thành (Tuần 7+8)**

**1\. Tổng kết Tuần 7: Hoàn thiện luồng cơ bản (Happy Path)**

* Đã đóng gói kịch bản test (Postman Collection) cho Module Gym Class.  
* Nghiệm thu thành công luồng nghiệp vụ chuẩn: Trainer tạo lớp \-\> User đặt lịch \-\> Hệ thống tự động cập nhật đúng sĩ số (Kèm ảnh minh chứng).

https://github.com/ThongVo-GitHub/Gym-Management-System/commit/17576ab5d4da91483f2be09aac4e7389e8c4e028

**2\. Tổng kết Tuần 8: Phát hiện Bug và Vá lỗ hổng hệ thống (Hotfixes)**

* **Vá lỗi Bảo mật (RBAC):** Khắc phục lỗ hổng nghiêm trọng cho phép Member tạo Gói tập; Cấu hình lại quyền cho phép Admin tạo Lớp học.  
* **Vá lỗi Nghiệp vụ (Logic):** Triệt tiêu bug "học chùa" (chặn khách chưa thanh toán đặt lớp), sửa logic đếm sĩ số để chặn overbooking, bổ sung API Hủy lịch tập.  
* **Tối ưu Hệ thống:** Cập nhật `GlobalExceptionHandler` để triệt tiêu lỗi sập Server (500), trả về đúng chuẩn HTTP (400, 403, 409). Xử lý xong lỗi font Tiếng Việt (Entity).

https://github.com/ThongVo-GitHub/Gym-Management-System/issues/16

# **2\. Tiến độ tổng thể**

| Hạng mục | Trạng thái | % |  
|----------|------------|---|  
| Phân tích yêu cầu | Hoàn thành | 100% |  
| Thiết kế kiến trúc | Đang làm | 100% |  
| Backend API | Đang làm | 90% |  
| Frontend UI | Đang làm | 90% |  
| Docker | Chưa bắt đầu | 0% |  
| Testing | Đang làm  | 40% |

**Tổng tiến độ: 75%**

# **3\. Kế hoạch tuần tới**

| Thành viên | Công việc dự kiến |  
|------------|-------------------|  
| **Võ Minh Thông** |  Tối ưu backend, bổ sung, xử lý exception, thêm các endpoint QR check-in và AI nếu có thể, chia công việc kiểm thử cho nhóm và tiến hành kiểm thử/ 

	  
| **Lê Dư Như Ý** | Hoàn thiện tích hợp API, xử lý lỗi FE-BE, tiến hành thực hiện kiểm thử chung với tester.

| **Lê Nhật Vy** | Hoàn thiện UI/UX, cải thiện trải nghiệm người dùng, tiến hành thực hiện kiểm thử chung với tester.

| **Trần Hoàng Anh Khuê** |  

**1\. Kiểm thử Xuyên suốt Nghiệp vụ (End-to-End / E2E Testing):**

* Đánh giá toàn bộ luồng sống của dữ liệu Backend: *Đăng ký/Đăng nhập \-\> Mua gói tập \-\> Xác nhận thanh toán \-\> Đặt lịch học \-\> Check-in thành công.*  
* Đảm bảo dữ liệu luân chuyển giữa các Module (Auth, Membership, GymClass, Payment) không bị tắc nghẽn hay sai lệch trạng thái.

**2\. Kiểm thử Hồi quy Toàn diện (Full Regression Testing):**

* Chạy lại 100% các Test Case   
* Đảm bảo các đoạn code tích hợp mới của nhóm Dev không làm "sống lại" những lỗ hổng đã được QA vá thành công (như lỗi học chùa, lỗi trùng lịch, văng lỗi 500).

**4\. Khó khăn / Cần hỗ trợ**   
   
Một số API cần đồng bộ format giữa frontend và backend  
Xử lý JWT và phân quyền còn phát sinh lỗi  
Testing chưa bao phủ toàn bộ hệ thống  
Chưa triển khai Docker

**Ngày nộp:** 20/04/2026  
**Xác nhận của Nhóm trưởng:** Võ Minh Thông

