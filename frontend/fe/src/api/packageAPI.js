import axiosClient from '@/api/axiosClient';

const packageApi = {
  getAll: () => axiosClient.get('/packages'),
  getById: (id) => axiosClient.get(`/packages/${id}`),
  create: (data) => axiosClient.post('/packages', data),
  update: (id, data) => axiosClient.put(`/packages/${id}`, data),
  delete: (id) => axiosClient.delete(`/packages/${id}`),
};

export default packageApi;