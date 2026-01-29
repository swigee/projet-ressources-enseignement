// src/app/models/teacher.model.ts
// Note: Ces interfaces sont maintenant définies dans le service
// Ce fichier est optionnel si vous préférez centraliser les types

export interface Teacher {
  id: number;
  name: string;
  firstName?: string;
  lastName?: string;
  subject: string;
  status: string;
  hours?: string;
  totalHours?: number;
  remainingHours?: number;
  statusColor: string;
  roles?: string[];
}

export interface AffectationRow {
  id?: string; // Pour compatibilité avec l'ancien code
  ressourceId?: number; // ID de la ressource (backend)
  module: string;
  td: string;
  tp: string;
  cm: string;
  teachers: string[] | TeacherAssignment[]; // Flexible pour les deux formats
}

export interface TeacherAssignment {
  assignmentId: number;
  teacherId: number;
  teacherName: string;
  lessonType: string;
  assignedHours: number;
}

export interface DragData {
  type: 'teacher';
  teacherId: number;
  teacherName: string;
}

export interface AssignmentStatistics {
  totalTeachers: number;
  totalAssignments: number;
  totalHoursAssigned: number;
  unassignedModules: number;
  hoursByLessonType: { [key: string]: number };
  hoursByTeacher: { [key: string]: number };
}
