import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { ServiceSummary } from "../../models/service-summary.model";

@Injectable({
  providedIn: 'root'
})
export class ServiceSheetService {
  private apiUrl = 'http://localhost:8080/api/services';

  constructor(private http: HttpClient) { }

  // On récupère la liste pour un prof donné
  getServicesSheet(userId: number): Observable<ServiceSummary[]> {
    return this.http.get<ServiceSummary[]>(`${this.apiUrl}/${userId}`);
  }
}
