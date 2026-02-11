export interface WeekHours {
  cm: number;
  td: number;
  tp: number;
  total: number;
}

export interface RessourceSchedule {
  id: number;
  courseName: string;
  category?: string;
  isHighlighted: boolean;
  hoursPerWeek: { [key: string]: WeekHours };
  hoursPerHalfGroup: number;
  totalHours: number;
  totalCM?: number;
  totalTD?: number;
  totalTP?: number;
}

export interface ProjectSchedule {
  id: string;
  name: string;
  hoursPerWeek: { [key: string]: WeekHours };
  hoursPerHalfGroup: number;
  totalHours: number;
}

export interface Week {
  num: number;
  date: string;
  type: string; // 'E' or 'S'
}

export interface Month {
  month: string;
  weeks: Week[];
}

export interface ScheduleStatistics {
  totalResources: number;
  totalWithProject: number;
  companyWeeksCount: number;
  weeklyTotals: { [key: number]: number };
  weeklyTotalsWithProject: { [key: number]: number };
}

export interface PedagogicalSchedule {
  selectedYear: string;
  selectedClass: string;
  selectedSemester: string;
  scheduleData: RessourceSchedule[];
  projectData: ProjectSchedule;
  weeks: Month[];
  statistics: ScheduleStatistics;
}

export interface UpdateHours {
  ressourceId: number;
  hoursPerWeek: { [key: string]: WeekHours };
  hoursPerHalfGroup: number;
}

export interface ValidationRequest {
  selectedYear: string;
  selectedClass: string;
  selectedSemester: string;
  ressources: UpdateHours[];
  project: UpdateHours;
}

export interface ValidationResponse {
  success: boolean;
  message: string;
  errors?: string[];
}
