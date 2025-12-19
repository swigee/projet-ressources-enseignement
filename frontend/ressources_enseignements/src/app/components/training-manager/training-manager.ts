import { Component, inject } from '@angular/core';
import { EducationalManager } from '../../services/educational-manager';

@Component({
  selector: 'app-training-manager',
  imports: [],
  templateUrl: './training-manager.html',
  styleUrl: './training-manager.css',
})
export class TrainingManager {
  edManager = inject(EducationalManager)

  
}
