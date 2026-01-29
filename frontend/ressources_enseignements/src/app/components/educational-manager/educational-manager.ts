import { Component, inject } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Education } from '../../models/education.model';

@Component({
  selector: 'app-training-manager',
  imports: [RouterLink],
  templateUrl: './educational-manager.html',
  styleUrl: './educational-manager.css',
})
export class EducationalManager {
  edManager = inject(EducationalManagerService)
  
  constructor(private router: Router) {
  }

  clickDelete(id: number){
    this.edManager.deleteEducation(id)
  }

  updateEducation(etu: Education){
    this.router.navigate(['/education-manager/edit', etu]);
  }
}
