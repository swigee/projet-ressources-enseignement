import { Component, inject, OnInit, signal, computed, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user/user-service';
import { ServiceSheetService } from '../../services/professor-service/service-sheet.service';
import { ServiceSummary } from '../../models/service-summary.model';
import { PageTitle } from '../../services/page-title/page-title-service';

@Component({
  selector: 'app-service-validation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './service-validation.html'
})
export class ServiceValidation implements OnInit {
  private readonly userService = inject(UserService);
  private readonly serviceSheetService = inject(ServiceSheetService);
  private readonly platformId = inject(PLATFORM_ID);
  private readonly pageTitle = inject(PageTitle);

  selectedYear = signal<string>('1');
  validationStatus = signal<string>('NONE');
  showValidationConfirm = signal<boolean>(false);
  validationCommentValue = '';
  services = signal<ServiceSummary[]>([]);

  years = [
    { value: '1', label: 'Année 1' },
    { value: '2', label: 'Année 2' },
    { value: '3', label: 'Année 3' }
  ];

  filteredServices = computed(() =>
    this.services().filter(s => s.year === this.selectedYear())
  );

  ngOnInit(): void {
    this.pageTitle.title.set("Validation des services");
    this.loadValidationStatus();
    this.loadServices();
  }

  loadValidationStatus(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const userId = Number(localStorage.getItem('userId'));
    if (!userId) return;
    this.userService.getUserById(userId).subscribe({
      next: (user) => { if (user.validationStatus) this.validationStatus.set(user.validationStatus); },
      error: (err) => console.error('Error loading validation status', err)
    });
  }

  loadServices(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const userId = Number(localStorage.getItem('userId'));
    if (!userId) return;
    this.serviceSheetService.getServicesSheet(userId).subscribe({
      next: (data) => this.services.set(data),
      error: (err) => console.error('Error loading services', err)
    });
  }

  onYearChange(year: string): void {
    this.selectedYear.set(year);
  }

  requestValidation(): void {
    this.showValidationConfirm.set(true);
  }

  cancelValidation(): void {
    this.showValidationConfirm.set(false);
    this.validationCommentValue = '';
  }

  confirmValidation(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const userId = Number(localStorage.getItem('userId'));
    this.userService.validateService(userId, this.validationCommentValue).subscribe({
      next: () => {
        alert('Service validé avec succès !');
        this.validationStatus.set('SUBMITTED');
        this.showValidationConfirm.set(false);
        this.validationCommentValue = '';
      },
      error: (err) => {
        console.error('Error validating service', err);
        alert('Erreur lors de la validation du service.');
        this.showValidationConfirm.set(false);
      }
    });
  }
}
