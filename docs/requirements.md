<<<<<<< HEAD
# **SOFTWARE REQUIREMENT SPECIFICATION (SRS)**

## **Gym Management System**

Version: 1.0  
 Date: 2026  
 Author: QA Team

# **1\. Introduction**

## **1.1 Purpose**

Tài liệu **Software Requirement Specification (SRS)** này mô tả đầy đủ các yêu cầu chức năng và phi chức năng của hệ thống **Gym Management System**.

Tài liệu được sử dụng cho các mục đích:

* Làm cơ sở cho **thiết kế hệ thống**

* Hỗ trợ **development team**

* Là tài liệu tham chiếu cho **tester để viết test case**

* Là tài liệu thống nhất giữa **stakeholders**

## **1.2 Scope**

Gym Management System là hệ thống quản lý hoạt động của phòng gym, bao gồm:

* Quản lý hội viên

* Quản lý gói tập

* Đặt lớp học và PT

* Check-in 

* Quản lý chi nhánh

* Quản lý bảng giá

* Quản lý tài khoản người dùng

Hệ thống có 3 loại người dùng:

1. Member (Hội viên)

2. Gym Manager (Quản lý gym)

3. Admin (Quản trị hệ thống)

## **1.3 Definitions, Acronyms, Abbreviations**

| Term | Meaning |
| ----- | ----- |
| QR | Quick Response Code |
| PT | Personal Trainer |
| CRUD | Create, Read, Update, Delete |
| SRS | Software Requirement Specification |
| UI | User Interface |

## **1.4 References**

* IEEE 830 Software Requirement Specification

* Gym business operation workflow

* Capstone Project Requirements

# **2\. Overall Description**

## **2.1 Product Perspective**

Gym Management System là **web-based application**.

Người dùng truy cập qua:

* Web browser

* Mobile browser

Hệ thống gồm các module:

* User Management

* Membership Management

* Class Booking

* Check-in bằng vân tay

* Trainer Management

* Branch Management

* Pricing Management

## **2.2 Product Functions**

Hệ thống hỗ trợ các chức năng chính:

* Đăng ký gói tập

* Thanh toán gói tập

* Đặt lớp học

* Đặt PT

* Check-in bằng vân tay

* Quản lý lịch lớp

* Quản lý hội viên

* Quản lý chi nhánh

* Quản lý bảng giá

## **2.3 User Classes and Characteristics**

### **1\. Member**

Hội viên của phòng gym.

Khả năng:

* Đăng ký gói tập

* Đặt lớp học

* Đặt PT

* Check-in bằng QR

Trình độ sử dụng: cơ bản.

### **2\. Gym Manager**

Người quản lý hoạt động gym.

Khả năng:

* Quản lý lớp học

* Quản lý hội viên

* Xác nhận booking

* Theo dõi doanh thu

Trình độ: trung cấp.

### **3\. Admin**

Quản trị hệ thống.

Khả năng:

* Quản lý chi nhánh

* Quản lý tài khoản

* Quản lý bảng giá

## **2.4 Operating Environment**

Hệ thống hoạt động trên:

* Web browser (Chrome, Firefox, Edge)

* Desktop

* Mobile

Backend:

* API server

* Database server

## **2.5 Design and Implementation Constraints**

* Hệ thống phải hỗ trợ QR check-in

* Mỗi hội viên chỉ có 1 tài khoản

* Lớp học có giới hạn số lượng người

## **2.6 Assumptions and Dependencies**

* Người dùng có internet

* QR scanner hoạt động bình thường

* Payment gateway hoạt động ổn định

# **3\. System Features**

# **3.1 Membership Registration**

### **Description**

Cho phép hội viên đăng ký gói tập.

### **Functional Requirements**

FR-01  
 Hệ thống hiển thị danh sách gói tập.

FR-02  
 Hệ thống hiển thị chi tiết gói tập.

FR-03  
 Hội viên có thể chọn gói tập.

FR-04  
 Hội viên có thể thanh toán gói tập.

FR-05  
 Sau khi thanh toán thành công:

* trạng thái gói tập \= Active.

FR-06  
 Hệ thống lưu lịch sử gói tập.

# **3.2 Class Booking**

### **Description**

Cho phép hội viên đặt lớp học.

### **Functional Requirements**

FR-07  
 Hệ thống hiển thị danh sách lớp học.

FR-08  
 Hệ thống hiển thị lịch PT.

FR-09  
 Hội viên có thể đặt lớp học.

FR-10  
 Hội viên có thể đặt PT.

FR-11  
 Hệ thống gửi yêu cầu booking.

FR-12  
 Gym Manager xác nhận booking.

FR-13  
 Gym Manager từ chối booking.

# **3.3 QR Check-in**

### **Description**

Cho phép hội viên check-in khi đến phòng gym.

### **Functional Requirements**

FR-14  
 Hệ thống tạo QR code cho hội viên.

FR-15  
 Hội viên có thể quét QR code để check-in.

FR-16  
 Hệ thống ghi nhận thời gian check-in.

FR-17  
 Chỉ hội viên có gói tập **Active** mới được check-in.

FR-18  
 Hệ thống hiển thị lịch sử check-in.

# **3.4 Class Management**

### **Description**

Quản lý lớp học.

### **Functional Requirements**

FR-19  
 Gym Manager có thể tạo lớp học.

FR-20  
 Gym Manager có thể chỉnh sửa lớp học.

FR-21  
 Gym Manager có thể xóa lớp học.

FR-22  
 Gym Manager có thể phân công PT.

FR-23  
 Gym Manager có thể xem danh sách học viên.

# **3.5 Member Management**

### **Description**

Quản lý hội viên.

### **Functional Requirements**

FR-24  
 Gym Manager xem danh sách hội viên.

FR-25  
 Gym Manager xem thông tin hội viên.

FR-26  
 Gym Manager xem trạng thái gói tập.

FR-27  
 Gym Manager khóa tài khoản hội viên.

# **3.6 Branch Management**

### **Description**

Quản lý chi nhánh.

### **Functional Requirements**

FR-28  
 Admin tạo chi nhánh.

FR-29  
 Admin chỉnh sửa chi nhánh.

FR-30  
 Admin xóa chi nhánh.

FR-31  
 Admin xem danh sách chi nhánh.

# **3.7 Account Management**

### **Description**

Quản lý tài khoản.

### **Functional Requirements**

FR-32  
 Admin tạo tài khoản.

FR-33  
 Admin chỉnh sửa tài khoản.

FR-34  
 Admin khóa tài khoản.

FR-35  
 Admin phân quyền.

# **3.8 Pricing Management**

### **Description**

Quản lý gói tập.

### **Functional Requirements**

FR-36  
 Admin tạo gói tập.

FR-37  
 Admin chỉnh sửa gói tập.

FR-38  
 Admin xóa gói tập.

FR-39  
 Admin cấu hình giá.

# **4\. External Interface Requirements**

## **4.1 User Interfaces**

### **Member UI**

* Login

* Dashboard

* Membership

* Booking

* QR Check-in

### **Gym Manager UI**

* Class Management

* Member Management

* Booking Approval

### **Admin UI**

* Branch Management

* Pricing Management

* Account Management

## **4.2 Hardware Interfaces**

* Camera (QR scan)

## **4.3 Software Interfaces**

* Payment gateway

* Database system

# **5\. Non-functional Requirements**

## **5.1 Performance**

NFR-01  
 Hệ thống phải phản hồi trong **≤ 3 giây**.

## **5.2 Security**

NFR-02  
 Người dùng phải đăng nhập.

NFR-03  
 Hệ thống phân quyền theo role.

## **5.3 Availability**

NFR-04  
 Hệ thống uptime ≥ 99%.

## **5.4 Reliability**

NFR-05  
 Hệ thống phải đảm bảo dữ liệu booking chính xác.

## **5.5 Usability**

NFR-06  
 UI phải dễ sử dụng.

## **5.6 Scalability**

NFR-07  
 Hệ thống hỗ trợ tối thiểu **1000 hội viên**

# **6\. Data Model (High Level)**

Các entity chính:

* User

* Member

* Trainer

* Membership Package

* Booking

* Class

* Branch

* Payment

* Check-in

# **7\. System Workflow**

## **7.1 Overview**

System workflow mô tả luồng hoạt động chính của **Gym Management System** giữa ba loại người dùng:

* **Member (Hội viên)**

* **Gym Manager (Quản lý gym)**

* **Admin (Quản trị hệ thống)**

Quy trình bắt đầu từ việc hội viên đăng ký gói tập và đặt lớp học, sau đó quản lý gym lập lịch và xác nhận đặt lớp, cuối cùng admin quản lý thông tin chi nhánh và bảng giá.

---

## **7.2 Main Workflow**

### **Step 1 – Membership Registration**

1. Hội viên đăng ký tài khoản vào hệ thống.

2. Hội viên đăng nhập vào hệ thống.

3. Hội viên xem danh sách gói tập.

4. Hội viên chọn gói tập phù hợp.

5. Hội viên thanh toán và kích hoạt gói tập.

Kết quả:  
 Hội viên có **gói tập hợp lệ (Active Membership)**.

### **Step 2 – Class Booking**

1. Hội viên truy cập danh sách lớp học.

2. Hệ thống hiển thị lịch lớp và lịch PT còn trống.

3. Hội viên chọn lớp học hoặc PT.

4. Hội viên gửi yêu cầu đặt lớp.

Kết quả:  
 Hệ thống tạo **booking request** với trạng thái **Pending**.

### **Step 3 – Class Scheduling and Booking Confirmation**

1. Gym Manager đăng nhập vào hệ thống quản lý.

2. Gym Manager tạo hoặc cập nhật lịch lớp học.

3. Gym Manager phân công PT cho lớp học.

4. Gym Manager xem danh sách booking request.

5. Gym Manager xác nhận hoặc từ chối yêu cầu đặt lớp.

Kết quả:  
 Booking được cập nhật thành:

* **Approved** nếu chấp nhận

* **Rejected** nếu từ chối

### **Step 4 – Gym Check-in**

1. Hội viên đến phòng gym.

2. Hệ thống cung cấp mã QR cho hội viên.

3. Hội viên quét QR để check-in.

4. Hệ thống ghi nhận thời gian check-in.

Kết quả:  
 Lịch sử check-in được lưu vào hệ thống.

### **Step 5 – Branch and Pricing Management**

1. Admin đăng nhập vào hệ thống quản trị.

2. Admin quản lý thông tin chi nhánh gym (CRUD).

3. Admin quản lý bảng giá gói tập.

4. Admin cấu hình chính sách giá và các gói tập.

Kết quả:  
 Hệ thống cập nhật thông tin **chi nhánh và bảng giá**.

## **7.3 Workflow Diagram (High-Level)**

Member  
   │  
   │ Register Membership  
   │  
   ▼  
Purchase Gym Package  
   │  
   ▼  
Book Class / PT  
   │  
   ▼  
Gym Manager  
   │  
   │ Create Class Schedule  
   │ Approve / Reject Booking  
   ▼  
Member Attend Gym  
   │  
   ▼  
QR Check-in  
   │  
   ▼  
Admin  
   │  
   │ Manage Branches  
   │ Manage Pricing  
   ▼  
System Update

# **7.4 Alternative Flows**

Alternative Flow mô tả các luồng **không phải luồng chính**, nhưng vẫn có thể xảy ra trong quá trình sử dụng hệ thống.

---

## **AF-01 – Booking Rejected**

**Actor:** Gym Manager, Member

**Description:**  
 Xảy ra khi quản lý gym từ chối yêu cầu đặt lớp của hội viên.

**Flow:**

1. Hội viên gửi yêu cầu đặt lớp học.

2. Hệ thống tạo booking với trạng thái **Pending**.

3. Gym Manager xem danh sách booking.

4. Gym Manager chọn **Reject booking**.

5. Hệ thống cập nhật trạng thái booking thành **Rejected**.

6. Hệ thống thông báo cho hội viên.

**Result:**  
 Hội viên không được tham gia lớp học.

---

## **AF-02 – Class Fully Booked**

**Actor:** Member

**Description:**  
 Xảy ra khi lớp học đã đạt số lượng tối đa.

**Flow:**

1. Hội viên truy cập danh sách lớp học.

2. Hội viên chọn lớp học.

3. Hệ thống kiểm tra số lượng học viên.

4. Nếu lớp đã đầy, hệ thống hiển thị thông báo **Class is full**.

**Result:**  
 Hội viên không thể đặt lớp.

---

## **AF-03 – Expired Membership**

**Actor:** Member

**Description:**  
 Xảy ra khi hội viên cố gắng đặt lớp hoặc check-in với gói tập đã hết hạn.

**Flow:**

1. Hội viên chọn lớp học.

2. Hệ thống kiểm tra trạng thái membership.

3. Nếu membership \= **Expired**, hệ thống hiển thị thông báo yêu cầu gia hạn.

**Result:**  
 Hội viên không thể đặt lớp hoặc check-in.

---

## **AF-04 – Member Cancels Booking**

**Actor:** Member

**Description:**  
 Hội viên có thể hủy đặt lớp trước thời gian lớp bắt đầu.

**Flow:**

1. Hội viên mở danh sách booking.

2. Hội viên chọn lớp đã đặt.

3. Hội viên chọn **Cancel booking**.

4. Hệ thống xác nhận hủy.

5. Hệ thống cập nhật trạng thái booking thành **Canceled**.

**Result:**  
 Slot của lớp học được giải phóng.

# **7.5 Exception Flows**

Exception Flow mô tả các tình huống **lỗi hệ thống hoặc lỗi nghiệp vụ** có thể xảy ra.

## **EF-01 – Payment Failure**

**Actor:** Member

**Description:**  
 Xảy ra khi thanh toán gói tập thất bại.

**Flow:**

1. Hội viên chọn gói tập.

2. Hội viên tiến hành thanh toán.

3. Payment gateway trả về trạng thái **Failed**.

4. Hệ thống hiển thị thông báo thanh toán thất bại.

**System Response:**

* Membership không được kích hoạt.

* Người dùng có thể thử thanh toán lại.

## **EF-02 – Invalid QR Code**

**Actor:** Member

**Description:**  
 Xảy ra khi hội viên sử dụng QR code không hợp lệ.

**Flow:**

1. Hội viên quét QR code.

2. Hệ thống kiểm tra QR code.

3. QR code không hợp lệ hoặc hết hạn.

**System Response:**

* Hiển thị thông báo **Invalid QR Code**.

* Check-in bị từ chối.

## **EF-03 – Network Connection Error**

**Actor:** All users

**Description:**  
 Xảy ra khi hệ thống mất kết nối mạng.

**Flow:**

1. Người dùng thực hiện một thao tác (booking, check-in, payment).

2. Hệ thống không thể kết nối server.

**System Response:**

* Hiển thị thông báo **Network error**.

* Người dùng được yêu cầu thử lại.

## **EF-04 – Duplicate Booking**

**Actor:** Member

**Description:**  
 Xảy ra khi hội viên cố đặt cùng một lớp nhiều lần.

**Flow:**

1. Hội viên đã đặt lớp học.

2. Hội viên tiếp tục gửi yêu cầu đặt cùng lớp.

3. Hệ thống kiểm tra booking hiện có.

**System Response:**

* Hệ thống từ chối yêu cầu.

* Hiển thị thông báo **You have already booked this class**.

## **EF-05 – Unauthorized Access**

**Actor:** Unauthorized user

**Description:**  
 Xảy ra khi người dùng cố truy cập chức năng không được phép.

**Flow:**

1. Người dùng truy cập một chức năng admin hoặc manager.

2. Hệ thống kiểm tra role của người dùng.

**System Response:**

* Từ chối truy cập.

* Hiển thị thông báo **Access Denied**.

# **8\. Future Enhancements**

Các tính năng có thể phát triển thêm:

* Mobile app

* Push notification

* AI workout recommendation

* Loyalty program

# **9\. Appendix**

Tài liệu liên quan:

* Test Plan

* Test Case

* Use Case Specification

* System Design Document
=======
# SOFTWARE REQUIREMENT SPECIFICATION (SRS)
**Gym Management System**
*Version: 1.0 | Date: 2026 | Author: QA Team*

## 1. Introduction
### 1.1 Purpose
Tài liệu Software Requirement Specification (SRS) này mô tả đầy đủ các yêu cầu chức năng và phi chức năng của hệ thống Gym Management System.
Tài liệu được sử dụng cho các mục đích:
- Làm cơ sở cho thiết kế hệ thống
- Hỗ trợ development team
- Là tài liệu tham chiếu cho tester để viết test case
- Là tài liệu thống nhất giữa stakeholders

### 1.2 Scope
Gym Management System là hệ thống quản lý hoạt động của phòng gym, bao gồm:
- Quản lý hội viên
- Quản lý gói tập
- Đặt lớp học và PT
- Check-in 
- Quản lý chi nhánh
- Quản lý bảng giá
- Quản lý tài khoản người dùng

Hệ thống có 3 loại người dùng:
- Member (Hội viên)
- Gym Manager (Quản lý gym)
- Admin (Quản trị hệ thống)

### 1.3 Definitions, Acronyms, Abbreviations
- **QR**: Quick Response Code
- **PT**: Personal Trainer
- **CRUD**: Create, Read, Update, Delete
- **SRS**: Software Requirement Specification
- **UI**: User Interface

### 1.4 References
- IEEE 830 Software Requirement Specification
- Gym business operation workflow
- Capstone Project Requirements

---

## 2. Overall Description
### 2.1 Product Perspective
Gym Management System là web-based application. Người dùng truy cập qua Web browser hoặc Mobile browser.
Hệ thống gồm các module:
- User Management
- Membership Management
- Class Booking
- QR Check-in
- Trainer Management
- Branch Management
- Pricing Management

### 2.2 Product Functions
Hệ thống hỗ trợ các chức năng chính:
- Đăng ký gói tập, Thanh toán gói tập
- Đặt lớp học, Đặt PT
- Check-in bằng QR Code
- Quản lý lịch lớp, hội viên, chi nhánh, bảng giá

### 2.3 User Classes and Characteristics
1. **Member:** Hội viên của phòng gym. Khả năng: Đăng ký gói tập, Đặt lớp học, Đặt PT, Check-in bằng QR. Trình độ sử dụng: cơ bản.
2. **Gym Manager:** Người quản lý hoạt động gym. Khả năng: Quản lý lớp học, hội viên, xác nhận booking, theo dõi doanh thu. Trình độ: trung cấp.
3. **Admin:** Quản trị hệ thống. Khả năng: Quản lý chi nhánh, tài khoản, bảng giá.

### 2.4 Operating Environment
- Hệ thống hoạt động trên: Web browser (Chrome, Firefox, Edge), Desktop, Mobile.
- Backend: API server, Database server.

### 2.5 Design and Implementation Constraints
- Hệ thống phải hỗ trợ QR check-in.
- Mỗi hội viên chỉ có 1 tài khoản.
- Lớp học có giới hạn số lượng người.

### 2.6 Assumptions and Dependencies
- Người dùng có internet.
- QR scanner hoạt động bình thường.
- Payment gateway hoạt động ổn định.

---

## 3. System Features
### 3.1 Membership Registration
*Description:* Cho phép hội viên đăng ký gói tập.
* **FR-01:** Hệ thống hiển thị danh sách gói tập.
* **FR-02:** Hệ thống hiển thị chi tiết gói tập.
* **FR-03:** Hội viên có thể chọn gói tập.
* **FR-04:** Hội viên có thể thanh toán gói tập.
* **FR-05:** Sau khi thanh toán thành công: trạng thái gói tập = Active.
* **FR-06:** Hệ thống lưu lịch sử gói tập.

### 3.2 Class Booking
*Description:* Cho phép hội viên đặt lớp học.
* **FR-07:** Hệ thống hiển thị danh sách lớp học.
* **FR-08:** Hệ thống hiển thị lịch PT.
* **FR-09:** Hội viên có thể đặt lớp học.
* **FR-10:** Hội viên có thể đặt PT.
* **FR-11:** Hệ thống gửi yêu cầu booking.
* **FR-12:** Gym Manager xác nhận booking.
* **FR-13:** Gym Manager từ chối booking.

### 3.3 QR Check-in
*Description:* Cho phép hội viên check-in khi đến phòng gym.
* **FR-14:** Hệ thống tạo QR code cho hội viên.
* **FR-15:** Hội viên có thể quét QR code để check-in.
* **FR-16:** Hệ thống ghi nhận thời gian check-in.
* **FR-17:** Chỉ hội viên có gói tập Active mới được check-in.
* **FR-18:** Hệ thống hiển thị lịch sử check-in.

### 3.4 Class Management
*Description:* Quản lý lớp học.
* **FR-19:** Gym Manager có thể tạo lớp học.
* **FR-20:** Gym Manager có thể chỉnh sửa lớp học.
* **FR-21:** Gym Manager có thể xóa lớp học.
* **FR-22:** Gym Manager có thể phân công PT.
* **FR-23:** Gym Manager có thể xem danh sách học viên.

### 3.5 Member Management
*Description:* Quản lý hội viên.
* **FR-24:** Gym Manager xem danh sách hội viên.
* **FR-25:** Gym Manager xem thông tin hội viên.
* **FR-26:** Gym Manager xem trạng thái gói tập.
* **FR-27:** Gym Manager khóa tài khoản hội viên.

### 3.6 Branch Management
*Description:* Quản lý chi nhánh.
* **FR-28:** Admin tạo chi nhánh.
* **FR-29:** Admin chỉnh sửa chi nhánh.
* **FR-30:** Admin xóa chi nhánh.
* **FR-31:** Admin xem danh sách chi nhánh.

### 3.7 Account Management
*Description:* Quản lý tài khoản.
* **FR-32:** Admin tạo tài khoản.
* **FR-33:** Admin chỉnh sửa tài khoản.
* **FR-34:** Admin khóa tài khoản.
* **FR-35:** Admin phân quyền.

### 3.8 Pricing Management
*Description:* Quản lý gói tập.
* **FR-36:** Admin tạo gói tập.
* **FR-37:** Admin chỉnh sửa gói tập.
* **FR-38:** Admin xóa gói tập.
* **FR-39:** Admin cấu hình giá.

---

## 4. External Interface Requirements
### 4.1 User Interfaces
- **Member UI:** Login, Dashboard, Membership, Booking, QR Check-in.
- **Gym Manager UI:** Class Management, Member Management, Booking Approval.
- **Admin UI:** Branch Management, Pricing Management, Account Management.

### 4.2 Hardware Interfaces
- Camera (QR scan)

### 4.3 Software Interfaces
- Payment gateway
- Database system

---

## 5. Non-functional Requirements
### 5.1 Performance
- **NFR-01:** Hệ thống phải phản hồi trong ≤ 3 giây.

### 5.2 Security
- **NFR-02:** Người dùng phải đăng nhập.
- **NFR-03:** Hệ thống phân quyền theo role.

### 5.3 Availability
- **NFR-04:** Hệ thống uptime ≥ 99%.

### 5.4 Reliability
- **NFR-05:** Hệ thống phải đảm bảo dữ liệu booking chính xác.

### 5.5 Usability
- **NFR-06:** UI phải dễ sử dụng.

### 5.6 Scalability
- **NFR-07:** Hệ thống hỗ trợ tối thiểu 1000 hội viên.

---

## 6. Data Model (High Level)
Các entity chính: User, Member, Trainer, Membership Package, Booking, Class, Branch, Payment, Check-in.

---

## 7. System Workflow
### 7.1 Overview
System workflow mô tả luồng hoạt động chính của Gym Management System giữa ba loại người dùng: Member, Gym Manager, Admin.
Quy trình bắt đầu từ việc hội viên đăng ký gói tập và đặt lớp học, sau đó quản lý gym lập lịch và xác nhận đặt lớp, cuối cùng admin quản lý thông tin chi nhánh và bảng giá.

### 7.2 Main Workflow
- **Step 1 – Membership Registration:** Hội viên đăng ký, đăng nhập, thanh toán gói tập -> Active Membership.
- **Step 2 – Class Booking:** Hội viên chọn lớp/PT, gửi yêu cầu -> Booking Pending.
- **Step 3 – Class Scheduling and Booking Confirmation:** Gym Manager tạo lịch, xác nhận/từ chối booking -> Approved/Rejected.
- **Step 4 – Gym Check-in:** Hội viên quét QR -> Ghi nhận thời gian check-in.
- **Step 5 – Branch and Pricing Management:** Admin quản lý chi nhánh và bảng giá.

### 7.3 Workflow Diagram (High-Level)
Member -> Register Membership -> Purchase Gym Package -> Book Class / PT -> Member Attend Gym -> QR Check-in
Gym Manager -> Create Class Schedule -> Approve / Reject Booking
Admin -> Manage Branches -> Manage Pricing -> System Update

### 7.4 Alternative Flows
- **AF-01 – Booking Rejected:** Quản lý từ chối đặt lớp -> Booking Rejected.
- **AF-02 – Class Fully Booked:** Lớp đầy -> Thông báo Class is full.
- **AF-03 – Expired Membership:** Gói tập hết hạn -> Yêu cầu gia hạn, từ chối check-in/booking.
- **AF-04 – Member Cancels Booking:** Hội viên tự hủy lớp -> Slot được giải phóng.

### 7.5 Exception Flows
- **EF-01 – Payment Failure:** Thanh toán lỗi -> Trạng thái Failed.
- **EF-02 – Invalid QR Code:** Quét mã sai/hết hạn -> Check-in bị từ chối.
- **EF-03 – Network Connection Error:** Lỗi kết nối -> Yêu cầu thử lại.
- **EF-04 – Duplicate Booking:** Đặt cùng 1 lớp nhiều lần -> Từ chối yêu cầu.
- **EF-05 – Unauthorized Access:** Truy cập sai Role -> Access Denied.

---

## 8. Future Enhancements
- Mobile app
- Push notification
- AI workout recommendation
- Loyalty program

---

## 9. Appendix
Tài liệu liên quan: Test Plan, Test Case, Use Case Specification, System Design Document.

---

## 10. Đặc tả Use Case chi tiết (MVP)

### UC-01: Đặt lớp tập (Class Booking)
* **Actor:** Member (Hội viên)
* **Luồng chính (Happy Path):** 1. Hội viên xem danh sách lớp học và PT còn trống (FR-07, FR-08).
  2. Hội viên chọn một lớp và gửi yêu cầu đặt (FR-09).
  3. Hệ thống tạo booking request với trạng thái Pending.
* **Luồng ngoại lệ (Alternative/Exception Flows):**
  * *Lớp đã đầy (AF-02):* Hệ thống hiển thị thông báo "Class is full", không cho phép đặt.
  * *Gói tập hết hạn (AF-03):* Hệ thống chặn và yêu cầu gia hạn.
  * *Trùng lịch (EF-04):* Hệ thống báo "You have already booked this class".

### UC-02: Check-in bằng QR Code
* **Actor:** Member (Hội viên)
* **Luồng chính (Happy Path):**
  1. Hội viên mở app, hệ thống tạo QR code (FR-14).
  2. Hội viên quét QR code tại cổng (FR-15).
  3. Hệ thống ghi nhận thời gian và cho phép qua cổng (FR-16).
* **Luồng ngoại lệ (Exception Flows):**
  * *Mã QR hết hạn/sai (EF-02):* Hệ thống báo "Invalid QR Code", từ chối check-in.
  * *Gói tập hết hạn (AF-03):* Từ chối check-in do không có Active Membership (FR-17).

### UC-03: Quản lý lớp tập và Duyệt Booking
* **Actor:** Gym Manager
* **Luồng chính (Happy Path):**
  1. Manager truy cập module Class Management.
  2. Manager tạo lớp học mới và phân công PT (FR-19, FR-22).
  3. Manager xem danh sách các yêu cầu đặt lớp của hội viên.
  4. Manager nhấn "Xác nhận", trạng thái booking đổi thành Approved (FR-12).
* **Luồng ngoại lệ (Alternative Flow):**
  * *Từ chối duyệt (AF-01):* Manager chọn Reject, trạng thái đổi thành Rejected và gửi thông báo cho Member.
>>>>>>> 278eea83ab171fbd770738e897ac7d0b342daa05
