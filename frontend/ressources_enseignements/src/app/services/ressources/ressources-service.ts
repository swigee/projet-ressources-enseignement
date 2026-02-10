import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import {
  RessourceRowDTO,
  TeacherBadgeDTO,
  ScheduleConflictDTO,
  RessourcesTableResponseDTO
} from '../../models/ressources/ressources.model';

@Injectable({
  providedIn: 'root'
})
export class RessourcesService {
  private apiUrl = 'http://localhost:8080/api/ressources-table';

  constructor(private http: HttpClient) {}

  getRessourcesTable(year: string, className: string, semester: string): Observable<RessourcesTableResponseDTO> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className)
      .set('semester', semester);

    return this.http.get<RessourcesTableResponseDTO>(`${this.apiUrl}/data`, { params })
      .pipe(
        catchError(error => {
          if (error.status === 404) {
            return throwError(() => new Error('Ressources non trouvees'));
          }
          return throwError(() => new Error('Erreur serveur'));
        })
      );
  }

  getAvailableTeachers(): Observable<TeacherBadgeDTO[]> {
    return this.http.get<TeacherBadgeDTO[]>(`${this.apiUrl}/teachers`);
  }

  detectConflicts(teacherId: number): Observable<ScheduleConflictDTO[]> {
    const params = new HttpParams().set('teacherId', teacherId.toString());
    return this.http.get<ScheduleConflictDTO[]>(`${this.apiUrl}/conflicts`, { params });
  }

  searchRessources(keyword: string): Observable<RessourceRowDTO[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<RessourceRowDTO[]>(`${this.apiUrl}/search`, { params });
  }
}
