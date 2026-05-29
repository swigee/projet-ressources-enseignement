export interface WeekHoursDTO {
  cm: number;
  td: number;
  tp: number;
  total: number;
}

export interface ResourceScheduleDTO {
  id: number;
  courseName: string;
  category?: string;
  isHighlighted: boolean;
  hoursPerWeek: { [key: string]: WeekHoursDTO };
  hoursPerHalfGroup: number;
  totalHours: number;
  totalCM?: number;
  totalTD?: number;
  totalTP?: number;
}

export interface ProjectScheduleDTO {
  id: string;
  name: string;
  hoursPerWeek: { [key: string]: WeekHoursDTO };
  hoursPerHalfGroup: number;
  totalHours: number;
}

export interface WeekDTO {
  num: number;
  date: string;
  type: string; // 'E' or 'S'
}

export interface MonthDTO {
  month: string;
  weeks: WeekDTO[];
}

export interface ScheduleStatisticsDTO {
  totalResources: number;
  totalWithProject: number;
  companyWeeksCount: number;
  weeklyTotals: { [key: number]: number };
  weeklyTotalsWithProject: { [key: number]: number };
}

export interface PedagogicalScheduleDTO {
  selectedYear: string;
  selectedClass: string;
  selectedSemester: string;
  scheduleData: ResourceScheduleDTO[];
  projectData: ProjectScheduleDTO;
  weeks: MonthDTO[];
  statistics: ScheduleStatisticsDTO;
}

export interface UpdateHoursDTO {
  resourceId: number;
  hoursPerWeek: { [key: string]: WeekHoursDTO };
  hoursPerHalfGroup: number;
}

export interface ValidationRequestDTO {
  selectedYear: string;
  selectedClass: string;
  selectedSemester: string;
  resources: UpdateHoursDTO[];
  project: UpdateHoursDTO;
}

export interface ValidationResponseDTO {
  success: boolean;
  message: string;
  errors?: string[];
}
