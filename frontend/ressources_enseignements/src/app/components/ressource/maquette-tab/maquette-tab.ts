import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MonthDTO, WeekDTO, WeekHoursDTO, RessourceScheduleDTO, ProjectScheduleDTO } from '../../../models/schedule/schedule.model';

export interface ScheduleHoursDelta {
  rowId: number;
  weekNum: number;
  type: 'cm' | 'td' | 'tp';
  value: number;
}

export interface ProjectHoursDelta {
  weekNum: number;
  type: 'cm' | 'td' | 'tp';
  value: number;
}

@Component({
  selector: 'app-maquette-tab',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './maquette-tab.html'
})
export class MaquetteTab {
  @Input() scheduleData: RessourceScheduleDTO[] = [];
  @Input() projectData: ProjectScheduleDTO = {
    id: 'project-sae', name: 'Projet SAE', hoursPerWeek: {}, hoursPerHalfGroup: 0, totalHours: 0
  };
  @Input() weeks: MonthDTO[] = [];
  @Input() isEditing = false;
  @Input() successMessage = '';
  @Input() errorMessage = '';

  @Output() editingEnabled = new EventEmitter<void>();
  @Output() editingValidated = new EventEmitter<void>();
  @Output() editingCancelled = new EventEmitter<void>();
  @Output() scheduleHoursChanged = new EventEmitter<ScheduleHoursDelta>();
  @Output() projectHoursChanged = new EventEmitter<ProjectHoursDelta>();

  getAllWeeks(): WeekDTO[] {
    return this.weeks.flatMap(month => month.weeks);
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
    return this.scheduleData.reduce((s, r) => s + this.calculateTotalCM(r), 0);
  }

  get maquetteTotalTD(): number {
    return this.scheduleData.reduce((s, r) => s + this.calculateTotalTD(r), 0);
  }

  get maquetteTotalTP(): number {
    return this.scheduleData.reduce((s, r) => s + this.calculateTotalTP(r), 0);
  }

  calculateProjectTotal(): number {
    if (!this.projectData?.hoursPerWeek) return 0;
    return Object.values(this.projectData.hoursPerWeek).reduce(
      (sum, w) => sum + (w.cm || 0) + (w.td || 0) + (w.tp || 0), 0
    );
  }

  calculateWeekTotal(weekNum: number): number {
    return this.scheduleData.reduce((sum, row) => {
      const w = row.hoursPerWeek[weekNum.toString()];
      return sum + (w ? (w.cm || 0) + (w.td || 0) + (w.tp || 0) : 0);
    }, 0);
  }

  calculateWeekTotalWithProject(weekNum: number): number {
    const pw = this.projectData?.hoursPerWeek[weekNum.toString()];
    return this.calculateWeekTotal(weekNum) + (pw ? (pw.cm || 0) + (pw.td || 0) + (pw.tp || 0) : 0);
  }

  calculateMaquetteGrandTotal(): number {
    return this.scheduleData.reduce((sum, row) => sum + this.calculateTotalHours(row), 0);
  }

  calculateGrandTotalWithProject(): number {
    return this.calculateMaquetteGrandTotal() + this.calculateProjectTotal();
  }

  getCompanyWeeksCount(): number {
    return this.getAllWeeks().filter(w => w.type === 'S').length;
  }

  getWeekTotal(hoursPerWeek: { [key: string]: WeekHoursDTO }, weekNum: number): number {
    const w = hoursPerWeek[weekNum.toString()];
    return w ? (w.cm || 0) + (w.td || 0) + (w.tp || 0) : 0;
  }

  onScheduleHoursInput(rowId: number, weekNum: number, type: 'cm' | 'td' | 'tp', event: Event): void {
    const value = Number((event.target as HTMLInputElement).value) || 0;
    this.scheduleHoursChanged.emit({ rowId, weekNum, type, value });
  }

  onProjectHoursInput(weekNum: number, type: 'cm' | 'td' | 'tp', event: Event): void {
    const value = Number((event.target as HTMLInputElement).value) || 0;
    this.projectHoursChanged.emit({ weekNum, type, value });
  }
}
