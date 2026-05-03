import { CreateClassForm } from './CreateClassForm';

export function AdminDashboard() {
  return (
    <main>
      <h1 data-testid="admin-dashboard-title">Quản lý Admin</h1>
      <CreateClassForm />
    </main>
  );
}
