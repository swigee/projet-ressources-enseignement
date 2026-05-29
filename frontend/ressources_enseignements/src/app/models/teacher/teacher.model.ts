export interface Teacher {
  id: number;
  name: string;
  firstName: string;
  lastName: string;
  subject: string;
  status: string;
  totalHours: number;
  remainingHours: number;
  statusColor: string;
  roles?: string[];
}

export interface TeacherAssignment {
  assignmentId: number;
  teacherId: number;
  teacherName: string;
  lessonType: string;
  assignedHours: number;
}

export interface AssignmentRow {
  resourceId: number;
  module: string;
  groups?: string[];
  td: string;
  tp: string;
  cm: string;
  tdHours?: number;
  tpHours?: number;
  cmHours?: number;
  tdTeachers: TeacherAssignment[];
  tpTeachers: TeacherAssignment[];
  cmTeachers: TeacherAssignment[];
}


export interface AssignmentStatistics {
  totalTeachers: number;
  totalAssignments: number;
  totalHoursAssigned: number;
  unassignedModules: number;
  hoursByLessonType: { [key: string]: number };
  hoursByTeacher: { [key: string]: number };
}

export interface AssignmentGrid {
  selectedProgram: string;
  selectedYear: string;
  selectedSemester?: string;
  availableTeachers: Teacher[];
  assignmentGrid: AssignmentRow[];
  statistics: AssignmentStatistics;
}

export interface CreateAssignment {
  userId: number;
  resourceId: number;
  lessonType: string;
  assignedTimes: number;
}

export interface AssignmentValidationResponse {
  success: boolean;
  message: string;
  errors?: string[];
  warnings?: string[];
}
