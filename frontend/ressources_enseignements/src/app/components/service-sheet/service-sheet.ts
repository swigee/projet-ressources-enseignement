import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Observable, of } from 'rxjs';
import { Router } from '@angular/router'; // <--- Import Router pour rediriger si pas connecté
import { ServiceSummary } from '../../models/service-summary.model';
import { ServiceSheetService } from "../../services/professor-service/service-sheet.service";
import { TicketService } from "../../services/ticket/ticket.service";
import { UserService } from "../../services/user/user-service";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-service-sheet',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './service-sheet.html',
  styleUrl: './service-sheet.css',
})
export class ServiceSheet implements OnInit {

  services$: Observable<ServiceSummary[]> | undefined;

  showModal = false;
  ticketData = {
    title: '',
    description: ''
  };

  validationStatus: string = 'NONE'; // NONE, SUBMITTED, VALIDATED

  constructor(
    private serviceSheetService: ServiceSheetService,
    private ticketService: TicketService,
    private userService: UserService, // Inject UserService
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {


      const storedId = localStorage.getItem('userId');

      if (!storedId) {
        this.router.navigate(['/login']);
        return;
      }
      // const userRole = localStorage.getItem('userRole'); // pour vérifier si c'est bien un prof


      const userId = Number(storedId);

      // 4. (Optionnel) Vérification du rôle
      // Si tu veux bloquer l'accès aux étudiants, tu peux décommenter ça :
      /*
      if (userRole !== 'TEACHER') {
         alert("Accès réservé aux enseignants !");
         this.router.navigate(['/dashboard']);
         return;
      }
      */

      // 5. On lance la requête avec le VRAI ID
      console.log("Chargement des services pour l'utilisateur ID :", userId);
      this.services$ = this.serviceSheetService.getServicesSheet(userId);

      // Check validation status
      this.userService.getUserById(userId).subscribe(user => {
        this.validationStatus = user.validationStatus;
      });
    }
  }

  openTicketModal() {
    this.showModal = true;
  }

  closeTicketModal() {
    this.showModal = false;
    // On vide le formulaire
    this.ticketData = { title: '', description: '' };
  }

  submitTicket() {
    if (isPlatformBrowser(this.platformId)) {
      const userId = Number(localStorage.getItem('userId'));

      const newTicket = {
        title: this.ticketData.title,
        description: this.ticketData.description,
        userId: userId
      };

      this.ticketService.createTicket(newTicket).subscribe({
        next: () => {
          alert("Ticket envoyé avec succès !");
          this.closeTicketModal();
        },
        error: (err) => {
          console.error("Erreur envoi ticket", err);
          alert("Erreur lors de l'envoi du ticket.");
        }
      });
    }
  }

  showValidationConfirm = false;

  requestValidation() {
    console.log("Requesting validation...");
    this.showValidationConfirm = true;
  }

  cancelValidation() {
    this.showValidationConfirm = false;
  }

  validateService() {
    if (isPlatformBrowser(this.platformId)) {
      const userId = Number(localStorage.getItem('userId'));
      this.userService.validateService(userId).subscribe({
        next: () => {
          alert("Service validé avec succès !");
          this.validationStatus = 'SUBMITTED';
          this.showValidationConfirm = false;
        },
        error: (err) => {
          console.error("Erreur validation service", err);
          alert("Erreur lors de la validation du service.");
          this.showValidationConfirm = false;
        }
      });
    }
  }
}
