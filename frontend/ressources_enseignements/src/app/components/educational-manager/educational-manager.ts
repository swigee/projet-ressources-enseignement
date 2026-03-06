import { Component, inject } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Education } from '../../models/education/education.model';
import { LessonService } from '../../services/lesson/lesson-service';
import { AsyncPipe } from '@angular/common';
import {PageTitle} from '../../services/page-title/page-title-service';

@Component({
  selector: 'app-training-manager',
  imports: [],
  templateUrl: './educational-manager.html',
})
export class EducationalManager {

  edManager = inject(EducationalManagerService)
  lessonsService = inject(LessonService)
  userCounts: Record<number, number> = {};
  constructor(private router: Router, private pageTitle: PageTitle) {
  }
  ngOnInit() {
    this.pageTitle.title.set("Gestion des formations");
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
