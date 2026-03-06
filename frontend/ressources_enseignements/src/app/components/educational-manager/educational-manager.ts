import { Component, inject } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Education } from '../../models/education/education.model';
import { LessonService } from '../../services/lesson/lesson-service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-training-manager',
  imports: [AsyncPipe],
  templateUrl: './educational-manager.html',
})
export class EducationalManager {
  edManager = inject(EducationalManagerService)
  lessonsService = inject(LessonService)
  userCounts: Record<number, number> = {};
  constructor(private router: Router) {
  }
  ngonInit() {
  this.edManager.educationList().forEach(ed => {
    if (ed.id) {
      this.edManager.getUsers(ed.id).subscribe(users => {
        this.userCounts[ed.id!] = users.length;
        console.log(`Education ID: ${ed.id}, User Count: ${users.length}`);
      });
    }
  });
  }
  clickDelete(id: number) {
    this.edManager.deleteEducation(id)
  }

  updateEducation(etu: Education){
    this.router.navigate(['/education-manager/edit', etu.id]);
  }

  getUsers(id: number){
    this.edManager.getUsers(id)
  }
}
