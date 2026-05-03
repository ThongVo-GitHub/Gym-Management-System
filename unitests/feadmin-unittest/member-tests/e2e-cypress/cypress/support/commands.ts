Cypress.Commands.add('loginAsAdmin', (username: string, password: string) => {
  cy.visit('/login');
  cy.get('[data-testid="input-username"]').type(username);
  cy.get('[data-testid="input-password"]').type(password);
  cy.get('[data-testid="button-login"]').click();
});
