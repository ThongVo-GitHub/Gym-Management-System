import packageAPI from '@/api/packageAPI'; // Gọi sang lớp API

const packageService = {
  // Hàm này sẽ dùng ở trang React
  getDisplayPackages: async () => {
    try {
      const res = await packageAPI.getAll();
      // Bạn có thể lọc hoặc định dạng lại dữ liệu ở đây
      return res.data; 
    } catch (error) {
      throw error;
    }
  }
};

export default packageService;