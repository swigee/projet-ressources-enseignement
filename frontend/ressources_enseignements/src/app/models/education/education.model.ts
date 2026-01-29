import { Lesson } from "../lesson/lesson.model";

export interface Education {
  idformation?: number;
  name: string;
  description: string;
  lessons: Lesson[];
}
