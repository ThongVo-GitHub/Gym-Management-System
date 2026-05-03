import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CreateClassForm } from '../src/pages/CreateClassForm';

describe('CreateClassForm', () => {
  it('hiện lỗi khi giờ kết thúc nhỏ hơn hoặc bằng giờ bắt đầu', async () => {
    render(<CreateClassForm />);

    await userEvent.clear(screen.getByTestId('input-start-time'));
    await userEvent.type(screen.getByTestId('input-start-time'), '09:00');
    await userEvent.clear(screen.getByTestId('input-end-time'));
    await userEvent.type(screen.getByTestId('input-end-time'), '08:00');
    await userEvent.click(screen.getByTestId('button-submit'));

    expect(screen.getByTestId('validation-error')).toHaveTextContent(
      'Giờ kết thúc phải lớn hơn giờ bắt đầu.'
    );
  });

  it('không hiển thị lỗi khi giờ kết thúc lớn hơn giờ bắt đầu', async () => {
    render(<CreateClassForm />);

    await userEvent.clear(screen.getByTestId('input-start-time'));
    await userEvent.type(screen.getByTestId('input-start-time'), '09:00');
    await userEvent.clear(screen.getByTestId('input-end-time'));
    await userEvent.type(screen.getByTestId('input-end-time'), '10:30');
    await userEvent.click(screen.getByTestId('button-submit'));

    expect(screen.queryByTestId('validation-error')).toBeNull();
  });
});
