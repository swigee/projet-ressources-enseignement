import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private apiUrl = 'http://localhost:8080/api/tickets';

  constructor(private http: HttpClient) { }

  createTicket(ticket: { title: string, description: string, userId: number }): Observable<any> {
    return this.http.post(this.apiUrl, ticket);
  }

}
