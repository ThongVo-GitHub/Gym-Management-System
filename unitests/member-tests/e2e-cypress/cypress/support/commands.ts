/// <reference types="cypress" />

// Helper: login through the UI.
Cypress.Commands.add("loginAsMember", (email: string, password: string) => {
  cy.visit("/login");
  cy.get('input[type="text"]').first().clear().type(email);
  cy.get('input[type="password"]').first().clear().type(password);
  cy.contains("button", /đăng nhập/i).click();
  cy.location("pathname", { timeout: 10000 }).should("not.eq", "/login");
});

declare global {
  // eslint-disable-next-line @typescript-eslint/no-namespace
  namespace Cypress {
    interface Chainable {
      loginAsMember(email: string, password: string): Chainable<void>;
    }
  }
}

export {};
