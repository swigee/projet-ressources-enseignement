import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CreateTicketDTO {
  title: string;
  description: string;
}

export interface TicketResponseDTO {
  id: number;
  title: string;
  description: string;
  date: string;
  resolutionDate?: string;
  status: string;
  userId: number;
  userName: string;
}

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private apiUrl = `${environment.apiUrl}/api/tickets`;

  constructor(private http: HttpClient) { }

  createTicket(userId: number, ticket: CreateTicketDTO): Observable<any> {
    return this.http.post(this.apiUrl, { ...ticket, userId });
  }

  getTickets(): Observable<TicketResponseDTO[]> {
    return this.http.get<TicketResponseDTO[]>(this.apiUrl);
  }

  updateTicketStatus(ticketId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${ticketId}/status`, { status });
  }
}
