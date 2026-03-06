import { Component, inject } from '@angular/core';
import { Router } from "@angular/router";
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Education } from '../../models/education/education.model';
import { LessonService } from '../../services/lesson/lesson-service';

@Component({
  selector: 'app-training-manager',
  templateUrl: './educational-manager.html',
})
export class EducationalManager {
  edManager = inject(EducationalManagerService)
  lessonsService = inject(LessonService)

  constructor(private router: Router) {}

  clickDelete(id: number) {
    this.edManager.deleteEducation(id)
  }

  updateEducation(etu: Education){
    this.router.navigate(['/education-manager/edit', etu.id]);
  }

  openDetails(){

  }
}
