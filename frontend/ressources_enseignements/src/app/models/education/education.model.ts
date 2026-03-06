import { User } from "../../interfaces/user.interface";
import { Lesson } from "../lesson/lesson.model";

export interface Education {
  id: number;
  name: string;
  description: string;
  className: string;
  year: number;
  lessons: Lesson[];
  sutdent: User[];
}
