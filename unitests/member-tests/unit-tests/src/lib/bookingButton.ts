// Pure logic for the Booking page action button.
// Extracted so it can be unit-tested independently of the React component.

export interface BookingButtonInput {
  spotsLeft: number;
  isBooking: boolean;
}

export interface BookingButtonState {
  disabled: boolean;
  label: "ĐĂNG KÝ LỚP" | "HẾT CHỖ" | "LOADING";
  /** true => render with reduced opacity (class is full) */
  dimmed: boolean;
}

export function getBookingButtonState({ spotsLeft, isBooking }: BookingButtonInput): BookingButtonState {
  const full = spotsLeft <= 0;
  if (isBooking) {
    return { disabled: true, label: "LOADING", dimmed: full };
  }
  if (full) {
    return { disabled: true, label: "HẾT CHỖ", dimmed: true };
  }
  return { disabled: false, label: "ĐĂNG KÝ LỚP", dimmed: false };
}
