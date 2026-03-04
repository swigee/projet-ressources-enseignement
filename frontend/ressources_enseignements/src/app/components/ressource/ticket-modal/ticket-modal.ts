import { Component, inject, Input, Output, EventEmitter, PLATFORM_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { isPlatformBrowser } from '@angular/common';
import { TicketService, CreateTicketDTO } from '../../../services/ticket/ticket.service';

@Component({
  selector: 'app-ticket-modal',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './ticket-modal.html'
})
export class TicketModal {
  private readonly ticketService = inject(TicketService);
  private readonly platformId = inject(PLATFORM_ID);

  @Input() isOpen = false;
  @Output() closed = new EventEmitter<void>();

  ticketData: CreateTicketDTO = { title: '', description: '' };

  closeTicketModal(): void {
    this.ticketData = { title: '', description: '' };
    this.closed.emit();
  }

  submitTicket(): void {
    if (!this.ticketData.title || !this.ticketData.description) {
      alert('Veuillez remplir tous les champs obligatoires.');
      return;
    }
    if (!isPlatformBrowser(this.platformId)) return;

    const userId = Number(localStorage.getItem('userId'));
    if (!userId) {
      alert('Utilisateur non connecté.');
      return;
    }

    this.ticketService.createTicket(userId, this.ticketData).subscribe({
      next: () => {
        alert('Ticket envoyé avec succès !');
        this.closeTicketModal();
      },
      error: (err) => {
        console.error('Error submitting ticket', err);
        alert("Erreur lors de l'envoi du ticket.");
      }
    });
  }
}
