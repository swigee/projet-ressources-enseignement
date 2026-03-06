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

  // Pagination state for resolved tickets
  resolvedCurrentPage = 1;
  resolvedPageSize = 5;

  constructor(private ticketService: TicketService) { }

  get activeTickets(): TicketResponseDTO[] {
    return this.tickets.filter(t => t.status !== 'RESOLVED');
  }

  get allResolvedTickets(): TicketResponseDTO[] {
    return this.tickets.filter(t => t.status === 'RESOLVED');
  }

  get paginatedResolvedTickets(): TicketResponseDTO[] {
    const startIndex = (this.resolvedCurrentPage - 1) * this.resolvedPageSize;
    return this.allResolvedTickets.slice(startIndex, startIndex + this.resolvedPageSize);
  }

  get totalResolvedItems(): number {
    return this.allResolvedTickets.length;
  }

  get hasNextResolvedPage(): boolean {
    return this.resolvedCurrentPage * this.resolvedPageSize < this.totalResolvedItems;
  }

  get hasPrevResolvedPage(): boolean {
    return this.resolvedCurrentPage > 1;
  }

  nextResolvedPage(): void {
    if (this.hasNextResolvedPage) {
      this.resolvedCurrentPage++;
    }
  }

  prevResolvedPage(): void {
    if (this.hasPrevResolvedPage) {
      this.resolvedCurrentPage--;
    }
  }

  get startIndexDisplay(): number {
    if (this.totalResolvedItems === 0) return 0;
    return (this.resolvedCurrentPage - 1) * this.resolvedPageSize + 1;
  }

  get endIndexDisplay(): number {
    return Math.min(this.resolvedCurrentPage * this.resolvedPageSize, this.totalResolvedItems);
  }

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
