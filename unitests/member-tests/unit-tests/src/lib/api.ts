// Minimal API stub – tests mock this entire module via vi.mock("../lib/api").
export const api = {
  async get<T>(_path: string): Promise<T> {
    throw new Error("api.get not mocked");
  },
  async post<T>(_path: string, _body?: unknown): Promise<T> {
    throw new Error("api.post not mocked");
  },
};
