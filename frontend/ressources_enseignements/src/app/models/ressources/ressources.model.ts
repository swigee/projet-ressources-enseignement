export interface RessourceRowDTO {
  id: number;
  moduleName: string;
  category?: string;
  heuresPrevisionnelles: number;
  heuresReelles: number;
  heuresTD: number;
  heuresTP: number;
  heuresCM: number;
  isHighlighted: boolean;
  assignedTeachers: TeacherBadgeDTO[];
}

export interface TeacherBadgeDTO {
  teacherId: number;
  fullName: string;  // "MARTIN Louis"
  assignedHours: number;
}

export interface ScheduleConflictDTO {
  teacherId: number;
  teacherName: string;
  conflictingModules: string[];
  weekNumber: number;
  timeSlot: string;
}

export interface RessourcesTotalsDTO {
  totalHeuresPrevisionnelles: number;
  totalHeuresReelles: number;
  totalHeuresTD: number;
  totalHeuresTP: number;
  totalHeuresCM: number;
}

export interface RessourcesTableResponseDTO {
  ressources: RessourceRowDTO[];
  availableTeachers: TeacherBadgeDTO[];
  conflicts: ScheduleConflictDTO[];
}
