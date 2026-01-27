import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RessourceScheduleDTO {
  id: number;
  courseName: string;
  category?: string;
  isHighlighted: boolean;
  hoursPerWeek: { [key: string]: number };
  hoursPerHalfGroup: { [key: string]: number };
  totalHours: number;
}

export interface ProjectScheduleDTO {
  id: string;
  name: string;
  hoursPerWeek: { [key: string]: number };
  hoursPerHalfGroup: { [key: string]: number };
  totalHours: number;
}

export interface WeekDTO {
  num: number;
  date: string;
  type: string; // 'E' ou 'S'
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
  scheduleData: RessourceScheduleDTO[];
  projectData: ProjectScheduleDTO;
  weeks: MonthDTO[];
  statistics: ScheduleStatisticsDTO;
}

export interface UpdateHoursDTO {
  ressourceId: number;
  hoursPerWeek: { [key: string]: number };
  hoursPerHalfGroup: { [key: string]: number };
}

export interface ValidationRequestDTO {
  selectedYear: string;
  selectedClass: string;
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

  // Récupérer toutes les ressources
  getAllRessources(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/ressources`);
  }

  // Récupérer toutes les ressources en DTO
  getAllRessourcesDTO(): Observable<RessourceScheduleDTO[]> {
    return this.http.get<RessourceScheduleDTO[]>(`${this.apiUrl}/ressources/dto`);
  }

  // Récupérer une ressource par ID
  getRessourceById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ressources/${id}`);
  }

  // Récupérer les ressources par année et classe
  getRessourcesByFilter(year: string, className: string): Observable<RessourceScheduleDTO[]> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className);

    return this.http.get<RessourceScheduleDTO[]>(`${this.apiUrl}/ressources/filter`, { params });
  }

  // Récupérer le planning complet
  getCompleteSchedule(year: string, className: string): Observable<PedagogicalScheduleDTO> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className);

    return this.http.get<PedagogicalScheduleDTO>(`${this.apiUrl}/schedule`, { params });
  }

  // Créer une nouvelle ressource
  createRessource(ressource: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/ressources`, ressource);
  }

  // Mettre à jour une ressource complète
  updateRessource(id: number, ressource: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/ressources/${id}`, ressource);
  }

  // Mettre à jour les heures d'une ressource
  updateRessourceHours(id: number, updateDTO: UpdateHoursDTO): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/ressources/${id}/hours`, updateDTO);
  }

  // Valider et sauvegarder un planning complet
  validateSchedule(validationRequest: ValidationRequestDTO): Observable<ValidationResponseDTO> {
    return this.http.post<ValidationResponseDTO>(`${this.apiUrl}/schedule/validate`, validationRequest);
  }

  // Supprimer une ressource
  deleteRessource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/ressources/${id}`);
  }

  // Rechercher des ressources par mot-clé
  searchRessources(keyword: string): Observable<RessourceScheduleDTO[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<RessourceScheduleDTO[]>(`${this.apiUrl}/ressources/search`, { params });
  }

  // Health check
  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}
