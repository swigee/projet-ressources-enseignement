import { Lesson } from "./lesson.model";

export interface Education {
  idformation: number;
  name: string;
  description: string;
  lessons: Lesson[];
}