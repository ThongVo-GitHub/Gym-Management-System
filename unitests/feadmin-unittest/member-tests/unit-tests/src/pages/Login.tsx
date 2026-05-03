export function LoginPage() {
  return (
    <main>
      <h1 data-testid="login-title">Đăng nhập</h1>
      <label>
        Tài khoản
        <input data-testid="input-username" type="text" />
      </label>
      <label>
        Mật khẩu
        <input data-testid="input-password" type="password" />
      </label>
      <button data-testid="button-login">Đăng nhập</button>
    </main>
  );
}
