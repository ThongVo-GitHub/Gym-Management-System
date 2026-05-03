import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    // Đổi sang URL FE local của em (ví dụ http://localhost:8080) nếu cần
    baseUrl: "http://localhost:3000",
    supportFile: "cypress/support/e2e.ts",
    specPattern: "cypress/e2e/**/*.cy.{ts,tsx,js,jsx}",
    defaultCommandTimeout: 8000,
    viewportWidth: 1280,
    viewportHeight: 800,
  },
});
