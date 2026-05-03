import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Register from "./Register";

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

const renderRegister = () =>
  render(
    <MemoryRouter>
      <Register />
    </MemoryRouter>,
  );

describe("Register form validation", () => {
  beforeEach(() => postMock.mockReset());

  it("shows red errors for every empty field", async () => {
    renderRegister();
    fireEvent.click(screen.getByRole("button", { name: /đăng ký/i }));

    expect(await screen.findByText("Vui lòng nhập username")).toBeInTheDocument();
    expect(screen.getByText("Vui lòng nhập họ và tên")).toBeInTheDocument();
    expect(screen.getByText("Vui lòng nhập email")).toBeInTheDocument();
    expect(screen.getByText("Vui lòng nhập mật khẩu")).toBeInTheDocument();
    expect(postMock).not.toHaveBeenCalled();
  });

  it("rejects invalid email format", async () => {
    renderRegister();
    fireEvent.change(screen.getByLabelText("Username"), { target: { value: "tester" } });
    fireEvent.change(screen.getByLabelText("Họ và tên"), { target: { value: "Tester" } });
    fireEvent.change(screen.getByLabelText("Email"), { target: { value: "not-an-email" } });
    fireEvent.change(screen.getByLabelText("Mật khẩu"), { target: { value: "secret123" } });
    fireEvent.click(screen.getByRole("button", { name: /đăng ký/i }));

    expect(await screen.findByText("Email không hợp lệ")).toBeInTheDocument();
  });

  it("rejects passwords shorter than 6 characters", async () => {
    renderRegister();
    fireEvent.change(screen.getByLabelText("Username"), { target: { value: "tester" } });
    fireEvent.change(screen.getByLabelText("Họ và tên"), { target: { value: "Tester" } });
    fireEvent.change(screen.getByLabelText("Email"), { target: { value: "t@a.com" } });
    fireEvent.change(screen.getByLabelText("Mật khẩu"), { target: { value: "123" } });
    fireEvent.click(screen.getByRole("button", { name: /đăng ký/i }));

    expect(await screen.findByText("Mật khẩu tối thiểu 6 ký tự")).toBeInTheDocument();
  });

  it("submits when all fields are valid", async () => {
    postMock.mockResolvedValueOnce({});
    renderRegister();
    fireEvent.change(screen.getByLabelText("Username"), { target: { value: "tester" } });
    fireEvent.change(screen.getByLabelText("Họ và tên"), { target: { value: "Tester" } });
    fireEvent.change(screen.getByLabelText("Email"), { target: { value: "t@a.com" } });
    fireEvent.change(screen.getByLabelText("Mật khẩu"), { target: { value: "secret123" } });
    fireEvent.click(screen.getByRole("button", { name: /đăng ký/i }));

    await waitFor(() => {
      expect(postMock).toHaveBeenCalledWith("/auth/register", {
        username: "tester",
        email: "t@a.com",
        password: "secret123",
        fullName: "Tester",
      });
    });
  });
});
