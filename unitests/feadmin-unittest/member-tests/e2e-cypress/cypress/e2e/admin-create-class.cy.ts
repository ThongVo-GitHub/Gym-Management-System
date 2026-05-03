describe('Admin tạo lớp học', () => {
  it('Admin đăng nhập, tạo lớp mới và kiểm tra hiển thị ở dòng đầu tiên', () => {
    cy.loginAsAdmin('admin@example.com', 'Admin123!');

    cy.visit('/admin/classes');
    cy.get('[data-testid="button-create-class"]').click();

    cy.get('[data-testid="input-class-name"]').type('Lớp luyện tập A');
    cy.get('[data-testid="input-start-time"]').type('09:00');
    cy.get('[data-testid="input-end-time"]').type('11:00');
    cy.get('[data-testid="button-submit"]').click();

    cy.get('[data-testid="class-list-row-0"]').should('contain.text', 'Lớp luyện tập A');
  });
});
