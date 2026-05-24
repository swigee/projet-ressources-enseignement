import { Component, Input, Output, EventEmitter, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RessourceRow, RessourcesTotals, ScheduleConflict, TeacherBadge } from '../../../models/ressources/ressources.model';

@Component({
  selector: 'app-resources-tab',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './resources-tab.html'
})
export class ResourcesTab {
  @Input() filteredRessources: RessourceRow[] = [];
  @Input() calculatedTotals: RessourcesTotals = {
    totalPlannedHours: 0, totalActualHours: 0,
    totalTDHours: 0, totalTPHours: 0, totalCMHours: 0
  };
  @Input() filteredConflicts: ScheduleConflict[] = [];
  @Input() availableTeachers: TeacherBadge[] = [];
  @Input() selectedTeacherIds: number[] = [];
  @Input() searchQuery = '';

  @Output() searchQueryChange = new EventEmitter<string>();
  @Output() teacherSelected = new EventEmitter<number>();
  @Output() teacherFilterRemoved = new EventEmitter<number>();
  @Output() filtersCleared = new EventEmitter<void>();

  teacherSearchQuery = signal<string>('');
  showTeacherDropdown = signal<boolean>(false);

  filteredTeacherSuggestions = computed(() => {
    const query = this.teacherSearchQuery().toLowerCase().trim();
    if (!query) return [];
    return this.availableTeachers.filter(t =>
      t.fullName.toLowerCase().includes(query) && !this.selectedTeacherIds.includes(t.teacherId)
    );
  });

  onSearchInput(event: Event): void {
    this.searchQueryChange.emit((event.target as HTMLInputElement).value);
  }

  onTeacherSearchInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.teacherSearchQuery.set(value);
    this.showTeacherDropdown.set(value.trim().length > 0);
  }

  selectTeacherFromDropdown(teacherId: number): void {
    this.teacherSelected.emit(teacherId);
    this.teacherSearchQuery.set('');
    this.showTeacherDropdown.set(false);
  }

  closeTeacherDropdown(): void {
    setTimeout(() => this.showTeacherDropdown.set(false), 200);
  }

  getTeacherName(teacherId: number): string {
    return this.availableTeachers.find(t => t.teacherId === teacherId)?.fullName ?? '';
  }

  hasConflict(ressource: RessourceRow): boolean {
    return this.filteredConflicts.some(c => c.conflictingModules.includes(ressource.moduleName));
  }
}
