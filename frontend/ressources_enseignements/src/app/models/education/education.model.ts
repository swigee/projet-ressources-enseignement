import { User } from "../../interfaces/user.interface";
import { Lesson } from "../lesson/lesson.model";

export interface EducationSemester {
  id?: number;
  year?: number;
  semester_number: number;
  parcours: string;
  resourceList: Lesson[];
}

export interface Education {
  id: number;
  name: string;
  description: string;
  className: string;
  year: number;
  lessons: Lesson[];
  sutdent: User[];
  parcours?: string;
  resourceList: Lesson[];
  semesters?: EducationSemester[];
}
