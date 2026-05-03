import { useState } from 'react';

export interface CreateClassFormValues {
  className: string;
  startTime: string;
  endTime: string;
}

export function CreateClassForm() {
  const [values, setValues] = useState<CreateClassFormValues>({
    className: '',
    startTime: '08:00',
    endTime: '09:00'
  });
  const [error, setError] = useState('');

  const validate = (nextValues: CreateClassFormValues) => {
    if (nextValues.endTime <= nextValues.startTime) {
      return 'Giờ kết thúc phải lớn hơn giờ bắt đầu.';
    }
    return '';
  };

  const handleChange = (field: keyof CreateClassFormValues, value: string) => {
    setValues((current) => ({ ...current, [field]: value }));
    setError(validate({ ...values, [field]: value }));
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const validationError = validate(values);
    setError(validationError);
    if (!validationError) {
      window.alert(`Tạo lớp học: ${values.className}`);
    }
  };

  return (
    <form onSubmit={handleSubmit} data-testid="create-class-form">
      <div>
        <label htmlFor="class-name">Tên lớp học</label>
        <input
          id="class-name"
          data-testid="input-class-name"
          value={values.className}
          onChange={(event) => handleChange('className', event.target.value)}
        />
      </div>
      <div>
        <label htmlFor="start-time">Giờ bắt đầu</label>
        <input
          id="start-time"
          type="time"
          data-testid="input-start-time"
          value={values.startTime}
          onChange={(event) => handleChange('startTime', event.target.value)}
        />
      </div>
      <div>
        <label htmlFor="end-time">Giờ kết thúc</label>
        <input
          id="end-time"
          type="time"
          data-testid="input-end-time"
          value={values.endTime}
          onChange={(event) => handleChange('endTime', event.target.value)}
        />
      </div>
      {error && <p role="alert" data-testid="validation-error">{error}</p>}
      <button type="submit" data-testid="button-submit">Lưu lớp học</button>
    </form>
  );
}
