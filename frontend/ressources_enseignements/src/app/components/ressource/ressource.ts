import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { RessourcesService } from '../../services/ressources/ressources-service';
import { RessourceRow, RessourcesTotals, ScheduleConflict, TeacherBadge } from '../../models/ressources/ressources.model';
import { MonthDTO, RessourceScheduleDTO, ProjectScheduleDTO, UpdateHoursDTO, ValidationRequestDTO, WeekHoursDTO } from '../../models/schedule/schedule.model';
import { PedagogicalScheduleService } from '../../services/pedagogical-schedule/pedagogical-schedule-service';
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { FilterControls } from './filter-controls/filter-controls';
import { ResourcesTab } from './resources-tab/resources-tab';
import { MaquetteTab, ScheduleHoursDelta, ProjectHoursDelta } from './maquette-tab/maquette-tab';
import {PageTitle} from '../../services/page-title/page-title-service';

@Component({
  selector: 'app-ressource',
  standalone: true,
  imports: [FilterControls, ResourcesTab, MaquetteTab],
  templateUrl: './ressource.html'
})
export class Ressource implements OnInit {
  private readonly ressourcesTableService = inject(RessourcesService);
  private readonly pedagogicalScheduleService = inject(PedagogicalScheduleService);
  private readonly educationManagerService = inject(EducationalManagerService);

  activeTab = signal<string>('ressources');
  selectedFormation = signal<string>('');
  formations = signal<{ id: string; name: string }[]>([]);
  selectedYear = signal<string>('');
  selectedClass = signal<string>('');
  availableClasses = signal<string[]>([]);
  selectedSemester = signal<string>('');

  ressources = signal<RessourceRow[]>([]);
  availableTeachers = signal<TeacherBadge[]>([]);
  conflicts = signal<ScheduleConflict[]>([]);
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  scheduleData = signal<RessourceScheduleDTO[]>([]);
  projectData = signal<ProjectScheduleDTO>({
    id: 'project-sae', name: 'Projet SAE', hoursPerWeek: {}, hoursPerHalfGroup: 0, totalHours: 0
  });
  weeks = signal<MonthDTO[]>([]);
  isEditing = signal<boolean>(false);

  searchQuery = signal<string>('');
  selectedTeacherIds = signal<number[]>([]);

  showModal = signal<boolean>(false);

  classData: Record<string, { name: string }> = {
    '1': { name: 'Année 1' },
    '2': { name: 'Année 2' },
    '3': { name: 'Année 3' }
  };

  filteredRessources = computed<RessourceRow[]>(() => {
    let result = this.ressources();
    const query = this.searchQuery().toLowerCase();
    const selectedTeachers = this.selectedTeacherIds();
    if (query) {
      result = result.filter(r =>
        r.moduleName.toLowerCase().includes(query) ||
        (r.category && r.category.toLowerCase().includes(query))
      );
    }
    if (selectedTeachers.length > 0) {
      result = result.filter(r => r.assignedTeachers.some(t => selectedTeachers.includes(t.teacherId)));
    }
    return result;
  });

  calculatedTotals = computed<RessourcesTotals>(() => {
    const filtered = this.filteredRessources();
    return {
      totalPlannedHours: filtered.reduce((sum, r) => sum + r.plannedHours, 0),
      totalActualHours: filtered.reduce((sum, r) => sum + r.actualHours, 0),
      totalTDHours: filtered.reduce((sum, r) => sum + r.tdHours, 0),
      totalTPHours: filtered.reduce((sum, r) => sum + r.tpHours, 0),
      totalCMHours: filtered.reduce((sum, r) => sum + r.cmHours, 0)
    };
  });

  filteredConflicts = computed<ScheduleConflict[]>(() => {
    const selected = this.selectedTeacherIds();
    if (selected.length === 0) return this.conflicts();
    return this.conflicts().filter(c => selected.includes(c.teacherId));
  });

  ngOnInit(): void {
    this.pageTitle.title.set("Gestion pédagogique - Ressources et plannings");
    this.loadFormations();
  }

  constructor(private pageTitle: PageTitle) {
  }

  loadFormations(): void {
    this.educationManagerService.loadDistinctFormationNames().subscribe({
      next: (names) => {
        this.formations.set(names.map(n => ({ id: n, name: n })));
        if (!this.selectedFormation() && names.length > 0) {
          this.selectedFormation.set(names[0]);
        }
        this.loadClasses();
        this.loadData();
      },
      error: () => {}
    });
  }

  loadClasses(): void {
    this.educationManagerService.getDistinctClasses(
      this.selectedYear(),
      this.selectedFormation()
    ).subscribe({
      next: (classes) => {
        this.availableClasses.set(classes);
        if (this.selectedClass() && !classes.includes(this.selectedClass())) {
          this.selectedClass.set(classes[0] || '');
        }
      },
      error: () => this.availableClasses.set([])
    });
  }

  loadData(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');
    forkJoin({
      ressourcesData: this.ressourcesTableService.getRessourcesTable(this.selectedYear(), this.selectedClass(), this.selectedSemester(), this.selectedFormation()),
      scheduleData: this.pedagogicalScheduleService.getCompleteSchedule(this.selectedYear(), this.selectedClass(), this.selectedSemester(), this.selectedFormation())
    }).subscribe({
      next: (data) => {
        console.log(data);
        this.ressources.set(data.ressourcesData.ressources);
        this.availableTeachers.set(data.ressourcesData.availableTeachers);
        this.conflicts.set(data.ressourcesData.conflicts);
        this.scheduleData.set(data.scheduleData.scheduleData);
        this.projectData.set(data.scheduleData.projectData);
        this.weeks.set(data.scheduleData.weeks);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error loading data:', error);
        this.errorMessage.set(error.message || 'Erreur lors du chargement des données');
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
    this.projectData.set({ id: 'project-sae', name: 'Projet SAE', hoursPerWeek: {}, hoursPerHalfGroup: 0, totalHours: 0 });
    this.weeks.set([]);
  }

  setActiveTab(tab: string): void {
    if (this.isEditing()) this.cancelEditing();
    this.activeTab.set(tab);
  }

  onFormationChange(formation: string): void {
    this.selectedFormation.set(formation);
    this.loadClasses();
    this.loadData();
  }

  onYearChange(year: string): void {
    this.selectedYear.set(year);
    this.selectedSemester.set('');
    this.loadClasses();
    this.loadData();
  }

  onClassChange(cls: string): void {
    this.selectedClass.set(cls);
    this.loadData();
  }

  onSemesterChange(semester: string): void {
    this.selectedSemester.set(semester);
    this.loadData();
  }

  toggleTeacherFilter(teacherId: number): void {
    const current = this.selectedTeacherIds();
    this.selectedTeacherIds.set(
      current.includes(teacherId) ? current.filter(id => id !== teacherId) : [...current, teacherId]
    );
  }

  removeTeacherFilter(teacherId: number): void {
    this.selectedTeacherIds.set(this.selectedTeacherIds().filter(id => id !== teacherId));
  }

  clearFilters(): void {
    this.searchQuery.set('');
    this.selectedTeacherIds.set([]);
  }

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

    const validationRequest: ValidationRequestDTO = {
      selectedYear: this.selectedYear(),
      selectedClass: this.selectedClass(),
      selectedSemester: this.selectedSemester(),
      ressources: ressourcesDTO,
      project: { ressourceId: 0, hoursPerWeek: this.projectData().hoursPerWeek, hoursPerHalfGroup: this.projectData().hoursPerHalfGroup }
    };

    this.pedagogicalScheduleService.validateSchedule(validationRequest).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage.set('Planning validé et sauvegardé avec succès !');
          setTimeout(() => this.successMessage.set(''), 5000);
          this.isEditing.set(false);
          this.loadData();
        } else {
          this.errorMessage.set(response.errors?.length ? 'Erreurs détectées:\n' + response.errors.join('\n') : response.message);
        }
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error validating:', error);
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

  onScheduleHoursChanged(delta: ScheduleHoursDelta): void {
    const updatedData = this.scheduleData().map(row => {
      if (row.id !== delta.rowId) return row;
      const current = row.hoursPerWeek[delta.weekNum.toString()] || { cm: 0, td: 0, tp: 0, total: 0 };
      const updated = { ...current, [delta.type]: delta.value };
      updated.total = updated.cm + updated.td + updated.tp;
      return { ...row, hoursPerWeek: { ...row.hoursPerWeek, [delta.weekNum.toString()]: updated } };
    });
    this.scheduleData.set(updatedData);
  }

  onProjectHoursChanged(delta: ProjectHoursDelta): void {
    const project = this.projectData();
    const current = project.hoursPerWeek[delta.weekNum.toString()] || { cm: 0, td: 0, tp: 0, total: 0 };
    const updated = { ...current, [delta.type]: delta.value };
    updated.total = updated.cm + updated.td + updated.tp;
    this.projectData.set({ ...project, hoursPerWeek: { ...project.hoursPerWeek, [delta.weekNum.toString()]: updated } });
  }
}
