import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketService, TicketResponseDTO } from '../../services/ticket/ticket.service';

@Component({
  selector: 'app-ticket-manager',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ticket-manager.html',
  styleUrl: './ticket-manager.css',
})
export class TicketManager implements OnInit {
  tickets: TicketResponseDTO[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private ticketService: TicketService) { }

  ngOnInit(): void {
    this.loadTickets();
  }

  loadTickets(): void {
    this.isLoading = true;
    this.ticketService.getTickets().subscribe({
      next: (data) => {
        this.tickets = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des tickets:', err);
        this.errorMessage = 'Impossible de charger les tickets.';
        this.isLoading = false;
      }
    });
  }

  updateStatus(ticket: TicketResponseDTO, newStatus: string): void {
    ticket.status = newStatus;
    this.ticketService.updateTicketStatus(ticket.id, newStatus).subscribe({
      next: () => {
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour:', err);
        this.errorMessage = 'Erreur lors de la mise à jour du statut.';
        this.loadTickets();
      }
    });
  }
}
