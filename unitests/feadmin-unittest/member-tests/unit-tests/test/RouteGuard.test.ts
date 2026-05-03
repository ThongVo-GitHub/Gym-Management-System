import { canAccessAdminRoute } from '../src/guards/routeGuard';

describe('Route Guard Admin', () => {
  it('chặn người dùng không phải ADMIN truy cập /admin', () => {
    expect(canAccessAdminRoute('TRAINER')).toBe(false);
    expect(canAccessAdminRoute('USER')).toBe(false);
  });

  it('cho phép ADMIN truy cập /admin', () => {
    expect(canAccessAdminRoute('ADMIN')).toBe(true);
  });
});
