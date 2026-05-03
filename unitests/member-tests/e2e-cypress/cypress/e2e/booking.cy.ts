/// <reference types="cypress" />

/**
 * E2E – Member core flow:
 *   Mở trình duyệt -> Đăng nhập User -> Vào Lịch tập -> Chọn lớp -> Click Đặt chỗ
 *   -> Verify toast "Đăng ký lớp thành công".
 *
 * Chạy:
 *   CYPRESS_USER_EMAIL=member@test.com CYPRESS_USER_PASSWORD=secret123 npx cypress run
 *   # hoặc:
 *   npx cypress open
 */

describe("Member booking flow", () => {
  const email = Cypress.env("USER_EMAIL") || "member@test.com";
  const password = Cypress.env("USER_PASSWORD") || "secret123";
  // Optional: chuỗi tên lớp muốn đặt. Để trống = chọn lớp đầu tiên còn slot.
  const classNameQuery = Cypress.env("CLASS_NAME") || "";

  it("đăng nhập, chọn lớp còn trống, đặt chỗ và thấy toast thành công", () => {
    // 1. Mở trình duyệt -> /login
    cy.visit("/login");

    // 2. Nhập tài khoản và đăng nhập
    cy.get('input[type="text"]').first().type(email);
    cy.get('input[type="password"]').first().type(password);
    cy.contains("button", /đăng nhập/i).click();

    // 3. Sau khi đăng nhập, vào trang đặt lịch tập
    cy.location("pathname", { timeout: 10000 }).should("not.eq", "/login");
    cy.visit("/booking");

    // 4. Đợi danh sách lớp hiển thị
    cy.contains(/danh sách lớp học nhóm/i, { timeout: 10000 }).should("be.visible");

    // 5. Chọn 1 lớp để đặt
    const cardSelector = ".glass-card-hover";
    cy.get(cardSelector, { timeout: 10000 }).should("have.length.greaterThan", 0);

    const target = classNameQuery
      ? cy.get(cardSelector).contains(classNameQuery).parents(cardSelector).first()
      : cy
          .get(cardSelector)
          .filter((_, el) => {
            const btn = el.querySelector("button");
            return !!btn && !(btn as HTMLButtonElement).disabled;
          })
          .first();

    target.within(() => {
      cy.contains("button", /đăng ký lớp/i)
        .should("not.be.disabled")
        .click();
    });

    // 6. Verify toast "Đăng ký lớp thành công" hiện ra (sonner)
    cy.get("[data-sonner-toast], li[role='status']", { timeout: 8000 })
      .should("be.visible")
      .and("contain.text", "Đăng ký lớp thành công");
  });

  it("hiện lỗi đỏ khi bỏ trống mật khẩu lúc đăng nhập", () => {
    cy.visit("/login");
    cy.get('input[type="text"]').first().type(email);
    cy.contains("button", /đăng nhập/i).click();
    cy.contains(/vui lòng nhập mật khẩu/i)
      .should("be.visible")
      .and("have.attr", "role", "alert");
  });
});
