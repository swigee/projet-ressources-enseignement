export interface Teacher {
  id: number;
  name: string;
  subject: string; 
  status: 'Permanent' | 'Vacataire';
  hours: string;
  statusColor: string;
}

export interface AffectationRow {
  id: string;
  module: string;
  td: string;
  tp: string;
  cm: string;
  teachers: string[];
}

export interface DragData {
  type: 'teacher';
  teacherId: number;
  teacherName: string;
}