import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { PedagogicalScheduleDTO, ResourceScheduleDTO, ValidationRequestDTO, ValidationResponseDTO, ProjectScheduleDTO, ScheduleStatisticsDTO, WeekDTO, WeekHoursDTO, UpdateHoursDTO, MonthDTO } from '../../models/schedule/schedule.model';

@Injectable({
  providedIn: 'root'
})
export class PedagogicalScheduleService {
  private apiUrl = `${environment.apiUrl}/api/pedagogical-schedule`;

  constructor(private http: HttpClient) {}

  getAllResources(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/ressources`);
  }

  getAllResourcesDTO(): Observable<ResourceScheduleDTO[]> {
    return this.http.get<ResourceScheduleDTO[]>(`${this.apiUrl}/ressources/dto`);
  }

  getResourceById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ressources/${id}`);
  }

  getResourcesByFilter(year: string, className: string, semester: string): Observable<ResourceScheduleDTO[]> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className)
      .set('semester', semester);

    return this.http.get<ResourceScheduleDTO[]>(`${this.apiUrl}/ressources/filter`, { params });
  }

  getCompleteSchedule(year: string, className: string, semester: string, program?: string): Observable<PedagogicalScheduleDTO> {
    let params = new HttpParams();
    if (year) params = params.set('year', year);
    if (className) params = params.set('className', className);
    if (semester) params = params.set('semester', semester);
    if (program) params = params.set('program', program);

    return this.http.get<PedagogicalScheduleDTO>(`${this.apiUrl}/schedule`, { params });
  }

  createResource(resource: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/ressources`, resource);
  }

  updateResource(id: number, resource: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/ressources/${id}`, resource);
  }

  updateResourceHours(id: number, updateDTO: UpdateHoursDTO): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/ressources/${id}/hours`, updateDTO);
  }

  validateSchedule(validationRequest: ValidationRequestDTO): Observable<ValidationResponseDTO> {
    return this.http.post<ValidationResponseDTO>(`${this.apiUrl}/schedule/validate`, validationRequest);
  }

  deleteResource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ressources/${id}`);
  }

  searchResources(keyword: string): Observable<ResourceScheduleDTO[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<ResourceScheduleDTO[]>(`${this.apiUrl}/ressources/search`, { params });
  }

  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}
