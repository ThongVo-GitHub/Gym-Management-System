export type UserRole = 'ADMIN' | 'TRAINER' | 'USER';

export function canAccessAdminRoute(role: UserRole | string) {
  return role === 'ADMIN';
}

export function getRedirectForRole(role: UserRole | string) {
  return canAccessAdminRoute(role) ? '/admin' : '/unauthorized';
}
