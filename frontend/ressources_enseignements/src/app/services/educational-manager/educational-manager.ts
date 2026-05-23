import { inject, Injectable, signal } from '@angular/core';
import { Education } from '../../models/education/education.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { LessonService } from '../lesson/lesson-service';
import { map, switchMap } from 'rxjs';
import { User } from '../../interfaces/user.interface';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EducationalManagerService {

  private readonly http = inject(HttpClient)

  api = `${environment.apiUrl}/api/education-manager`

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

  getUsers(id: number): Observable<User[]>{
    return this.http.get<User[]>(`${this.api}/${id}/users`);
  }

  duplicateEducation(id: number, newName: string): Observable<Education> {
    return this.http.post<Education>(`${this.api}/${id}/duplicate`, { newName });
  }

  getDistinctFormationNames(): string[] {
    const names = this.educationList().map(e => e.name);
    return [...new Set(names)].sort();
  }

  loadDistinctFormationNames(): Observable<string[]> {
    return this.http.get<Education[]>(`${this.api}/list`).pipe(
      map(educations => [...new Set(educations.map(e => e.name))].sort())
    );
  }

  getDistinctClasses(year?: string, formation?: string): Observable<string[]> {
    let params = new HttpParams();
    if (year) params = params.set('year', year);
    if (formation) params = params.set('formation', formation);
    return this.http.get<string[]>(`${this.api}/classes`, { params });
  }
}