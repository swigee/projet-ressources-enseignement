import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import {
  RessourceRow,
  TeacherBadge,
  ScheduleConflict,
  RessourcesTableResponse
} from '../../models/ressources/ressources.model';

@Injectable({
  providedIn: 'root'
})
export class RessourcesService {
  private apiUrl = 'http://localhost:8080/api/ressources-table';

  constructor(private http: HttpClient) {}

  getRessourcesTable(year?: string, className?: string, semester?: string, formation?: string): Observable<RessourcesTableResponse> {
    let params = new HttpParams();
    if (year) params = params.set('year', year);
    if (className) params = params.set('className', className);
    if (semester) params = params.set('semester', semester);
    if (formation) params = params.set('formation', formation);

    return this.http.get<RessourcesTableResponse>(`${this.apiUrl}/data`, { params })
      .pipe(
        catchError(error => {
          if (error.status === 404) {
            return throwError(() => new Error('Ressources non trouvees'));
          }
          return throwError(() => new Error('Erreur serveur'));
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

  searchRessources(keyword: string): Observable<RessourceRow[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<RessourceRow[]>(`${this.apiUrl}/search`, { params });
  }
}
