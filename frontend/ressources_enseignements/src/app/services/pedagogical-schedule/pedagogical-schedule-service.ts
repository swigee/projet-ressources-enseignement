import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  RessourceSchedule,
  ValidationResponse,
  ValidationRequest,
  UpdateHours,
  PedagogicalSchedule,
} from '../../models/schedule/schedule.model';

@Injectable({
  providedIn: 'root'
})
export class PedagogicalScheduleService {
  private apiUrl = 'http://localhost:8080/api/pedagogical-schedule';

  constructor(private http: HttpClient) {}

  getAllRessources(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/ressources`);
  }

  getAllRessourcesDTO(): Observable<RessourceSchedule[]> {
    return this.http.get<RessourceSchedule[]>(`${this.apiUrl}/ressources/dto`);
  }

  getRessourceById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ressources/${id}`);
  }

  getRessourcesByFilter(year: string, className: string, semester: string): Observable<RessourceSchedule[]> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className)
      .set('semester', semester);

    return this.http.get<RessourceSchedule[]>(`${this.apiUrl}/ressources/filter`, { params });
  }

  getCompleteSchedule(year: string, className: string, semester: string): Observable<PedagogicalSchedule> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className)
      .set('semester', semester);

    return this.http.get<PedagogicalSchedule>(`${this.apiUrl}/schedule`, { params });
  }

  createRessource(ressource: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/ressources`, ressource);
  }

  updateRessource(id: number, ressource: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/ressources/${id}`, ressource);
  }

  updateRessourceHours(id: number, update: UpdateHours): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/ressources/${id}/hours`, update);
  }

  validateSchedule(validationRequest: ValidationRequest): Observable<ValidationResponse> {
    return this.http.post<ValidationResponse>(`${this.apiUrl}/schedule/validate`, validationRequest);
  }

  deleteRessource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ressources/${id}`);
  }

  searchRessources(keyword: string): Observable<RessourceSchedule[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<RessourceSchedule[]>(`${this.apiUrl}/ressources/search`, { params });
  }

  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}
