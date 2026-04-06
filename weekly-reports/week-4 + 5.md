# **Báo cáo Tuần 4 \+ 5** 

**Tuần:** 4 \+ 5  (16/03/2026 \- 29/03/2026)  
**Nhóm:** Nhóm 9  
**Đề tài:** 12- Gym Management System  
**Nhóm trưởng:** Võ Minh Thông \- 2354050128  
		      Lê Dư Như Ý \- 2354050175  
		      Lê Nhật Vy \- 2354050171  
 		      Trần Hoàng Anh Khuê \- 2354050057

# **1\. Công việc đã hoàn thành**

| Thành viên | MSSV | Công việc | Link Commit/PR |  
|------------|------|-----------|----------------|  
| Võ Minh Thông | 2354050128| Xây dựng Backend: thiết lập lại Spring Boot project, cấu hình Security (JWT, phân quyền ROLE), xây dựng API User (Admin, user) , Package, Membership , kết nối PostgreSQL  
\[link\] [https://github.com/ThongVo-GitHub/Gym-Management-System/commit/950da6d](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/950da6d) 

| Lê Dư Như Ý | 2354050175| Thiết kế giao diện Frontend: xây dựng layout tổng thể, các trang đăng nhập/đăng ký, dashboard, giao diện cho user   
\[link\] [https://github.com/ThongVo-GitHub/Gym-Management-System/commit/e7b2600780e3fce2c2f2652fd879219e53a34bd6](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/e7b2600780e3fce2c2f2652fd879219e53a34bd6)

| Lê Nhật Vy | 2354050171| Thiết kế giao diện Frontend: xây dựng giao diện quản lý admin, gói tập, lớp tập, báo cáo thu chi,   
\[link\]  
[https://github.com/ThongVo-GitHub/Gym-Management-System/tree/75d4745fa3504cc87e04bd8809c5eb25e15fc8aa](https://github.com/ThongVo-GitHub/Gym-Management-System/tree/75d4745fa3504cc87e04bd8809c5eb25e15fc8aa) 

| Trần Hoàng Anh Khuê | 2354050057 |   
    TUẦN 4:

* Kiểm thử tĩnh (Review code): Đánh giá kiến trúc Backend, đề xuất chuẩn hóa cấu trúc Entity và REST API.  
* Thiết kế kịch bản: Lên Test Cases trên Excel và dựng sẵn Postman Collection cho các API cốt lõi.  
* Trạng thái: Chờ BE hoàn thiện API để tiến hành kiểm thử động.

TUẦN 5:

* Kiểm thử động (API Testing): Chạy thực tế Postman luồng Core (Register, Login, Buy Membership) qua xác thực JWT, verify Status 200 OK.  
* Quản lý Lỗi (Bug Tracking): Lập report 3 lỗi nghiêm trọng (Crash do sai mapping Entity, thiếu Transactional, lỗi 500 Type Mismatch).  
* Tiêu chuẩn mã nguồn: Phối hợp BE áp dụng chuẩn code mới (DTO, Enum, bọc Transactional) để chống rác dữ liệu DB.  
* Bàn giao: Push thư mục nghiệm thu QA\_Reports (Postman Collection, Evidence, Bug Report) lên Git.

Link PR báo cáo: [https://github.com/ThongVo-GitHub/Gym-Management-System/pull/9](https://github.com/ThongVo-GitHub/Gym-Management-System/pull/9)

# **2\. Tiến độ tổng thể**

| Hạng mục | Trạng thái | % |  
|----------|------------|---|  
| Phân tích yêu cầu | Hoàn thành | 100% |  
| Thiết kế kiến trúc | Đang làm | 60% |  
| Backend API | Đang làm | 70% |  
| Frontend UI | Đang làm | 80% |  
| Docker | Chưa bắt đầu | 0% |  
| Testing | Đang làm  | 20% |

**Tổng tiến độ: 50%**

# **3\. Kế hoạch tuần tới**

| Thành viên | Công việc dự kiến |  
|------------|-------------------|  
| **Võ Minh Thông** |  Hoàn thiện backend, bổ sung API còn thiếu, tích hợp Swagger, hỗ trợ frontend kết nối API.  

	  
| **Lê Dư Như Ý** | Tích hợp API vào frontend (login, register, user).

| **Lê Nhật Vy** | Hoàn thiện UI và kết nối dữ liệu động từ backend, xử lý state và hiển thị dữ liệu

| **Trần Hoàng Anh Khuê** |    
\-Kiểm thử hồi quy (Regression Test): Nghiệm thu luồng Mua gói (Membership) trên nhánh main, đảm bảo BE đã fix triệt để lỗi Crash Server và thiếu Transactional.  
\-Kiểm thử ngoại lệ (Negative Test): Mở rộng kịch bản Postman, bẫy lỗi các case rủi ro (token hết hạn, mua trùng gói, gửi sai định dạng data).  
\-Chuẩn bị Test Case mới: Lên kịch bản kiểm thử API cho các module tiếp theo (Quản lý lịch tập, Cập nhật hồ sơ).  
\-Giám sát chuẩn Code: Tiếp tục review code sớm, ép BE duy trì đúng kiến trúc DTO, Enum và Transactional cho các API mới.

**4\. Khó khăn / Cần hỗ trợ**   
Backend và frontend chưa tích hợp hoàn toàn → cần đồng bộ API format  
Xử lý JWT và phân quyền trên frontend còn chưa hoàn thiện  
Chưa có kinh nghiệm triển khai Docker  
Cần thêm thời gian để test đầy đủ các chức năng

**Ngày nộp:** 29/03/2026  
**Xác nhận của Nhóm trưởng:** Võ Minh Thông

