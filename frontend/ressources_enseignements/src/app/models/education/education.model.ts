import { Lesson } from "../lesson/lesson.model";

export interface EducationSemester {
  id?: number;
  year?: number;
  semester_number: number;
  parcours: string;
  resourceList: Lesson[];
}

export interface Education {
  id?: number;
  name: string;
  description: string;
  parcours?: string;
  resourceList: Lesson[];
  semesters?: EducationSemester[];
}
