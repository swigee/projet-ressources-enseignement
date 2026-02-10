import { inject, Injectable, signal } from '@angular/core';
import { Education } from '../../models/education/education.model';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { LessonService } from '../lesson/lesson-service';
import { map, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EducationalManagerService {

  private readonly http = inject(HttpClient)

  api = "http://localhost:8080/api/education-manager"

  private readonly educationListDatabase = signal<Education[]>([]);
  educationList = this.educationListDatabase.asReadonly()
  lessonsService = inject(LessonService)

  constructor(private router: Router){
    this.loadEducations();
  }

  loadEducations(){
    this.http
    .get<Education[]>(`${this.api}/list`)
    .subscribe(t => this.educationListDatabase.set(t));
  }

  deleteEducation(id: number){
     this.http
    .delete<Education[]>(`${this.api}/${id}`)
    .subscribe(() => this.loadEducations());
  }

  createEducation(education: Education): Observable<Education>{
    return this.http.post<Education>(`${this.api}`, education)
  }

  updateEducation(etu: Education): Observable<Education>{
    console.log(etu);
    return this.http.put<Education>(`${this.api}/${etu.id}`, etu)
  }

  getEducationById(id: number): Observable<Education>{
    return this.http.get<Education>(`${this.api}/${id}`).pipe(
    switchMap(education =>
      this.lessonsService.loadLessonsById(id).pipe(
        map(lessons => {
          education.lessons = lessons;
          return education;
        })
      )
    )
  );
  }
}
