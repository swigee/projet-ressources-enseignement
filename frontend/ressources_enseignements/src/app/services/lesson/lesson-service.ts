import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Lesson } from '../../models/lesson/lesson.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LessonService {

  private readonly http = inject(HttpClient)

  api = "http://localhost:8080/api/education-manager"

  private readonly lessonListDatabase = signal<Lesson[]>([]);
  lessonsList = this.lessonListDatabase.asReadonly()

  constructor() {
    this.loadLessons();
  }

  loadLessons(){
    this.http
    .get<Lesson[]>(`${this.api}/lessons/list`)
    .subscribe(t => this.lessonListDatabase.set(t));
  }

  loadLessonsById(id: number): Observable<Lesson[]>{
    return this.http.get<Lesson[]>(`${this.api}/lessons/${id}`);
  }
}
