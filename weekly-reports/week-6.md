# **Báo cáo Tuần 6**

**Tuần:** 6  (30/03/2026 \- 05/04/2026)  
**Nhóm:** Nhóm 9  
**Đề tài:** 12- Gym Management System  
**Nhóm trưởng:** Võ Minh Thông \- 2354050128  
		      Lê Dư Như Ý \- 2354050175  
		      Lê Nhật Vy \- 2354050171  
 		      Trần Hoàng Anh Khuê \- 2354050057

# **1\. Công việc đã hoàn thành**

| Thành viên | MSSV | Công việc | Link Commit/PR |  
|------------|------|-----------|----------------|  
| Võ Minh Thông | 2354050128| Hoàn thiện backend, bổ sung các api còn thiếu, hoàn thiện các API cũ và kết nối hoàn thiện với PostgreSQL  
\[link\]  
[https://github.com/ThongVo-GitHub/Gym-Management-System/commit/615ee118475bb191ee664bfee844b1cad77bfd64](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/615ee118475bb191ee664bfee844b1cad77bfd64)

| Lê Dư Như Ý | 2354050175| Kết nối BE với FE, test các api chức năng.   
\[link\] [https://github.com/ThongVo-GitHub/Gym-Management-System/commit/ab399509595ddaa8679f3ba6aa10e73fb92d8cb0](https://github.com/ThongVo-GitHub/Gym-Management-System/commit/ab399509595ddaa8679f3ba6aa10e73fb92d8cb0) 

| Lê Nhật Vy | 2354050171| Thiết kế giao diện Frontend: xây dựng giao diện quản lý admin, gói tập, lớp tập, báo cáo thu chi,   
\[link\] Q

| Trần Hoàng Anh Khuê | 2354050057 |   
    TUẦN 6:

### **1\. Nhiệm vụ đã hoàn thành (Tuần 6\)**

* **Xử lý Code & Logic:**  
  * Khắc phục lỗi khởi tạo Bean tại `UserRepository` (lỗi chính tả phương thức).  
  * Triển khai logic chặn người dùng mua trùng gói tập khi gói cũ đang hoạt động.  
  * Bổ sung Endpoint thanh toán hóa đơn (`PUT /pay/{id}`) để kích hoạt gói tập.  
* **Tài liệu Kiểm thử (QA Assets):**  
  * Hoàn thiện báo cáo lỗi chi tiết (`TEST_REPORT_WEEK6.md`) kèm ảnh minh chứng (Evidence).  
  * Thiết lập bộ Test Case (Excel/CSV) và xuất Postman Collection hỗ trợ tái hiện lỗi.  
* **Quản lý mã nguồn:** \* Đóng gói Source Code sạch (loại bỏ thư mục `target`).  
  * Nộp bài qua Pull Request (PR) trên GitHub và ghi nhận trạng thái xung đột (conflict) để xử lý.

Link PR báo cáo: https://github.com/ThongVo-GitHub/Gym-Management-System/pull/11

# **2\. Tiến độ tổng thể**

| Hạng mục | Trạng thái | % |  
|----------|------------|---|  
| Phân tích yêu cầu | Hoàn thành | 100% |  
| Thiết kế kiến trúc | Đang làm | 60% |  
| Backend API | Đang làm | 80% |  
| Frontend UI | Đang làm | 85% |  
| Docker | Chưa bắt đầu | 0% |  
| Testing | Đang làm  | 30% |

**Tổng tiến độ: 60%**

# **3\. Kế hoạch tuần tới**

| Thành viên | Công việc dự kiến |  
|------------|-------------------|  
| **Võ Minh Thông** |  Hoàn thiện backend, chỉnh sửa API, hỗ trợ frontend và phân chia công việc test 

	  
| **Lê Dư Như Ý** | Hoàn thiện kết nối API BE-FE.

| **Lê Nhật Vy** | Hoàn thiện UI và kết nối dữ liệu động từ backend, xử lý state và hiển thị dữ liệu

| **Trần Hoàng Anh Khuê** |    
**Kiểm thử Module Gym Class:** Tập trung vào kịch bản trùng lịch tập (Overlap), đặt chỗ khi lớp đã đầy (Full slot) và điều kiện đặt lịch theo trạng thái gói tập.  
**Kiểm tra Bảo mật Profile:** Thực hiện kiểm thử lỗ hổng IDOR (truy cập/sửa thông tin người dùng khác bằng Token cá nhân) và tính toàn vẹn của dữ liệu.  
**Module Admin & Package:** Kiểm tra ràng buộc dữ liệu (Validation) khi tạo gói tập mới (giá âm, tên trùng) và logic xử lý khi xóa gói tập đang có hội viên sử dụng.  
**Hỗ trợ Dev:** Phối hợp cùng đội phát triển để merge code fix vào nhánh chính và kiểm tra lại (Retest) các lỗi đã sửa.

**4\. Khó khăn / Cần hỗ trợ**   
Backend và frontend chưa tích hợp hoàn toàn → cần đồng bộ API format  
Xử lý JWT và phân quyền trên frontend còn chưa hoàn thiện  
Chưa có kinh nghiệm triển khai Docker  
Cần thêm thời gian để test đầy đủ các chức năng

**Ngày nộp:** 06/04/2026  
**Xác nhận của Nhóm trưởng:** Võ Minh Thông

