import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface CreateTicketDTO {
  title: string;
  description: string;
}

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private apiUrl = 'http://localhost:8080/api/tickets';

  constructor(private http: HttpClient) { }

  createTicket(userId: number, ticket: CreateTicketDTO): Observable<any> {
    return this.http.post(this.apiUrl, { ...ticket, userId });
  }

}
