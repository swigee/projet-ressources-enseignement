import { Lesson } from "../lesson/lesson.model";

export interface Education {
  id?: number;
  name: string;
  description: string;
  lessons: Lesson[];
}
