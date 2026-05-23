import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SoftwareModel } from '../../models/software/software.model';

@Injectable({ providedIn: 'root' })
export class SoftwareService {
  private readonly apiUrl = 'http://localhost:8080/api/software';
  private readonly http = inject(HttpClient);

  getAll(): Observable<SoftwareModel[]> {
    return this.http.get<SoftwareModel[]>(this.apiUrl);
  }

  getByResource(resourceId: number): Observable<SoftwareModel[]> {
    return this.http.get<SoftwareModel[]>(`${this.apiUrl}/resource/${resourceId}`);
  }

  create(dto: Record<string, unknown>): Observable<SoftwareModel> {
    return this.http.post<SoftwareModel>(this.apiUrl, dto);
  }

  update(id: number, dto: Record<string, unknown>): Observable<SoftwareModel> {
    return this.http.put<SoftwareModel>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getResourceTitles(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/resources/titles`);
  }
}
