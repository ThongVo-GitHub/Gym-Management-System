import axiosClient from "./axiosClient";

const invoiceApi = {
  getAll: (params) => {
    return axiosClient.get('/invoices', { params });
  },
  getById: (id) => {
    return axiosClient.get(`/invoices/${id}`);
  },
  create: (data) => {
    return axiosClient.post('/invoices', data);
  },
  update: (id, data) => {
    return axiosClient.put(`/invoices/${id}`, data);
  },
  delete: (id) => {
    return axiosClient.delete(`/invoices/${id}`);
  }
};

export default invoiceApi;
