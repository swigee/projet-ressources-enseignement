import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { VacataireModel } from '../../models/vacataire/vacataire.model';

@Injectable({ providedIn: 'root' })
export class VacataireService {
  private readonly apiUrl = `${environment.apiUrl}/api/vacataires`;
  private readonly http = inject(HttpClient);

  getAll(): Observable<VacataireModel[]> {
    return this.http.get<VacataireModel[]>(this.apiUrl);
  }

  getById(id: number): Observable<VacataireModel> {
    return this.http.get<VacataireModel>(`${this.apiUrl}/${id}`);
  }

  create(dto: VacataireModel): Observable<VacataireModel> {
    return this.http.post<VacataireModel>(this.apiUrl, dto);
  }

  update(id: number, dto: VacataireModel): Observable<VacataireModel> {
    return this.http.put<VacataireModel>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  search(keyword: string): Observable<VacataireModel[]> {
    return this.http.get<VacataireModel[]>(`${this.apiUrl}/search`, { params: { keyword } });
  }

  /** Returns only contractors that already have an active user account. */
  getActive(): Observable<VacataireModel[]> {
    return this.http.get<VacataireModel[]>(`${this.apiUrl}/active`);
  }

  /** Returns only contractors still in the recruitment pipeline (no account). */
  getPending(): Observable<VacataireModel[]> {
    return this.http.get<VacataireModel[]>(`${this.apiUrl}/pending`);
  }

  /**
   * Manually converts a contractor profile to a user account with custom credentials.
   * For most cases the account is created automatically when statut → VALIDE.
   */
  convertToUser(id: number, username: string, password: string): Observable<VacataireModel> {
    return this.http.post<VacataireModel>(`${this.apiUrl}/${id}/convert`, { username, password });
  }
}
