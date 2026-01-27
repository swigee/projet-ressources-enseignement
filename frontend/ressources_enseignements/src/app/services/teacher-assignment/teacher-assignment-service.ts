import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TeacherDTO {
  id: number;
  name: string;
  firstName: string;
  lastName: string;
  subject: string;
  status: string;
  totalHours: number;
  remainingHours: number;
  statusColor: string;
  roles?: string[];
}

export interface TeacherAssignmentDTO {
  assignmentId: number;
  teacherId: number;
  teacherName: string;
  lessonType: string;
  assignedHours: number;
}

export interface AffectationRowDTO {
  ressourceId: number;
  module: string;
  td: string;
  tp: string;
  cm: string;
  tdHours?: number;
  tpHours?: number;
  cmHours?: number;
  tdTeachers: TeacherAssignmentDTO[];
  tpTeachers: TeacherAssignmentDTO[];
  cmTeachers: TeacherAssignmentDTO[];
}


export interface AssignmentStatisticsDTO {
  totalTeachers: number;
  totalAssignments: number;
  totalHoursAssigned: number;
  unassignedModules: number;
  hoursByLessonType: { [key: string]: number };
  hoursByTeacher: { [key: string]: number };
}

export interface AssignmentGridDTO {
  selectedFormation: string;
  selectedYear: string;
  selectedSemester?: string;
  availableTeachers: TeacherDTO[];
  affectationGrid: AffectationRowDTO[];
  statistics: AssignmentStatisticsDTO;
}

export interface CreateAssignmentDTO {
  userId: number;
  ressourceId: number;
  lessonType: string;
  assignedTimes: number;
}

export interface AssignmentValidationResponseDTO {
  success: boolean;
  message: string;
  errors?: string[];
  warnings?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class TeacherAssignmentService {
  private apiUrl = 'http://localhost:8080/api/teacher-assignment';

  constructor(private http: HttpClient) {}

  // Récupérer tous les enseignants
  getAllTeachers(): Observable<TeacherDTO[]> {
    return this.http.get<TeacherDTO[]>(`${this.apiUrl}/teachers`);
  }

  // Rechercher des enseignants
  searchTeachers(keyword: string): Observable<TeacherDTO[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<TeacherDTO[]>(`${this.apiUrl}/teachers/search`, { params });
  }

  // Récupérer la grille d'affectation
  getAssignmentGrid(formation: string, year: string, className: string): Observable<AssignmentGridDTO> {
    const params = new HttpParams()
      .set('formation', formation)
      .set('year', year)
      .set('className', className);

    return this.http.get<AssignmentGridDTO>(`${this.apiUrl}/grid`, { params });
  }

  // Créer une affectation
  createAssignment(dto: CreateAssignmentDTO): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/assignments`, dto);
  }

  // Mettre à jour une affectation
  updateAssignment(id: number, dto: CreateAssignmentDTO): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/assignments/${id}`, dto);
  }

  // Supprimer une affectation
  deleteAssignment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/assignments/${id}`);
  }

  // Supprimer une affectation par enseignant et ressource
  deleteAssignmentByTeacherAndRessource(userId: number, ressourceId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/assignments/teacher/${userId}/ressource/${ressourceId}`);
  }

  // Valider les affectations
  validateAssignments(year: string, className: string): Observable<AssignmentValidationResponseDTO> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className);

    return this.http.post<AssignmentValidationResponseDTO>(`${this.apiUrl}/validate`, null, { params });
  }

  // Health check
  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}
