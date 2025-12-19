import { Component, inject } from '@angular/core';
import { EducationalManager } from '../../services/educational-manager';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-training-manager',
  imports: [RouterLink],
  templateUrl: './training-manager.html',
  styleUrl: './training-manager.css',
})
export class TrainingManager {
  edManager = inject(EducationalManager)

  clickDelete(id: number){
    this.edManager.deleteEducation(id)
  }
}
