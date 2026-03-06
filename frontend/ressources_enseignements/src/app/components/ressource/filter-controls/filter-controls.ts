import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-filter-controls',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './filter-controls.html'
})
export class FilterControls {
  @Input() selectedYear = '1';
  @Input() selectedClass = 'Classe A';
  @Input() selectedSemester = '1';
  @Input() classData: Record<string, { name: string; classes: string[] }> = {};

  @Output() yearChange = new EventEmitter<string>();
  @Output() classChange = new EventEmitter<string>();
  @Output() semesterChange = new EventEmitter<string>();

  getYearKeys(): string[] {
    return Object.keys(this.classData);
  }

  getAvailableClasses(): string[] {
    return this.classData[this.selectedYear]?.classes || [];
  }
}
