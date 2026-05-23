import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AssignmentGrid,TeacherAssignment,Teacher,AssignmentStatistics,CreateAssignment,AssignmentValidationResponse,AffectationRow} from '../../models/teacher/teacher.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TeacherAssignmentService {
  private apiUrl = `${environment.apiUrl}/api/teacher-assignment`;
  private educationApiUrl = `${environment.apiUrl}/api/education-manager`;

  constructor(private http: HttpClient) {}

  getAllTeachers(): Observable<Teacher[]> {
    return this.http.get<Teacher[]>(`${this.apiUrl}/teachers`);
  }

  searchTeachers(keyword: string): Observable<Teacher[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<Teacher[]>(`${this.apiUrl}/teachers/search`, { params });
  }

  getAvailableClasses(year?: string, formation?: string): Observable<string[]> {
    let params = new HttpParams();
    if (year) params = params.set('year', year);
    if (formation) params = params.set('formation', formation);
    return this.http.get<string[]>(`${this.educationApiUrl}/classes`, { params });
  }

  getAssignmentGrid(formation: string, year: string, className: string, semester?: string): Observable<AssignmentGrid> {
    let params = new HttpParams();
    if (formation) params = params.set('formation', formation);
    if (year) params = params.set('year', year);
    if (className) params = params.set('className', className);
    if (semester) params = params.set('semester', semester);

    return this.http.get<AssignmentGrid>(`${this.apiUrl}/grid`, { params });
  }

  createAssignment(dto: CreateAssignment): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/assignments`, dto);
  }

  updateAssignment(id: number, dto: CreateAssignment): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/assignments/${id}`, dto);
  }

  deleteAssignment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/assignments/${id}`);
  }

  deleteAssignmentByTeacherAndRessource(userId: number, ressourceId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/assignments/teacher/${userId}/ressource/${ressourceId}`);
  }

  validateAssignments(year: string, className: string): Observable<AssignmentValidationResponse> {
    const params = new HttpParams()
      .set('year', year)
      .set('className', className);

    return this.http.post<AssignmentValidationResponse>(`${this.apiUrl}/validate`, null, { params });
  }

  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' });
  }
}
