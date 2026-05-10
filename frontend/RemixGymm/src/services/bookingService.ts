// src/services/bookingService.ts
// Sửa endpoint: POST /api/classes/{classId}/book (khớp BE)

import axios from "axios";

const API = import.meta.env.VITE_API_URL || "http://localhost:8081/api";

export interface BookingResponse {
  id: number;
  classId: number;
  name: string;
  trainerName: string;
  date: string;        // yyyy-MM-dd
  startTime: string;   // HH:mm:ss
  endTime: string;
  studio?: string;
  status?: string;     // BOOKED | COMPLETED | CANCELLED
  createdAt?: string;
}

function authHeader() {
  const token = localStorage.getItem("token");
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export const bookingService = {
  /** POST /api/classes/{classId}/book */
  async create(classId: number): Promise<BookingResponse> {
    const res = await axios.post<BookingResponse>(
      `${API}/classes/${classId}/book`,
      null,
      { headers: authHeader() }
    );
    return res.data;
  },

  /** GET /api/classes - Lấy tất cả lớp thay vì gọi my-bookings bị lỗi */
  async getMyBookings(): Promise<BookingResponse[]> {
  const res = await axios.get<any>(
    `${API}/classes/my-schedule`,  // ← đổi từ /classes sang /bookings/my
    { headers: authHeader() }
  );
  return Array.isArray(res.data)
    ? res.data
    : (res.data?.data ?? res.data?.content ?? []);
},

  /** DELETE /api/classes/bookings/{id} */
  // Sửa lại cho chắc ăn:
async cancel(bookingId: number): Promise<void> {
  if (!bookingId) {
    console.error("Lỗi: Không có ID để hủy!");
    return;
  }
  // Đảm bảo truyền đúng bookingId vào chuỗi Template String
  await axios.delete(`${API}/classes/${bookingId}/cancel`, { headers: authHeader() });
}
};
