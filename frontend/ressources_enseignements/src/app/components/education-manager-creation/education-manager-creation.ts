import { Component, inject, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LessonService } from '../../services/lesson/lesson-service';
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Lesson } from '../../models/lesson/lesson.model';
import { Education } from '../../models/education/education.model';

@Component({
  selector: 'app-education-manager-creation',
  imports: [ReactiveFormsModule],
  templateUrl: './education-manager-creation.html',
})
export class EducationManagerCreation {
  lessonsService = inject(LessonService)
  edService = inject(EducationalManagerService)
  selectedLessons: Lesson[] = [];

  form!: FormGroup;
  private route = inject(ActivatedRoute);
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
      id: [null],
      name: ['', Validators.required],
      description: [''],
      lessons: [[]]
    });

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
    this.edService.getEducationById(+id).subscribe(edu => {
      this.form.patchValue(edu);

      const allLessons = this.lessonsService.lessonsList();

      this.selectedLessons = allLessons.filter(l =>
        edu.lessons?.some(el => el.id === l.id)
      );
      console.log(this.selectedLessons);
    });
  }
  }

  goBack() {
    this.router.navigate(['/education-manager']);
  }

  toggleLesson(l: Lesson) {
    const index = this.selectedLessons.findIndex(sl => sl.id === l.id);

    if (index !== -1) {
      this.selectedLessons.splice(index, 1);
    } else {
      this.selectedLessons.push(l);
    }
  }

  isSelected(l: Lesson): boolean {
    return this.selectedLessons.some(sl => sl.id === l.id);
  }


  submit() {
    if (this.form.invalid) return;
    const edu: Education = {
      ...this.form.value,
      lessons: this.selectedLessons
    };
    if (edu.id) {
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
