# **Báo cáo Tuần 9 \+ 10** 

**Tuần:** 2 (20/04/2026 \- 03/05/2026)  
**Nhóm:** Nhóm 9  
**Đề tài:** 12- Gym Management System  
**Nhóm trưởng:** Võ Minh Thông \- 2354050128  
		      Lê Dư Như Ý \- 2354050175  
		      Lê Nhật Vy \- 2354050171  
 		      Trần Hoàng Anh Khuê \- 2354050057

# **1\. Công việc đã hoàn thành**

| Thành viên | MSSV | Công việc | Link Commit/PR |   
|------------|------|-----------|----------------|  
| Võ Minh Thông | 2354050128| |   
**Kiến trúc & Database:** Hoàn tất 100% thiết kế kiến trúc hệ thống và cấu trúc cơ sở dữ liệu (ERD).  
**Core RESTful API:** Hoàn thiện 98% các endpoint cốt lõi cho hệ thống (Quản lý Hội viên, Lớp học, Booking, Thanh toán, Điểm danh).  
**Bảo mật & Nghiệp vụ:** Tích hợp thành công cơ chế xác thực bảo mật JWT, phân quyền Role-based và xử lý an toàn các luồng dữ liệu giao dịch.  
**Kiểm thử (Testing):** Đã đạt 60% tiến độ viết Test Case (Unit Test/Integration Test) cho các API quan trọng.

\[link\]   
([https://github.com/ThongVo-GitHub/Gym-Management-System/commit/39e0f1e32177c177a0465d5e629b10acbf9d6227](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/39e0f1e32177c177a0465d5e629b10acbf9d6227) ) 

| Lê Dư Như Ý | 2354050175|  
**UI/UX & giao diện:** Hoàn thiện 85% cho giao diện người dùng  
**Kiểm thử (Testing):** Hiện tại đã pass được một vài unit tests cơ bản  
\[link\] ( [github.com/thongvo-github/gym-management-system/commit/32ae23d486b28516d965c445daccd7133ed89422](http://github.com/thongvo-github/gym-management-system/commit/32ae23d486b28516d965c445daccd7133ed89422) )   
| Lê Nhật Vy | 2354050171|    
**UI/UX & Giao diện:** Hoàn thiện 90% giao diện người dùng theo thiết kế, tối ưu bố cục và đảm bảo tính đồng nhất giữa các màn hình. Tập trung cải thiện trải nghiệm người dùng (UX) với các tương tác mượt mà, trực quan hơn.  
**Tối ưu trải nghiệm người dùng:** Điều chỉnh animation, luồng điều hướng và phản hồi UI (loading, error, empty state) nhằm mang lại cảm giác sử dụng liền mạch và dễ chịu hơn.  
**Tích hợp & Hoàn thiện chức năng:** Kết nối ổn định khoảng 40% với các API backend, đảm bảo các chức năng chính như quản lý hội viên, đặt lịch, thanh toán và điểm danh hoạt động đúng luồng nghiệp vụ.  
**Kiểm thử (Testing):** Phối hợp cùng tester để thực hiện kiểm thử tổng thể (end-to-end), phát hiện và xử lý lỗi giao diện, đảm bảo tính ổn định.

\[link\] ([https://github.com/ThongVo-GitHub/Gym-Management-System/tree/fd6c9e50f0cbed97bb59f866fce85df158a26ba9/unitests/feadmin-unittest/member-tests](https://github.com/ThongVo-GitHub/Gym-Management-System/tree/fd6c9e50f0cbed97bb59f866fce85df158a26ba9/unitests/feadmin-unittest/member-tests) ) |

|  Trần Hoàng Anh Khuê | 2354050057| | 

**TUẦN 9:**

* **Kiểm thử tự động (API Automation):** Xây dựng thành công Postman Test Scripts cho Module 3 (Quản lý Lớp học). Tự động hóa việc luân chuyển Token (Admin/Trainer/User) và bóc tách ID động lưu vào Environment Variables (Chaining Requests).  
* **Kiểm thử Logic Nghiệp vụ (Business Rule Testing):** Thiết kế kịch bản bẫy lỗi các luồng tranh chấp dữ liệu phức tạp: Đặt/hủy lớp, cố tình sửa sức chứa nhỏ hơn số người đang có, ép trùng lịch (Overlap Schedule) của cả Trainer lẫn User (Hệ thống pass xuất sắc, chặn đúng lỗi 400).  
* **Quản lý Lỗi (Critical Bug Tracking):** Phát hiện lỗi **500 Internal Server Error** chí mạng khi Xóa lớp đang có học viên. (Nguyên nhân: Backend quên handle DataIntegrityViolationException do dính khóa ngoại `class_bookings` ở DB). Đã report và yêu cầu BE bắt Exception trả về 400\.

**TUẦN 10:**

* **Kiểm thử Thuật toán (Algorithm Testing):** Đánh giá chuyên sâu Module 4 (Check-in). Gài bẫy thành công thuật toán xác thực thời gian: Chặn đứng các case điểm danh quá sớm (trước 30 phút) và quá trễ (sau 15 phút), verify HTTP 400 chuẩn xác.  
* **Kiểm thử Data Validation:** Xác nhận hệ thống có lớp phòng thủ dữ liệu đầu vào cực tốt. Các request truyền sai Enum (VD: method Check-in rác) bị Jackson phân giải chặn lại ngay lập tức, không cho lọt xuống DB.  
* **Đóng gói Test Suite:** Hoàn thiện toàn bộ Postman Collection cho 4 Module cốt lõi. Tích hợp Assertion Script 100% để sẵn sàng chạy kiểm thử hàng loạt (Collection Runner).

\[link\]  
([https://github.com/ThongVo-GitHub/Gym-Management-System/commit/ae395b595c92435f90dc6dbf70f017a65b57fa02](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/ae395b595c92435f90dc6dbf70f017a65b57fa02) )

# **2\. Tiến độ tổng thê**

| Hạng mục | Trạng thái | % |  
|----------|------------|---|  
| Phân tích yêu cầu | Hoàn thành | 100% |  
| Thiết kế kiến trúc | Đang làm | 100% |  
| Backend API | Đang làm | 98% |  
| Frontend UI | Đang làm | 90% |  
| Docker | Chưa bắt đầu | 0% |  
| Testing | Đang làm  | 60% |

**Tổng tiến độ: 80%**

# **3\. Kế hoạch tuần tới**

| Thành viên | Công việc dự kiến |  
|------------|-------------------|  
| **Võ Minh Thông** |   
Tối ưu hiệu năng Database, Fix bug API và Deploy server   
Tiến hành test manual và automaition  
	  
| **Lê Dư Như Ý** |  
Viết thêm kịch bản kiểm thử cho giao diện người dùng, tìm ra những rủi ro tiềm ẩn từng trang giao diện, từ đó cải thiện và kiểm soát hoàn toàn quy trình hoạt động của các luồng xử lý và thao tác người dùng.  
Hoàn thiện giao diện người dùng ở mức ổn định nhất về cả mặt vận hành lẫn thẩm mỹ.

| **Lê Nhật Vy** |   
Hoàn thiện  giao diện người dùng theo thiết kế, tối ưu bố cục và đảm bảo tính đồng nhất giữa các màn hình. Tập trung cải thiện trải nghiệm người dùng (UX) với các tương tác mượt mà, trực quan hơn. Kết nối ổn định với các API backend, đảm bảo các chức năng chính như quản lý hội viên, đặt lịch, thanh toán và điểm danh hoạt động đúng luồng nghiệp vụ. Tiếp tục phối hợp cùng tester để thực hiện kiểm thử tổng thể (end-to-end), phát hiện và xử lý lỗi giao diện, đảm bảo tính ổn định.

**|Trần Hoàng Anh Khuê** | 

**KẾ HOẠCH TUẦN TỚI (TUẦN NỘP BÀI):**

* **Kiểm thử Hồi quy Tổng lực (Final Regression Test):** Run tự động toàn bộ Postman Collection. Nghiệm thu lần cuối các Bug BE đã fix trong Tuần 9 (đặc biệt là bắt Exception lỗi xóa Lớp).  
* **Dọn dẹp Data Test:** Phối hợp BE dọn rác dữ liệu trên DB để làm sạch luồng test, giải phóng các khung giờ bị kẹt lịch trước khi nộp bài.  
* **Bàn giao & Đóng gói (Handover):** Trích xuất Test Report cuối cùng. Export trọn bộ Postman Collection & Environment Variables (đã xóa token nhạy cảm) đẩy lên Git nhánh master để Giảng viên/BE có thể pull về run 1-click.

# **4\. Khó khăn / Cần hỗ trợ** 

**Ngày nộp:** 03/05/2026  
**Xác nhận của Nhóm trưởng:** Võ Minh Thông  
