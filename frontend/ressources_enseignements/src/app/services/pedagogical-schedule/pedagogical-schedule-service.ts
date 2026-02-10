import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface WeekHoursDTO {
  cm: number;
  td: number;
  tp: number;
  total: number;
}

export interface RessourceScheduleDTO {
  id: number;
  courseName: string;
  category?: string;
  isHighlighted: boolean;
  hoursPerWeek: { [key: string]: WeekHoursDTO };
  hoursPerHalfGroup: number;
  totalHours: number;
  totalCM?: number;
  totalTD?: number;
  totalTP?: number;
}

export interface ProjectScheduleDTO {
  id: string;
  name: string;
  hoursPerWeek: { [key: string]: WeekHoursDTO };
  hoursPerHalfGroup: number;
  totalHours: number;
}

export interface WeekDTO {
  num: number;
  date: string;
  type: string; // 'E' or 'S'
}

export interface MonthDTO {
  month: string;
  weeks: WeekDTO[];
}

export interface ScheduleStatisticsDTO {
  totalResources: number;
  totalWithProject: number;
  companyWeeksCount: number;
  weeklyTotals: { [key: number]: number };
  weeklyTotalsWithProject: { [key: number]: number };
}

export interface PedagogicalScheduleDTO {
  selectedYear: string;
  selectedClass: string;
  selectedSemester: string;
  scheduleData: RessourceScheduleDTO[];
  projectData: ProjectScheduleDTO;
  weeks: MonthDTO[];
  statistics: ScheduleStatisticsDTO;
}

export interface UpdateHoursDTO {
  ressourceId: number;
  hoursPerWeek: { [key: string]: WeekHoursDTO };
  hoursPerHalfGroup: number;
}

export interface ValidationRequestDTO {
  selectedYear: string;
  selectedClass: string;
  selectedSemester: string;
  ressources: UpdateHoursDTO[];
  project: UpdateHoursDTO;
}

export interface ValidationResponseDTO {
  success: boolean;
  message: string;
  errors?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class PedagogicalScheduleService {
  private apiUrl = 'http://localhost:8080/api/pedagogical-schedule';

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

  getCompleteSchedule(year: string, className: string, semester: string): Observable<PedagogicalScheduleDTO> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className)
      .set('semester', semester);

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
