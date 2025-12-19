import { Lesson } from "./lesson.model";

export interface Education {
  id: string;
  nom: string;
  description: string;
  lessons: Lesson[];
}