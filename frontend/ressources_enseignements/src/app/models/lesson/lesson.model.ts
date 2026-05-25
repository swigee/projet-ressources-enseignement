export interface Lesson{
  id: number;
  title: string;
  description: string;
  cm_iut_hours: number;
  cm_state_hours: number;
  td_iut_hours: number;
  td_state_hours: number;
  tp_state_hours: number;
  tp_iut_hours: number;
  category: string;
  hours_per_week: number;
  hours_per_half_group: number;
}