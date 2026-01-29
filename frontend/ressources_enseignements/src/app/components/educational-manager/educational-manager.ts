import { Component, inject } from '@angular/core';
import { RouterLink } from "@angular/router";
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';

@Component({
  selector: 'app-training-manager',
  imports: [RouterLink],
  templateUrl: './educational-manager.html',
})
export class EducationalManager {
  edManager = inject(EducationalManagerService)

  clickDelete(id: number){
    this.edManager.deleteEducation(id)
  }

  updateEducation(id: number){
    this.edManager.updateEducation(id)
  }
}
