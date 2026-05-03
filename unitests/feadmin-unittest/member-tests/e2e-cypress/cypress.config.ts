import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4173',
    specPattern: 'cypress/e2e/**/*.cy.ts',
    supportFile: 'cypress/support/commands.ts',
    setupNodeEvents() {
      // Có thể mở rộng với plugin nếu cần.
    }
  }
});
