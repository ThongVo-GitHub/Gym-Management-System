
-- Create classes table for dynamic slot tracking
CREATE TABLE public.classes (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  trainer TEXT NOT NULL,
  schedule TEXT NOT NULL,
  time TEXT NOT NULL,
  slots INTEGER NOT NULL DEFAULT 0,
  icon_name TEXT DEFAULT 'Dumbbell',
  accent TEXT DEFAULT '200, 70%, 50%',
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- Enable RLS
ALTER TABLE public.classes ENABLE ROW LEVEL SECURITY;

-- Everyone can read classes
CREATE POLICY "Anyone can view classes" ON public.classes FOR SELECT TO authenticated USING (true);

-- Authenticated users can update slots (for booking/cancel)
CREATE POLICY "Authenticated users can update classes" ON public.classes FOR UPDATE TO authenticated USING (true) WITH CHECK (true);

-- Seed data
INSERT INTO public.classes (id, name, trainer, schedule, time, slots, icon_name, accent) VALUES
  (1, 'Full Body Power', 'Quốc Dũng', 'Thứ 2 - 4 - 6', '18:00 - 19:30', 5, 'Dumbbell', '200, 70%, 50%'),
  (2, 'Upper Body Sculpt', 'Tuấn Anh', 'Thứ 3 - 5 - 7', '10:00 - 11:30', 0, 'Target', '280, 60%, 55%'),
  (3, 'Lower Body Strength', 'Minh Tú', 'Thứ 3 - 5 - 7', '17:00 - 18:30', 12, 'HeartPulse', '340, 75%, 55%'),
  (4, 'Cardio HIIT Blast', 'Hoàng Nam', 'Thứ 2 - 4 - 6', '19:30 - 20:30', 0, 'Flame', '24, 95%, 55%'),
  (5, 'Core & Abs Intense', 'Bích Phương', 'Thứ 2 - 4 - 6', '07:00 - 08:00', 8, 'Zap', '152, 60%, 48%');

-- Add class_id to user_schedules for linking
ALTER TABLE public.user_schedules ADD COLUMN class_id INTEGER REFERENCES public.classes(id) ON DELETE SET NULL;
