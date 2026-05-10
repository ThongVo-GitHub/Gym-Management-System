import axiosClient from '@/api/axiosClient';

function adapt(c) {
  const s = (c.startTime || "").slice(0, 5);
  const e = (c.endTime || "").slice(0, 5);
  return {
    ...c,
    trainer: c.trainerName,
    time: s && e ? `${s} - ${e}` : "",
    slots: c.spotsLeft,
  };
}

export const classService = {
  /** GET /api/classes - Get all available classes */
  async getAll() {
    const res = await axiosClient.get('/classes');
    return (res.data || []).map(adapt);
  },

  /** GET /api/classes/{id} - Get class details */
  async getById(classId) {
    const res = await axiosClient.get(`/classes/${classId}`);
    return adapt(res.data);
  },

  /** GET /api/classes/schedule - Get class schedule by date */
  async getByDate(date) {
    const res = await axiosClient.get('/classes/schedule', { params: { date } });
    return (res.data || []).map(adapt);
  },
};
