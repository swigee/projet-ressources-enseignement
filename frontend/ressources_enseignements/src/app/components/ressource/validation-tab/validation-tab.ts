import { Component, inject, Input, OnInit, OnChanges, Output, EventEmitter, signal, computed, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { UserService } from '../../../services/user/user-service';
import { ServiceSheetService } from '../../../services/professor-service/service-sheet.service';
import { ServiceSummary } from '../../../models/service-summary.model';

@Component({
  selector: 'app-validation-tab',
  standalone: true,
  imports: [],
  templateUrl: './validation-tab.html'
})
export class ValidationTab implements OnInit, OnChanges {
  private readonly userService = inject(UserService);
  private readonly serviceSheetService = inject(ServiceSheetService);
  private readonly platformId = inject(PLATFORM_ID);

  @Input() selectedYear = '1';
  @Output() ticketRequested = new EventEmitter<void>();

  validationStatus = signal<string>('NONE');
  showValidationConfirm = signal<boolean>(false);
  services = signal<ServiceSummary[]>([]);

  filteredServices = computed(() =>
    this.services().filter(s => s.year === this.selectedYear)
  );

  ngOnInit(): void {
    this.loadValidationStatus();
    this.loadServices();
  }

  ngOnChanges(): void {
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

  requestValidation(): void {
    this.showValidationConfirm.set(true);
  }

  cancelValidation(): void {
    this.showValidationConfirm.set(false);
  }

  confirmValidation(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const userId = Number(localStorage.getItem('userId'));
    this.userService.validateService(userId).subscribe({
      next: () => {
        alert('Service validé avec succès !');
        this.validationStatus.set('SUBMITTED');
        this.showValidationConfirm.set(false);
      },
      error: (err) => {
        console.error('Error validating service', err);
        alert('Erreur lors de la validation du service.');
        this.showValidationConfirm.set(false);
      }
    });
  }
}
