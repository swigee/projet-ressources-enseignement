import { Component, inject, Input } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Education } from '../../models/education/education.model';

@Component({
  selector: 'app-training-manager',
  imports: [RouterLink],
  templateUrl: './educational-manager.html',
})
export class EducationalManager {
  edManager = inject(EducationalManagerService)
  
  constructor(private router: Router) {
  }

  clickDelete(id: number) {
    this.edManager.deleteEducation(id)
  }

  updateEducation(etu: Education){
    this.router.navigate(['/education-manager/edit', etu]);
  }
}
