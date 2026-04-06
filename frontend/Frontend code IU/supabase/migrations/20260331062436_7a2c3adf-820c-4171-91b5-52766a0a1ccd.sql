
-- Drop permissive update policy
DROP POLICY "Authenticated users can update classes" ON public.classes;

-- Create secure functions for slot management
CREATE OR REPLACE FUNCTION public.decrement_class_slot(p_class_id INTEGER)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = public
AS $$
BEGIN
  UPDATE public.classes SET slots = slots - 1 WHERE id = p_class_id AND slots > 0;
END;
$$;

CREATE OR REPLACE FUNCTION public.increment_class_slot(p_class_id INTEGER)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = public
AS $$
BEGIN
  UPDATE public.classes SET slots = slots + 1 WHERE id = p_class_id;
END;
$$;
