import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClassData, CourseRow, MonthData } from '../../models/schedule.model';

@Component({
  selector: 'app-pedagogical-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pedagogical-schedule.html',
  styleUrl: './pedagogical-schedule.css',
})
export class PedagogicalScheduleComponent {
  activeTab: string = 'ressources';
  selectedYear: string = '1';
  selectedClass: string = 'BUT1-A';

  // Données des années et classes
  classData: ClassData = {
    '1': {
      name: '1ère année',
      classes: ['BUT1-A', 'BUT1-B', 'BUT1-C']
    },
    '2': {
      name: '2ème année',
      classes: ['BUT2-A', 'BUT2-B', 'BUT2-C']
    },
    '3': {
      name: '3ème année',
      classes: ['BUT3-A', 'BUT3-B', 'BUT3-C']
    }
  };

  // Semaines pour les années 1 et 2 (pas d'alternance)
  weeksYear12: MonthData[] = [
    {
      month: 'Septembre 2024',
      weeks: [
        { num: 1, date: '02', type: 'E' },
        { num: 2, date: '09', type: 'E' },
        { num: 3, date: '16', type: 'E' },
        { num: 4, date: '23', type: 'E' }
      ]
    },
    {
      month: 'Octobre 2024',
      weeks: [
        { num: 5, date: '30', type: 'E' },
        { num: 6, date: '07', type: 'E' },
        { num: 7, date: '14', type: 'E' },
        { num: 8, date: '21', type: 'E' },
        { num: 9, date: '28', type: 'E' },
        { num: 10, date: '04', type: 'E' },
        { num: 11, date: '11', type: 'E' },
        { num: 12, date: '18', type: 'E' },
        { num: 13, date: '25', type: 'E' },
        { num: 14, date: '02', type: 'E' }
      ]
    }
  ];

  // Semaines pour la 3ème année (alternance école/entreprise)
  weeksYear3: MonthData[] = [
    {
      month: 'Septembre 2024',
      weeks: [
        { num: 1, date: '02', type: 'E' },
        { num: 2, date: '09', type: 'E' },
        { num: 3, date: '16', type: 'E' },
        { num: 4, date: '23', type: 'E' }
      ]
    },
    {
      month: 'Octobre 2024',
      weeks: [
        { num: 5, date: '30', type: 'E' },
        { num: 6, date: '07', type: 'S' }, // Semaine entreprise
        { num: 7, date: '14', type: 'E' },
        { num: 8, date: '21', type: 'S' }, // Semaine entreprise
        { num: 9, date: '28', type: 'E' },
        { num: 10, date: '04', type: 'S' }, // Semaine entreprise
        { num: 11, date: '11', type: 'E' },
        { num: 12, date: '18', type: 'S' }, // Semaine entreprise
        { num: 13, date: '25', type: 'E' },
        { num: 14, date: '02', type: 'S' }  // Semaine entreprise
      ]
    }
  ];

  // Getter pour obtenir les semaines selon l'année sélectionnée
  get weeks(): MonthData[] {
    return this.selectedYear === '3' ? this.weeksYear3 : this.weeksYear12;
  }

  // Données des cours
  scheduleData: CourseRow[] = [
    {
      id: '1',
      courseName: "Initiation au management d'une équipe de projet informatique",
      isHighlighted: true,
      hoursPerWeek: { '1': 4, '2': 4, '3': 4 },
      hoursPerHalfGroup: {}
    },
    {
      id: '2',
      courseName: 'Qualité algorithmique',
      isHighlighted: false,
      hoursPerWeek: { '1': 2, '2': 2, '3': 2, '4': 2 },
      hoursPerHalfGroup: {}
    },
    {
      id: '3',
      courseName: 'Programmation avancée',
      category: 'Front-End (Angular)',
      isHighlighted: true,
      hoursPerWeek: { '1': 4, '2': 2, '3': 2 },
      hoursPerHalfGroup: {}
    },
    {
      id: '4',
      courseName: '',
      category: 'Back-End (SpringBoot)',
      isHighlighted: false,
      hoursPerWeek: { '1': 4, '2': 4, '3': 4, '4': 4 },
      hoursPerHalfGroup: {}
    },
    {
      id: '5',
      courseName: 'Qualité de dév',
      isHighlighted: false,
      hoursPerWeek: { '1': 2, '2': 4 },
      hoursPerHalfGroup: {}
    },
    {
      id: '6',
      courseName: 'Virtualisation avancée',
      isHighlighted: true,
      hoursPerWeek: { '1': 4, '2': 2, '3': 4 },
      hoursPerHalfGroup: {}
    },
    {
      id: '7',
      courseName: 'Programmation multimédia',
      isHighlighted: false,
      hoursPerWeek: { '1': 6, '2': 4, '3': 4 },
      hoursPerHalfGroup: {}
    },
    {
      id: '8',
      courseName: 'Programmation multimédia',
      isHighlighted: true,
      hoursPerWeek: { '1': 4, '2': 4 },
      hoursPerHalfGroup: {}
    },
    {
      id: '9',
      courseName: 'Programmation multimédia',
      isHighlighted: false,
      hoursPerWeek: { '12': 4, '13': 4, '14': 4 },
      hoursPerHalfGroup: {}
    },
    {
      id: '10',
      courseName: 'Programmation multimédia',
      isHighlighted: true,
      hoursPerWeek: { '12': 2, '14': 2 },
      hoursPerHalfGroup: {}
    }
  ];

  // Données du projet SAE
  projectData: CourseRow = {
    id: 'project-sae',
    courseName: 'Projet SAE',
    isHighlighted: false,
    hoursPerWeek: { '1': 8, '2': 8, '3': 8, '4': 8 },
    hoursPerHalfGroup: {}
  };

  // Méthode pour changer d'onglet
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  // Gérer le changement d'année
  onYearChange(year: string): void {
    this.selectedYear = year;
    this.selectedClass = this.classData[year].classes[0];
  }

  // Récupérer toutes les semaines
  getAllWeeks(): any[] {
    return this.weeks.flatMap(month => 
      month.weeks.map(week => ({ ...week, month: month.month }))
    );
  }

  // Obtenir les heures pour une semaine spécifique
  getHoursForWeek(row: CourseRow, weekNum: number): number | null {
    return row.hoursPerWeek[weekNum.toString()] || null;
  }

  // Calculer le total des heures pour un cours
  calculateTotalHours(row: CourseRow): number {
    return Object.values(row.hoursPerWeek).reduce((sum, hours) => sum + hours, 0);
  }

  // Calculer le total des heures par semaine (hors projet)
  calculateWeekTotal(weekNum: number): number {
    return this.scheduleData.reduce((sum, row) => {
      const hours = row.hoursPerWeek[weekNum.toString()] || 0;
      return sum + hours;
    }, 0);
  }

  // Calculer le total des heures par semaine (avec projet)
  calculateWeekTotalWithProject(weekNum: number): number {
    const resourceHours = this.calculateWeekTotal(weekNum);
    const projectHours = this.projectData.hoursPerWeek[weekNum.toString()] || 0;
    return resourceHours + projectHours;
  }

  // Calculer le total général (ressources seulement)
  calculateGrandTotal(): number {
    return this.scheduleData.reduce((sum, row) => sum + this.calculateTotalHours(row), 0);
  }

  // Calculer le total projet
  calculateProjectTotal(): number {
    return this.calculateTotalHours(this.projectData);
  }

  // Calculer le total général avec projet
  calculateGrandTotalWithProject(): number {
    return this.calculateGrandTotal() + this.calculateProjectTotal();
  }

  // Calculer le total hors semaines entreprise (ressources seulement)
  calculateTotalWithoutCompany(): number {
    let total = 0;
    this.getAllWeeks().forEach(week => {
      if (week.type === 'E') {
        total += this.calculateWeekTotal(week.num);
      }
    });
    return total;
  }

  // Calculer le total hors semaines entreprise (avec projet)
  calculateTotalWithoutCompanyWithProject(): number {
    let total = 0;
    this.getAllWeeks().forEach(week => {
      if (week.type === 'E') {
        total += this.calculateWeekTotalWithProject(week.num);
      }
    });
    return total;
  }

  // Compter le nombre de semaines entreprise
  getCompanyWeeksCount(): number {
    return this.getAllWeeks().filter(w => w.type === 'S').length;
  }

  // Obtenir les classes disponibles pour l'année sélectionnée
  getAvailableClasses(): string[] {
    return this.classData[this.selectedYear].classes;
  }

  // Obtenir les clés des années
  getYearKeys(): string[] {
    return Object.keys(this.classData);
  }

  isEditing: boolean = false; // État de saisie activé ou non

  // Activer la saisie
  enableEditing(): void {
    this.isEditing = true;
  }

  // Valider et désactiver la saisie
  validateEditing(): void {
    this.isEditing = false;
  }
}