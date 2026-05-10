import axiosClient from "./axiosClient";

const memberApi = {
  /** GET /api/users - Get all members (admin only) */
  getAll: (params) => {
    return axiosClient.get('/users', { params });
  },

  /** GET /api/users/{id} - Get member details */
  getById: (id) => {
    return axiosClient.get(`/users/${id}`);
  },

  /** GET /api/users/me - Get current user profile */
  getMe: () => {
    return axiosClient.get('/users/me');
  },

  /** POST /api/auth/register - Register new user */
  create: (data) => {
    return axiosClient.post('/auth/register', data);
  },

  /** PUT /api/users/me - Update current user profile */
  updateMe: (data) => {
    return axiosClient.put('/users/me', data);
  },

  /** PUT /api/users/{id} - Update user (admin) */
  update: (id, data) => {
    return axiosClient.put(`/users/${id}`, data);
  },

  /** DELETE /api/users/{id} - Delete user (admin) */
  delete: (id) => {
    return axiosClient.delete(`/users/${id}`);
  },

  /** PUT /api/users/me/avatar - Upload user avatar */
  uploadAvatar: (formData) => {
    return axiosClient.put('/users/me/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
};

export default memberApi;