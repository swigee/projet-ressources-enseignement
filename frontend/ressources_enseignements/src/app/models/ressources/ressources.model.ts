export interface RessourceRow {
  id: number;
  moduleName: string;
  category?: string;
  plannedHours: number;
  actualHours: number;
  tdHours: number;
  tpHours: number;
  cmHours: number;
  isHighlighted: boolean;
  assignedTeachers: TeacherBadge[];
}

export interface TeacherBadge {
  teacherId: number;
  fullName: string;
  assignedHours: number;
}

export interface ScheduleConflict {
  teacherId: number;
  teacherName: string;
  conflictingModules: string[];
  weekNumber: number;
  timeSlot: string;
}

export interface RessourcesTotals {
  totalPlannedHours: number;
  totalActualHours: number;
  totalTDHours: number;
  totalTPHours: number;
  totalCMHours: number;
}

export interface RessourcesTableResponse {
  ressources: RessourceRow[];
  availableTeachers: TeacherBadge[];
  conflicts: ScheduleConflict[];
}
