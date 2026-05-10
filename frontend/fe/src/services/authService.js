import axiosClient from '@/api/axiosClient';

const authService = {
  login: async (credentials) => {
    // Gọi đến POST /api/auth/login trong Swagger
    const response = await axiosClient.post('/auth/login', credentials);
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('refreshToken', response.data.refreshToken);
    }
    return response.data;
  },
  
  register: (userData) => axiosClient.post('/auth/register', userData),
};

export default authService;