import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PedagogicalScheduleDTO,RessourceScheduleDTO,ValidationRequestDTO,ValidationResponseDTO,ProjectScheduleDTO,ScheduleStatisticsDTO,WeekDTO,WeekHoursDTO,UpdateHoursDTO,MonthDTO } from '../../models/schedule/schedule.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PedagogicalScheduleService {
  private apiUrl = `${environment.apiUrl}/api/pedagogical-schedule`;

  constructor(private http: HttpClient) {}

  getAllRessources(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/ressources`);
  }

  getAllRessourcesDTO(): Observable<RessourceScheduleDTO[]> {
    return this.http.get<RessourceScheduleDTO[]>(`${this.apiUrl}/ressources/dto`);
  }

  getRessourceById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ressources/${id}`);
  }

  getRessourcesByFilter(year: string, className: string, semester: string): Observable<RessourceScheduleDTO[]> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className)
      .set('semester', semester);

    return this.http.get<RessourceScheduleDTO[]>(`${this.apiUrl}/ressources/filter`, { params });
  }

  getCompleteSchedule(year: string, className: string, semester: string, formation?: string): Observable<PedagogicalScheduleDTO> {
    let params = new HttpParams();
    if (year) params = params.set('year', year);
    if (className) params = params.set('className', className);
    if (semester) params = params.set('semester', semester);
    if (formation) params = params.set('formation', formation);

    return this.http.get<PedagogicalScheduleDTO>(`${this.apiUrl}/schedule`, { params });
  }

  createRessource(ressource: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/ressources`, ressource);
  }

  updateRessource(id: number, ressource: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/ressources/${id}`, ressource);
  }

  updateRessourceHours(id: number, updateDTO: UpdateHoursDTO): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/ressources/${id}/hours`, updateDTO);
  }

  validateSchedule(validationRequest: ValidationRequestDTO): Observable<ValidationResponseDTO> {
    return this.http.post<ValidationResponseDTO>(`${this.apiUrl}/schedule/validate`, validationRequest);
  }

  deleteRessource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ressources/${id}`);
  }

  searchRessources(keyword: string): Observable<RessourceScheduleDTO[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<RessourceScheduleDTO[]>(`${this.apiUrl}/ressources/search`, { params });
  }

  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}
