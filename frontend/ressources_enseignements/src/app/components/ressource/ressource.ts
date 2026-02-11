import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { RessourcesService } from '../../services/ressources/ressources-service';
import {
  RessourceRow, RessourcesTotals,
  ScheduleConflict,
  TeacherBadge
} from '../../models/ressources/ressources.model';
import {MonthDTO,WeekDTO,ValidationRequestDTO,WeekHoursDTO,UpdateHoursDTO,ValidationResponseDTO,ScheduleStatisticsDTO,RessourceScheduleDTO,PedagogicalScheduleDTO,ProjectScheduleDTO} from '../../models/schedule/schedule.model';
import {
  PedagogicalScheduleService
} from '../../services/pedagogical-schedule/pedagogical-schedule-service';
import { UserService } from '../../services/user/user-service';
import { Router } from '@angular/router';
import { PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, AsyncPipe, NgIf, NgFor } from '@angular/common';
import { TicketService, CreateTicketDTO } from '../../services/ticket/ticket.service';
import { ServiceSheetService } from '../../services/professor-service/service-sheet.service';
import { ServiceSummary } from '../../models/service-summary.model';


@Component({
  selector: 'app-ressource',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './ressource.html'
})
export class Ressource implements OnInit {
  private readonly ressourcesTableService = inject(RessourcesService);
  private readonly pedagogicalScheduleService = inject(PedagogicalScheduleService);
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);
  private readonly platformId = inject(PLATFORM_ID);
  private readonly serviceSheetService = inject(ServiceSheetService);
  private readonly ticketService = inject(TicketService);


  activeTab = signal<string>('ressources');


  validationStatus = signal<string>('NONE'); // NONE, SUBMITTED, VALIDATED
  showValidationConfirm = signal<boolean>(false);
  services = signal<ServiceSummary[]>([]);

  filteredServices = computed(() => {
    const allServices = this.services();
    const year = this.selectedYear();
    return allServices.filter(service => service.year === year);
  });

  showModal = false;
  ticketData: CreateTicketDTO = {
    title: '',
    description: ''
  };

  // Filter state
  searchQuery = signal<string>('');
  selectedTeacherIds = signal<number[]>([]);
  teacherSearchQuery = signal<string>('');
  showTeacherDropdown = signal<boolean>(false);
  selectedYear = signal<string>('1');
  selectedClass = signal<string>('Classe A');
  selectedSemester = signal<string>('1');

  // Data state
  ressources = signal<RessourceRow[]>([]);
  availableTeachers = signal<TeacherBadge[]>([]);
  conflicts = signal<ScheduleConflict[]>([]);
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  // Maquette-specific state
  scheduleData = signal<RessourceScheduleDTO[]>([]);
  projectData = signal<ProjectScheduleDTO>({
    id: 'project-sae', name: 'Projet SAE', hoursPerWeek: {}, hoursPerHalfGroup: 0, totalHours: 0
  });
  weeks = signal<MonthDTO[]>([]);
  isEditing = signal<boolean>(false);

  // Class data
  classData: Record<string, { name: string; classes: string[] }> = {
    '1': { name: 'Annee 1', classes: ['Classe A', 'Classe B'] },
    '2': { name: 'Annee 2', classes: ['Classe A', 'Classe B'] },
    '3': { name: 'Annee 3 (Alternance)', classes: ['Classe A', 'Classe B'] }
  };

  // Computed filtered data
  filteredRessources = computed(() => {
    let result = this.ressources();
    const query = this.searchQuery().toLowerCase();
    const selectedTeachers = this.selectedTeacherIds();

    // Apply search filter
    if (query) {
      result = result.filter(r =>
        r.moduleName.toLowerCase().includes(query) ||
        (r.category && r.category.toLowerCase().includes(query))
      );
    }

    // Apply teacher filter
    if (selectedTeachers.length > 0) {
      result = result.filter(r =>
        r.assignedTeachers.some(t => selectedTeachers.includes(t.teacherId))
      );
    }

    return result;
  });

  // Auto-updating totals
  calculatedTotals = computed<RessourcesTotals>(() => {
    const filtered = this.filteredRessources();
    return {
      totalPlannedHours: filtered.reduce((sum, r) => sum + r.plannedHours, 0),
      totalActualHours: filtered.reduce((sum, r) => sum + r.actualHours, 0),
      totalTDHours: filtered.reduce((sum, r) => sum + r.TDHours, 0),
      totalTPHours: filtered.reduce((sum, r) => sum + r.TPHours, 0),
      totalCMHours: filtered.reduce((sum, r) => sum + r.CMHours, 0)
    };
  });

  // Computed conflicts for filtered rows
  filteredConflicts = computed(() => {
    const selectedTeachers = this.selectedTeacherIds();
    if (selectedTeachers.length === 0) {
      return this.conflicts();
    }
    return this.conflicts().filter(c => selectedTeachers.includes(c.teacherId));
  });

  // Autocomplete suggestions for teacher search
  filteredTeacherSuggestions = computed(() => {
    const query = this.teacherSearchQuery().toLowerCase().trim();
    const selected = this.selectedTeacherIds();
    const teachers = this.availableTeachers();
    if (!query) return [];
    return teachers.filter(t =>
      t.fullName.toLowerCase().includes(query) && !selected.includes(t.teacherId)
    );
  });

  ngOnInit(): void {
    this.loadData();
    this.loadValidationStatus();
    this.loadServices();
  }

  loadData(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    forkJoin({
      ressourcesData: this.ressourcesTableService.getRessourcesTable(this.selectedYear(), this.selectedClass(), this.selectedSemester()),
      scheduleData: this.pedagogicalScheduleService.getCompleteSchedule(this.selectedYear(), this.selectedClass(), this.selectedSemester())
    }).subscribe({
      next: (data) => {
        // Ressources tab data
        this.ressources.set(data.ressourcesData.ressources);
        this.availableTeachers.set(data.ressourcesData.availableTeachers);
        this.conflicts.set(data.ressourcesData.conflicts);

        // Maquette tab data
        this.scheduleData.set(data.scheduleData.scheduleData);
        this.projectData.set(data.scheduleData.projectData);
        this.weeks.set(data.scheduleData.weeks);

        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Erreur lors du chargement:', error);
        this.errorMessage.set(error.message || 'Erreur lors du chargement des donnees');
        this.isLoading.set(false);
        this.initializeDefaultData();
      }
    });
  }

  initializeDefaultData(): void {
    this.ressources.set([]);
    this.availableTeachers.set([]);
    this.conflicts.set([]);
    this.scheduleData.set([]);
    this.projectData.set({
      id: 'project-sae', name: 'Projet SAE', hoursPerWeek: {}, hoursPerHalfGroup: 0, totalHours: 0
    });
    this.weeks.set([]);
  }

  setActiveTab(tab: string): void {
    // Cancel editing when switching tabs
    if (this.isEditing()) {
      this.cancelEditing();
    }
    this.activeTab.set(tab);
  }

  onYearChange(year: string): void {
    this.selectedYear.set(year);
    this.selectedClass.set(this.classData[year].classes[0]);
    this.selectedSemester.set('1');
    this.loadData();
  }

  onClassChange(): void {
    this.loadData();
  }

  onSemesterChange(semester: string): void {
    this.selectedSemester.set(semester);
    this.loadData();
  }

  getYearKeys(): string[] {
    return Object.keys(this.classData);
  }

  getAvailableClasses(): string[] {
    return this.classData[this.selectedYear()]?.classes || [];
  }

  toggleTeacherFilter(teacherId: number): void {
    const current = this.selectedTeacherIds();
    if (current.includes(teacherId)) {
      this.selectedTeacherIds.set(current.filter(id => id !== teacherId));
    } else {
      this.selectedTeacherIds.set([...current, teacherId]);
    }
  }

  isTeacherSelected(teacherId: number): boolean {
    return this.selectedTeacherIds().includes(teacherId);
  }

  clearFilters(): void {
    this.searchQuery.set('');
    this.selectedTeacherIds.set([]);
    this.teacherSearchQuery.set('');
    this.showTeacherDropdown.set(false);
  }

  onTeacherSearchInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.teacherSearchQuery.set(target.value);
    this.showTeacherDropdown.set(target.value.trim().length > 0);
  }

  selectTeacherFromDropdown(teacherId: number): void {
    const current = this.selectedTeacherIds();
    if (!current.includes(teacherId)) {
      this.selectedTeacherIds.set([...current, teacherId]);
    }
    this.teacherSearchQuery.set('');
    this.showTeacherDropdown.set(false);
  }

  removeTeacherFilter(teacherId: number): void {
    this.selectedTeacherIds.set(this.selectedTeacherIds().filter(id => id !== teacherId));
  }

  getTeacherName(teacherId: number): string {
    const teacher = this.availableTeachers().find(t => t.teacherId === teacherId);
    return teacher ? teacher.fullName : '';
  }

  closeTeacherDropdown(): void {
    setTimeout(() => this.showTeacherDropdown.set(false), 200);
  }

  hasConflict(ressource: RessourceRow): boolean {
    const conflictingModules = this.conflicts().flatMap(c => c.conflictingModules);
    return conflictingModules.includes(ressource.moduleName);
  }

  getConflictDetails(ressource: RessourceRow): ScheduleConflict | undefined {
    return this.conflicts().find(c => c.conflictingModules.includes(ressource.moduleName));
  }

  onSearchInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchQuery.set(target.value);
  }

  // Maquette editing methods
  enableEditing(): void {
    this.successMessage.set('');
    this.isEditing.set(true);
  }

  validateEditing(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    const ressourcesDTO: UpdateHoursDTO[] = this.scheduleData().map(row => ({
      ressourceId: row.id,
      hoursPerWeek: row.hoursPerWeek,
      hoursPerHalfGroup: row.hoursPerHalfGroup
    }));

    const projectDTO: UpdateHoursDTO = {
      ressourceId: 0,
      hoursPerWeek: this.projectData().hoursPerWeek,
      hoursPerHalfGroup: this.projectData().hoursPerHalfGroup
    };

    const validationRequest: ValidationRequestDTO = {
      selectedYear: this.selectedYear(),
      selectedClass: this.selectedClass(),
      selectedSemester: this.selectedSemester(),
      ressources: ressourcesDTO,
      project: projectDTO
    };

    this.pedagogicalScheduleService.validateSchedule(validationRequest)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.successMessage.set('Planning valide et sauvegarde avec succes !');
            setTimeout(() => this.successMessage.set(''), 5000);
            this.isEditing.set(false);
            this.loadData();
          } else {
            this.errorMessage.set(response.message);
            if (response.errors && response.errors.length > 0) {
              this.errorMessage.set('Erreurs detectees:\n' + response.errors.join('\n'));
            }
          }
          this.isLoading.set(false);
        },
        error: (error) => {
          console.error('Erreur lors de la validation:', error);
          this.errorMessage.set('Erreur lors de la sauvegarde du planning');
          this.isLoading.set(false);
        }
      });
  }

  cancelEditing(): void {
    this.successMessage.set('');
    this.isEditing.set(false);
    this.loadData();
  }

  // Maquette calculation methods
  getAllWeeks(): WeekDTO[] {
    return this.weeks().flatMap(month => month.weeks);
  }

  calculateTotalHours(row: RessourceScheduleDTO): number {
    if (!row.hoursPerWeek) return 0;
    return Object.values(row.hoursPerWeek).reduce((sum, w) => sum + (w.cm || 0) + (w.td || 0) + (w.tp || 0), 0);
  }

  calculateTotalCM(row: RessourceScheduleDTO): number {
    if (!row.hoursPerWeek) return 0;
    return Object.values(row.hoursPerWeek).reduce((sum, w) => sum + (w.cm || 0), 0);
  }

  calculateTotalTD(row: RessourceScheduleDTO): number {
    if (!row.hoursPerWeek) return 0;
    return Object.values(row.hoursPerWeek).reduce((sum, w) => sum + (w.td || 0), 0);
  }

  calculateTotalTP(row: RessourceScheduleDTO): number {
    if (!row.hoursPerWeek) return 0;
    return Object.values(row.hoursPerWeek).reduce((sum, w) => sum + (w.tp || 0), 0);
  }

  get maquetteTotalCM(): number {
    return this.scheduleData().reduce((s, r) => s + this.calculateTotalCM(r), 0);
  }

  get maquetteTotalTD(): number {
    return this.scheduleData().reduce((s, r) => s + this.calculateTotalTD(r), 0);
  }

  get maquetteTotalTP(): number {
    return this.scheduleData().reduce((s, r) => s + this.calculateTotalTP(r), 0);
  }

  calculateProjectTotal(): number {
    const project = this.projectData();
    if (!project?.hoursPerWeek) return 0;
    return Object.values(project.hoursPerWeek).reduce((sum, w) => sum + (w.cm || 0) + (w.td || 0) + (w.tp || 0), 0);
  }

  calculateWeekTotal(weekNum: number): number {
    return this.scheduleData().reduce((sum, row) => {
      const w = row.hoursPerWeek[weekNum.toString()];
      return sum + (w ? (w.cm || 0) + (w.td || 0) + (w.tp || 0) : 0);
    }, 0);
  }

  calculateWeekTotalWithProject(weekNum: number): number {
    const resourcesTotal = this.calculateWeekTotal(weekNum);
    const pw = this.projectData()?.hoursPerWeek[weekNum.toString()];
    return resourcesTotal + (pw ? (pw.cm || 0) + (pw.td || 0) + (pw.tp || 0) : 0);
  }

  calculateMaquetteGrandTotal(): number {
    return this.scheduleData().reduce((sum, row) => sum + this.calculateTotalHours(row), 0);
  }

  calculateGrandTotalWithProject(): number {
    return this.calculateMaquetteGrandTotal() + this.calculateProjectTotal();
  }

  getCompanyWeeksCount(): number {
    return this.getAllWeeks().filter(week => week.type === 'S').length;
  }

  // Update methods for signal-based inputs
  updateScheduleHoursByType(rowId: number, weekNum: number, type: 'cm' | 'td' | 'tp', event: Event): void {
    const value = Number((event.target as HTMLInputElement).value) || 0;
    const data = this.scheduleData();
    const updatedData = data.map(row => {
      if (row.id === rowId) {
        const current = row.hoursPerWeek[weekNum.toString()] || { cm: 0, td: 0, tp: 0, total: 0 };
        const updated = { ...current, [type]: value };
        updated.total = updated.cm + updated.td + updated.tp;
        return { ...row, hoursPerWeek: { ...row.hoursPerWeek, [weekNum.toString()]: updated } };
      }
      return row;
    });
    this.scheduleData.set(updatedData);
  }

  updateScheduleHalfGroup(rowId: number, event: Event): void {
    const target = event.target as HTMLInputElement;
    const value = Number(target.value) || 0;
    const data = this.scheduleData();
    const updatedData = data.map(row => {
      if (row.id === rowId) {
        return { ...row, hoursPerHalfGroup: value };
      }
      return row;
    });
    this.scheduleData.set(updatedData);
  }

  updateProjectHoursByType(weekNum: number, type: 'cm' | 'td' | 'tp', event: Event): void {
    const value = Number((event.target as HTMLInputElement).value) || 0;
    const project = this.projectData();
    const current = project.hoursPerWeek[weekNum.toString()] || { cm: 0, td: 0, tp: 0, total: 0 };
    const updated = { ...current, [type]: value };
    updated.total = updated.cm + updated.td + updated.tp;
    this.projectData.set({
      ...project,
      hoursPerWeek: { ...project.hoursPerWeek, [weekNum.toString()]: updated }
    });
  }

  getWeekTotal(hoursPerWeek: { [key: string]: WeekHoursDTO }, weekNum: number): number {
    const w = hoursPerWeek[weekNum.toString()];
    return w ? (w.cm || 0) + (w.td || 0) + (w.tp || 0) : 0;
  }

  updateProjectHalfGroup(event: Event): void {
    const target = event.target as HTMLInputElement;
    const value = Number(target.value) || 0;
    const project = this.projectData();
    this.projectData.set({ ...project, hoursPerHalfGroup: value });
  }

  // Validation methods
  loadValidationStatus(): void {
    if (isPlatformBrowser(this.platformId)) {
      const userId = Number(localStorage.getItem('userId'));
      if (userId) {
        this.userService.getUserById(userId).subscribe({
          next: (user) => {
            if (user.validationStatus) {
              this.validationStatus.set(user.validationStatus);
            }
          },
          error: (err) => console.error('Error loading validation status', err)
        });
      }
    }
  }

  loadServices(): void {
    if (isPlatformBrowser(this.platformId)) {
      const userId = Number(localStorage.getItem('userId'));
      if (userId) {
        this.serviceSheetService.getServicesSheet(userId).subscribe({
          next: (data) => this.services.set(data),
          error: (err) => console.error('Error loading services', err)
        });
      }
    }
  }

  requestValidation(): void {
    this.showValidationConfirm.set(true);
  }

  cancelValidation(): void {
    this.showValidationConfirm.set(false);
  }

  confirmValidation(): void {
    if (isPlatformBrowser(this.platformId)) {
      const userId = Number(localStorage.getItem('userId'));
      this.userService.validateService(userId).subscribe({
        next: () => {
          alert("Service validé avec succès !");
          this.validationStatus.set('SUBMITTED');
          this.showValidationConfirm.set(false);
        },
        error: (err) => {
          console.error("Erreur validation service", err);
          alert("Erreur lors de la validation du service.");
          this.showValidationConfirm.set(false);
        }
      });
    }
  }

  // Ticket Methods
  openTicketModal() {
    this.showModal = true;
  }

  closeTicketModal() {
    this.showModal = false;
    this.ticketData = { title: '', description: '' };
  }

  submitTicket() {
    if (!this.ticketData.title || !this.ticketData.description) {
      alert("Veuillez remplir tous les champs obligatoires.");
      return;
    }

    if (isPlatformBrowser(this.platformId)) {
      const userId = Number(localStorage.getItem('userId'));
      if (!userId) {
        alert("Utilisateur non connecté.");
        return;
      }

      this.ticketService.createTicket(userId, this.ticketData).subscribe({
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
}
