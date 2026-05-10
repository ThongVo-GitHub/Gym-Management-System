import axiosClient from '@/api/axiosClient';

export const bookingService = {
  /** POST /api/bookings - Create booking for a class */
  async create(classId) {
    const res = await axiosClient.post('/bookings', { classId });
    return res.data;
  },

  /** GET /api/bookings/my - Get user's bookings */
  async getMyBookings() {
    const res = await axiosClient.get('/bookings/my');
    return res.data || [];
  },

  /** DELETE /api/bookings/{id} - Cancel booking */
  async cancel(bookingId) {
    await axiosClient.delete(`/bookings/${bookingId}`);
  },

  /** GET /api/bookings/{id} - Get booking details */
  async getById(bookingId) {
    const res = await axiosClient.get(`/bookings/${bookingId}`);
    return res.data;
  },
};
