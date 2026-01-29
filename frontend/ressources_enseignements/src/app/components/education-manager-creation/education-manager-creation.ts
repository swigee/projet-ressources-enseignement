import { Component, inject } from '@angular/core';
import { LessonService } from '../../services/lesson/lesson-service';
import { Lesson } from '../../models/lesson/lesson.model';
import { Education } from '../../models/education/education.model';
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';

@Component({
  selector: 'app-education-manager-creation',
  imports: [],
  templateUrl: './education-manager-creation.html',
})
export class EducationManagerCreation {
  lessonsService = inject(LessonService)
  edService = inject(EducationalManagerService)
  selectedLessons: Lesson[] = [];


  constructor(){
    this.lessonsService.loadLessons();
  }

  toggleLesson(l: Lesson) {
    if (this.selectedLessons.includes(l)) {
      this.selectedLessons = this.selectedLessons.filter(r => r !== l);
    }
    else {
      this.selectedLessons.push(l);
    }
  }

  clickCreate(e: Education){
    console.log('click sur bouton, creation')
    this.edService.createEducation(e);
  }
}
