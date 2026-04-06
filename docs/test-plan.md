# KẾ HOẠCH KIỂM THỬ (TEST PLAN) - GYM MANAGEMENT SYSTEM

**Dự án:** Gym Management System | **Phiên bản:** 2.0 (Cập nhật MVP)
**Tác giả (QA/Tester):** Trần Hoàng Anh Khuê | **Ngày lập:** 13/03/2026 (Tuần 3)

---

## 1. Mục Đích (Purpose)
Tài liệu này xác định phạm vi, chiến lược, tài nguyên và tiêu chí kiểm thử cho hệ thống Gym Management System. Mục tiêu là đảm bảo chất lượng phần mềm đáp ứng đầy đủ yêu cầu nghiệp vụ (MVP) và đạt được các chỉ số kỹ thuật đã cam kết trong tài liệu SRS.

---

## 2. Phạm Vi Kiểm Thử (Test Scope)

### 2.1. Trong phạm vi (In-Scope)
Kiểm thử toàn bộ 10 Use Case cốt lõi cho 3 phân hệ:
- **Phân hệ Member:** Xác thực (UC-01), Thanh toán (UC-02), Đặt lớp (UC-03), QR Check-in (UC-04).
- **Phân hệ Gym Manager:** Quản lý lớp & Duyệt đặt chỗ (UC-05), Quản lý hội viên (UC-06), Duyệt thanh toán (UC-07).
- **Phân hệ Admin:** Quản lý Chi nhánh (UC-08), Bảng giá (UC-09), Quản lý nhân sự (UC-10).

### 2.2. Ngoài phạm vi (Out-of-Scope)
- Giao dịch thực tế trên cổng thanh toán (chỉ dùng Sandbox).
- Kiểm thử hiệu năng (Load Test) vượt quá 1000 hội viên.
- Bảo mật chuyên sâu ngoài phạm vi RBAC.

---

## 3. Chiến Lược Kiểm Thử (Test Strategy)

| Thành phần | Trọng số | Công cụ | Chỉ tiêu chất lượng |
| :--- | :--- | :--- | :--- |
| **Unit Test (BE)** | 30% | JUnit 5, Mockito | Coverage > 70%, bao phủ logic nghiệp vụ. |
| **Unit Test (FE)** | (Trong 30%) | Jest, Vitest | Kiểm thử Component và Validation. |
| **Manual Test** | 40% | Excel | 100% Test Cases được thực thi. |
| **Automation** | +20% | Postman, Playwright | Tự động hóa luồng: Đăng nhập -> Mua gói -> Đặt lớp. |

---

## 4. Môi Trường Kiểm Thử (Test Environment)
- **Cấu hình:** Chạy Localhost (Spring Boot & React).
- **Database:** PostgreSQL.
- **Trình duyệt:** Google Chrome, MS Edge (Hỗ trợ Responsive).
- **Công cụ:** Postman (Test 20-30 API endpoints).

---

## 5. Dữ Liệu Kiểm Thử (Test Data)
QA Team chuẩn bị bộ dữ liệu mẫu trong `database/test-data/`:
- Danh sách tài khoản mẫu (Admin, Manager, Member).
- Dữ liệu gói tập (Active, Expired, Pending).
- Lịch trình lớp học mẫu (để test trường hợp Full slots).

---

## 6. Tiêu Chí Đánh Giá (Pass/Fail Criteria)
- **Điều kiện Dừng (Suspension):** Hệ thống gặp lỗi Blocker (Sập API, không thể đăng nhập).
- **Điều kiện Đạt (Exit Criteria):**
  - 100% Happy Path Test Cases đạt "Pass".
  - Không còn lỗi Critical và High.
  - Hoàn thiện Test Report và Coverage Report.

---

## 7. Phân Công Trách Nhiệm (Roles)
- **Võ Minh Thông (PM/BE):** Giám sát, Unit Test BE, xử lý Bug logic.
- **Trần Hoàng Anh Khuê (QA):** Chủ trì Test Plan, Manual Test Cases, Automation Test.
- **Lê Nhật Vy (FE/QA):** Kịch bản UI, Manual Test giao diện, Unit Test FE.
- **Lê Dư Như Ý (FE):** Xử lý UI Bug, đảm bảo tính Responsive cho mã QR.
