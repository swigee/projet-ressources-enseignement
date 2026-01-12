import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  PedagogicalScheduleService,
  PedagogicalScheduleDTO,
  RessourceScheduleDTO,
  ProjectScheduleDTO,
  MonthDTO,
  WeekDTO,
  ValidationRequestDTO,
  UpdateHoursDTO
} from '../../services/pedagogical-schedule/pedagogical-schedule-service';

@Component({
  selector: 'app-pedagogical-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pedagogical-schedule.html',
  styleUrls: ['./pedagogical-schedule.css']
})
export class PedagogicalScheduleComponent implements OnInit {
  activeTab: string = 'maquette';
  selectedYear: string = '1';
  selectedClass: string = 'Classe A';
  isEditing: boolean = false;
  isLoading: boolean = false;
  errorMessage: string = '';

  scheduleData: RessourceScheduleDTO[] = [];
  projectData!: ProjectScheduleDTO;
  weeks: MonthDTO[] = [];

  // Données des classes (peut être aussi chargé depuis l'API)
  classData: any = {
    '1': { name: 'Année 1', classes: ['Classe A', 'Classe B'] },
    '2': { name: 'Année 2', classes: ['Classe A', 'Classe B'] },
    '3': { name: 'Année 3 (Alternance)', classes: ['Classe A', 'Classe B'] }
  };

  constructor(private scheduleService: PedagogicalScheduleService) {}

  ngOnInit(): void {
    this.loadScheduleData();
  }

  // Charger les données du planning
  loadScheduleData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.scheduleService.getCompleteSchedule(this.selectedYear, this.selectedClass)
      .subscribe({
        next: (data: PedagogicalScheduleDTO) => {
          this.scheduleData = data.scheduleData;
          this.projectData = data.projectData;
          this.weeks = data.weeks;
          this.isLoading = false;
          console.log('Données chargées:', data);
        },
        error: (error) => {
          console.error('Erreur lors du chargement:', error);
          this.errorMessage = 'Erreur lors du chargement des données';
          this.isLoading = false;

          // Données par défaut en cas d'erreur
          this.initializeDefaultData();
        }
      });
  }

  // Initialiser les données par défaut (fallback)
  initializeDefaultData(): void {
    this.scheduleData = [];
    this.projectData = {
      id: 'project-sae',
      name: 'Projet SAE',
      hoursPerWeek: {},
      hoursPerHalfGroup: {},
      totalHours: 0
    };
    this.weeks = [];
  }

  // Gestion des onglets
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  // Changement d'année
  onYearChange(year: string): void {
    this.selectedYear = year;
    this.selectedClass = this.classData[year].classes[0];
    this.loadScheduleData();
  }

  // Obtenir les clés des années
  getYearKeys(): string[] {
    return Object.keys(this.classData);
  }

  // Obtenir les classes disponibles
  getAvailableClasses(): string[] {
    return this.classData[this.selectedYear]?.classes || [];
  }

  // Activer le mode édition
  enableEditing(): void {
    this.isEditing = true;
  }

  // Valider et sauvegarder les modifications
  validateEditing(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Préparer les données pour la validation
    const ressourcesDTO: UpdateHoursDTO[] = this.scheduleData.map(row => ({
      ressourceId: row.id,
      hoursPerWeek: row.hoursPerWeek,
      hoursPerHalfGroup: row.hoursPerHalfGroup
    }));

    const projectDTO: UpdateHoursDTO = {
      ressourceId: 0, // Le projet n'a pas d'ID de ressource
      hoursPerWeek: this.projectData.hoursPerWeek,
      hoursPerHalfGroup: this.projectData.hoursPerHalfGroup
    };

    const validationRequest: ValidationRequestDTO = {
      selectedYear: this.selectedYear,
      selectedClass: this.selectedClass,
      ressources: ressourcesDTO,
      project: projectDTO
    };

    this.scheduleService.validateSchedule(validationRequest)
      .subscribe({
        next: (response) => {
          if (response.success) {
            alert('Planning validé et sauvegardé avec succès !');
            this.isEditing = false;
            this.loadScheduleData(); // Recharger les données
          } else {
            this.errorMessage = response.message;
            if (response.errors && response.errors.length > 0) {
              alert('Erreurs détectées:\n' + response.errors.join('\n'));
            }
          }
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Erreur lors de la validation:', error);
          this.errorMessage = 'Erreur lors de la sauvegarde';
          alert('Erreur lors de la sauvegarde du planning');
          this.isLoading = false;
        }
      });
  }

  // Obtenir toutes les semaines
  getAllWeeks(): WeekDTO[] {
    return this.weeks.flatMap(month => month.weeks);
  }

  // Calculer le total des heures pour une ressource
  calculateTotalHours(row: RessourceScheduleDTO): number {
    if (!row.hoursPerWeek) return 0;
    return Object.values(row.hoursPerWeek).reduce((sum, hours) => sum + (hours || 0), 0);
  }

  // Calculer le total du projet
  calculateProjectTotal(): number {
    if (!this.projectData?.hoursPerWeek) return 0;
    return Object.values(this.projectData.hoursPerWeek).reduce((sum, hours) => sum + (hours || 0), 0);
  }

  // Calculer le total d'une semaine
  calculateWeekTotal(weekNum: number): number {
    return this.scheduleData.reduce((sum, row) => {
      return sum + (row.hoursPerWeek[weekNum.toString()] || 0);
    }, 0);
  }

  // Calculer le total d'une semaine avec projet
  calculateWeekTotalWithProject(weekNum: number): number {
    const resourcesTotal = this.calculateWeekTotal(weekNum);
    const projectHours = this.projectData?.hoursPerWeek[weekNum.toString()] || 0;
    return resourcesTotal + projectHours;
  }

  // Calculer le grand total (hors projet)
  calculateGrandTotal(): number {
    return this.scheduleData.reduce((sum, row) => sum + this.calculateTotalHours(row), 0);
  }

  // Calculer le grand total avec projet
  calculateGrandTotalWithProject(): number {
    return this.calculateGrandTotal() + this.calculateProjectTotal();
  }

  // Compter les semaines entreprise
  getCompanyWeeksCount(): number {
    return this.getAllWeeks().filter(week => week.type === 'S').length;
  }
}
