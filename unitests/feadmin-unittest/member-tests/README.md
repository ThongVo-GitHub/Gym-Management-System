# Member Tests

## Mục tiêu
- Đảm bảo unit test frontend đạt chuẩn.
- Thiết kế cấu trúc thư mục rõ ràng cho Component, Page, Hook, Service.
- Thống nhất đặt `data-testid` hoặc `data-test` cho các nút bấm, input để dễ viết automation.
- Tập trung vào màn hình của Admin và Trainer.

## Cấu trúc hiện tại

- `unit-tests/`
  - `src/pages/`: chứa giao diện Admin/Trainer và form tạo lớp học.
  - `src/guards/`: chứa logic route guard.
  - `test/`: chứa unit test.
  - `vite.config.ts`: cấu hình Vitest + React.
  - `package.json`: script chạy test.

- `e2e-cypress/`
  - `cypress/e2e/`: kịch bản Selenium/Cypress.
  - `cypress/support/commands.ts`: custom command `loginAsAdmin`.

## Các nhiệm vụ đã tạo mẫu

### Unit Test
- `test/CreateClassForm.test.tsx`: kiểm tra validation "Giờ kết thúc phải lớn hơn giờ bắt đầu".
- `test/RouteGuard.test.ts`: kiểm tra người dùng không có role ADMIN không thể truy cập route /admin.

### Automation Test
- `cypress/e2e/admin-create-class.cy.ts`: script cho luồng Admin đăng nhập, vào quản lý lớp, tạo lớp mới và kiểm tra lớp mới xuất hiện.

## Hướng dẫn chạy

1. Vào thư mục `member-tests/unit-tests`
2. Chạy `npm install` nếu chưa có `package.json`
3. Chạy `npm test`

Với Cypress:
1. Vào `member-tests/e2e-cypress`
2. Chạy `npm install`
3. Chạy `npm run cypress:open` hoặc `npm run cypress:run`
