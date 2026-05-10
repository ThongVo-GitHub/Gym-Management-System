import axios from 'axios';

const axiosClient = axios.create({
  // baseURL phải là '/api' để Vite Proxy nhận diện và chuyển hướng sang 8081
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Danh sách các API không cần đính kèm Token
const AUTH_WHITELIST = [
  '/auth/login',
  '/auth/register',
  '/auth/refresh'
];

// INTERCEPTOR REQUEST: Tự động gắn Token vào Header
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    const isAuthPage = AUTH_WHITELIST.some((path) => config.url.includes(path));

    // Nếu là trang Login/Register thì xóa Authorization để tránh lỗi logic Server
    if (isAuthPage) {
      delete config.headers.Authorization;
    } 
    // Gắn Token cho các request còn lại nếu có
    else if (token && token !== 'null' && token !== 'undefined') {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// INTERCEPTOR RESPONSE: Xử lý lỗi 401 và tự động làm mới Token
axiosClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (!error.response) {
      console.error("Không thể kết nối đến Backend (8081)");
      return Promise.reject(error);
    }

    const isAuthRequest = AUTH_WHITELIST.some((path) => originalRequest.url.includes(path));

    // Nếu lỗi 401 (hết hạn) và không phải đang ở trang Login
    if (error.response.status === 401 && !isAuthRequest && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = localStorage.getItem('refreshToken');

      if (!refreshToken) {
        handleLogout();
        return Promise.reject(error);
      }

      try {
        // Gọi API refresh thông qua proxy /api/auth/refresh
        const res = await axios.post('/api/auth/refresh', {}, {
          headers: { Authorization: `Bearer ${refreshToken}` }
        });

        if (res.status === 200) {
          const newToken = res.data.token || res.data.accessToken;
          localStorage.setItem('token', newToken);
          
          // Thử lại request cũ với token mới
          originalRequest.headers.Authorization = `Bearer ${newToken}`;
          return axiosClient(originalRequest);
        }
      } catch (refreshError) {
        handleLogout();
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

const handleLogout = () => {
  localStorage.clear();
  if (window.location.pathname !== '/login') {
    window.location.href = '/login';
  }
};

export default axiosClient;