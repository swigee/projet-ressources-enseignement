export interface CourseRow {
  id: string;
  courseName: string;
  category?: string;
  isHighlighted: boolean;
  hoursPerWeek: { [key: string]: number };
  hoursPerHalfGroup?: { [key: string]: number };
}

export interface Week {
  num: number;
  date: string;
  type: 'E' | 'S'; // E = Étudiant, S = Entreprise
}

export interface MonthData {
  month: string;
  weeks: Week[];
}

export interface YearData {
  name: string;
  classes: string[];
}

export interface ClassData {
  [key: string]: YearData;
}