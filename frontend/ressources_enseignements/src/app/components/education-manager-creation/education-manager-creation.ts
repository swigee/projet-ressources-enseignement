import { Component, inject, Input } from '@angular/core';
import { LessonService } from '../../services/lesson-service';
import { Lesson } from '../../models/lesson.model';
import { Education } from '../../models/education.model';
import { EducationalManagerService } from '../../services/educational-manager';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-education-manager-creation',
  imports: [ReactiveFormsModule],
  templateUrl: './education-manager-creation.html',
  styleUrl: './education-manager-creation.css',
})
export class EducationManagerCreation {
  lessonsService = inject(LessonService)
  edService = inject(EducationalManagerService)
  selectedLessons: Lesson[] = [];
  
  @Input() education?: Education;
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private service: EducationalManagerService,
    private router: Router
  ) 
  {
     this.lessonsService.loadLessons();
  }
  ngOnInit() {
    this.form = this.fb.group({
      id: [this.education?.idformation || null], 
      name: [this.education?.name || '', Validators.required],
      description: [this.education?.description || ''],
      lessons: [this.education?.lessons || []]
    });
  }

  goBack() {
    this.router.navigate(['/education-manager']);
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

  submit() {
    if (this.form.invalid) return;
    const edu: Education = this.form.value;
    if (edu.idformation) {
      this.service.updateEducation(edu).subscribe(() => {
        this.router.navigate(['/education-manager']);
        this.edService.loadEducations();
      });
    } 
    else {
      this.service.createEducation(edu).subscribe(() => {
        this.router.navigate(['/education-manager']);
        this.edService.loadEducations();
      });
    }
  }
}
