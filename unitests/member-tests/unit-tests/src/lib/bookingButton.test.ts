import { describe, it, expect } from "vitest";
import { getBookingButtonState } from "./bookingButton";

describe("getBookingButtonState", () => {
  it("enables button when slots are available", () => {
    const s = getBookingButtonState({ spotsLeft: 5, isBooking: false });
    expect(s.disabled).toBe(false);
    expect(s.label).toBe("ĐĂNG KÝ LỚP");
    expect(s.dimmed).toBe(false);
  });

  it("disables and dims button when class is full", () => {
    const s = getBookingButtonState({ spotsLeft: 0, isBooking: false });
    expect(s.disabled).toBe(true);
    expect(s.label).toBe("HẾT CHỖ");
    expect(s.dimmed).toBe(true);
  });

  it("treats negative slots as full", () => {
    const s = getBookingButtonState({ spotsLeft: -1, isBooking: false });
    expect(s.disabled).toBe(true);
    expect(s.label).toBe("HẾT CHỖ");
  });

  it("disables button while a booking request is in flight", () => {
    const s = getBookingButtonState({ spotsLeft: 3, isBooking: true });
    expect(s.disabled).toBe(true);
    expect(s.label).toBe("LOADING");
  });

  it("stays disabled if booking is loading on a full class", () => {
    const s = getBookingButtonState({ spotsLeft: 0, isBooking: true });
    expect(s.disabled).toBe(true);
    expect(s.dimmed).toBe(true);
  });
});
