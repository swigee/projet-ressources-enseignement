import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-filter-controls',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './filter-controls.html'
})
export class FilterControls {
  @Input() selectedProgram = '';
  @Input() programs: { id: string; name: string }[] = [];
  @Input() selectedYear = '';
  @Input() selectedClass = '';
  @Input() availableClasses: string[] = [];
  @Input() selectedSemester = '';
  @Input() classData: Record<string, { name: string }> = {};

  get allClassesMode(): boolean {
    return !this.selectedClass;
  }

  @Output() programChange = new EventEmitter<string>();
  @Output() yearChange = new EventEmitter<string>();
  @Output() classChange = new EventEmitter<string>();
  @Output() semesterChange = new EventEmitter<string>();

  getYearKeys(): string[] {
    return Object.keys(this.classData);
  }
}
