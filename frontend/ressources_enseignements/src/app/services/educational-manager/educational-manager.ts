import { inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Education } from '../../models/education/education.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from '../../interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class EducationalManagerService {

  private readonly http = inject(HttpClient)

  api = `${environment.apiUrl}/api/education-manager`

  private readonly educationListDatabase = signal<Education[]>([]);
  educationList = this.educationListDatabase.asReadonly()

  constructor(private router: Router){
    this.loadEducations();
  }

  loadEducations(){
    this.http
    .get<Education[]>(`${this.api}/list`)
    .subscribe(t => {
      this.educationListDatabase.set(t);
    });
  }

  deleteEducation(id: number){
     this.http
    .delete<Education[]>(`${this.api}/${id}`)
    .subscribe(() => this.loadEducations());
  }

  createEducation(education: any): Observable<any>{
    return this.http.post<any>(`${this.api}`, education)
  }

  updateEducation(etu: any): Observable<any>{
    const id = etu.id ?? etu.formation?.id;
    if (!id) {
      throw new Error('Education id is required to update formation');
    }
    return this.http.patch<any>(`${this.api}/${id}`, etu)
  }

  getEducationById(id: number): Observable<Education>{
    return this.http.get<Education>(`${this.api}/${id}`);
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