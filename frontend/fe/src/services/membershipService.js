import axiosClient from '@/api/axiosClient';

const membershipService = {
  // POST /api/membership/buy
  buyPackage: (packageData) => axiosClient.post('/membership/buy', packageData),

  // PUT /api/membership/{invoiceId}/confirm
  confirmPayment: (invoiceId) => axiosClient.put(`/membership/${invoiceId}/confirm`),
  
  // GET /api/membership/me
  getMyMembership: () => axiosClient.get('/membership/me'),
};

export default membershipService;