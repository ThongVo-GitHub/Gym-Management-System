# Member Tests Bundle

Bộ test độc lập cho màn hình **Hội viên (Member)** — KHÔNG cần gộp vào dự án chính.
Có thể chạy riêng trong một project test, hoặc copy file vào project local của em.

## Nội dung

```
member-tests/
├── unit-tests/                  # Vitest + React Testing Library
│   ├── package.json
│   ├── vitest.config.ts
│   ├── tsconfig.json
│   └── src/
│       ├── test/setup.ts
│       ├── lib/
│       │   ├── bookingButton.ts            # logic được test
│       │   └── bookingButton.test.ts       # 5 cases
│       └── pages/
│           ├── Login.test.tsx              # 3 cases (validation đỏ)
│           └── Register.test.tsx           # 4 cases (validation đỏ + email/password)
│
└── e2e-cypress/                 # Cypress
    ├── package.json
    ├── cypress.config.ts
    ├── tsconfig.json
    └── cypress/
        ├── support/
        │   ├── e2e.ts
        │   └── commands.ts
        └── e2e/
            └── booking.cy.ts               # luồng cốt lõi: login -> book -> toast

```

## Chạy Unit Tests

```bash
cd unit-tests
npm install
npm test
```

Kỳ vọng: **13 tests passed**.

> Lưu ý: 2 test file `Login.test.tsx` và `Register.test.tsx` import từ
> `./pages/Login` và `./pages/Register` — đây là 2 component **placeholder**
> giống hệt logic của 2 trang Login/Register em đang dùng (đã thêm validation
> hiển thị chữ đỏ). Nếu em muốn test chính xác file của em, copy
> `Login.tsx` và `Register.tsx` từ project local (bản đã thêm validation lỗi
> đỏ) vào `unit-tests/src/pages/`.

## Chạy E2E (Cypress)

Yêu cầu: FE local đang chạy ở `http://localhost:5173` (đổi trong
`cypress.config.ts` nếu khác) và đã có 1 tài khoản Member tồn tại.

```bash
cd e2e-cypress
npm install
# chạy headless:
CYPRESS_USER_EMAIL=member@test.com CYPRESS_USER_PASSWORD=secret123 npx cypress run
# hoặc mở UI:
npx cypress open
```

## Kịch bản E2E

1. Mở trình duyệt → `/login`
2. Nhập tài khoản User → click **Đăng nhập**
3. Điều hướng `/booking` (Lịch đặt lớp)
4. Chọn lớp đầu tiên còn slot (hoặc theo biến `CYPRESS_CLASS_NAME`)
5. Click **Đăng ký lớp**
6. Verify: toast **"Đăng ký lớp thành công"** hiện ra

Có thêm 1 kịch bản phụ: bỏ trống mật khẩu khi login → phải hiện lỗi đỏ.
