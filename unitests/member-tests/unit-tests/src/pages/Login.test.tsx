import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Login from "./Login";

vi.mock("sonner", () => ({
  toast: { error: vi.fn(), success: vi.fn() },
  Toaster: () => null,
}));

const postMock = vi.fn();
vi.mock("../lib/api", () => ({
  api: {
    post: (...args: unknown[]) => postMock(...args),
    get: vi.fn(),
  },
}));

const renderLogin = () =>
  render(
    <MemoryRouter>
      <Login />
    </MemoryRouter>,
  );

describe("Login form validation", () => {
  beforeEach(() => postMock.mockReset());

  it("shows red inline error when password is empty", async () => {
    renderLogin();
    fireEvent.change(screen.getByLabelText("Email hoặc Username"), { target: { value: "user@test.com" } });
    fireEvent.click(screen.getByRole("button", { name: /đăng nhập/i }));

    const error = await screen.findByText("Vui lòng nhập mật khẩu");
    expect(error).toBeInTheDocument();
    expect(error).toHaveAttribute("role", "alert");
    expect(error.className).toMatch(/text-destructive/);
    expect(postMock).not.toHaveBeenCalled();
  });

  it("shows error when username/email is empty", async () => {
    renderLogin();
    fireEvent.change(screen.getByLabelText("Mật khẩu"), { target: { value: "secret123" } });
    fireEvent.click(screen.getByRole("button", { name: /đăng nhập/i }));

    expect(await screen.findByText("Vui lòng nhập email hoặc username")).toBeInTheDocument();
    expect(postMock).not.toHaveBeenCalled();
  });

  it("submits login when both fields are filled", async () => {
    postMock.mockResolvedValueOnce({ accessToken: "fake.jwt.token" });
    renderLogin();
    fireEvent.change(screen.getByLabelText("Email hoặc Username"), { target: { value: "user@test.com" } });
    fireEvent.change(screen.getByLabelText("Mật khẩu"), { target: { value: "secret123" } });
    fireEvent.click(screen.getByRole("button", { name: /đăng nhập/i }));

    await waitFor(() => {
      expect(postMock).toHaveBeenCalledWith("/auth/login", {
        usernameOrEmail: "user@test.com",
        password: "secret123",
      });
    });
  });
});
