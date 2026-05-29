import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, catchError, throwError } from 'rxjs';
import {
  ResourceRow,
  TeacherBadge,
  ScheduleConflict,
  ResourcesTableResponse
} from '../../models/ressources/ressources.model';

@Injectable({
  providedIn: 'root'
})
export class RessourcesService {
  private apiUrl = `${environment.apiUrl}/api/ressources-table`;

  constructor(private http: HttpClient) {}

  getResourcesTable(year?: string, className?: string, semester?: string, program?: string): Observable<ResourcesTableResponse> {
    let params = new HttpParams();
    if (year) params = params.set('year', year);
    if (className) params = params.set('className', className);
    if (semester) params = params.set('semester', semester);
    if (program) params = params.set('program', program);

    return this.http.get<ResourcesTableResponse>(`${this.apiUrl}/data`, { params })
      .pipe(
        catchError(error => {
          if (error.status === 404) {
            return throwError(() => new Error('Resources not found'));
          }
          return throwError(() => new Error('Server error'));
        })
      );
  }

  getAvailableTeachers(): Observable<TeacherBadge[]> {
    return this.http.get<TeacherBadge[]>(`${this.apiUrl}/teachers`);
  }

  detectConflicts(teacherId: number): Observable<ScheduleConflict[]> {
    const params = new HttpParams().set('teacherId', teacherId.toString());
    return this.http.get<ScheduleConflict[]>(`${this.apiUrl}/conflicts`, { params });
  }

  searchResources(keyword: string): Observable<ResourceRow[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<ResourceRow[]>(`${this.apiUrl}/search`, { params });
  }
}
