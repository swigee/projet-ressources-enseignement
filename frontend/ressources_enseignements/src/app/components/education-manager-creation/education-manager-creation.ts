import { Component, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { LessonService } from '../../services/lesson/lesson-service';
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Lesson } from '../../models/lesson/lesson.model';
import { Education, EducationSemester } from '../../models/education/education.model';

@Component({
  selector: 'app-education-manager-creation',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './education-manager-creation.html',
})
export class EducationManagerCreation {
  lessonsService = inject(LessonService)
  edService = inject(EducationalManagerService)

  form!: FormGroup;
  private route = inject(ActivatedRoute);

  constructor(
    private fb: FormBuilder,
    private service: EducationalManagerService,
    private router: Router
  ) {
    this.lessonsService.loadLessons();
  }

  ngOnInit() {
    this.form = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      description: [''],
      parcours: [''],
      semesters: this.fb.array([])
    });

    this.addSemester();

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.edService.getEducationById(+id).subscribe(edu => {
        this.patchEducation(edu);
      });
    }
  }

  get semesters() {
    return this.form.get('semesters') as FormArray;
  }

  get semesterFormGroups(): FormGroup[] {
    return this.semesters.controls as FormGroup[];
  }

  get allLessons(): Lesson[] {
    return this.lessonsService.lessonsList();
  }

  get totalSelectedLessons(): number {
    return this.semesters.controls.reduce((total, group) => {
      const lessons: Lesson[] = group.get('lessons')?.value || [];
      return total + lessons.length;
    }, 0);
  }

  addSemester() {
    this.semesters.push(this.createSemesterGroup());
  }

  removeSemesterByGroup(group: FormGroup) {
    const index = this.getSemesterIndex(group);
    if (index !== -1) {
      this.semesters.removeAt(index);
    }
  }

  getSemesterIndex(group: FormGroup) {
    return this.semesters.controls.indexOf(group);
  }

  createSemesterGroup(): FormGroup {
    return this.fb.group({
      semester_number: ['1', Validators.required],
      parcours: [''],
      lessons: [[]]
    });
  }

  private groupResourcesBySemester(resources: Lesson[]): Array<{ semester_number: number; parcours: string; lessons: Lesson[] }> {
    const groups = new Map<number, { semester_number: number; parcours: string; lessons: Lesson[] }>();

    resources.forEach((resource) => {
      const semesterNumber = Number(resource.semester || 1) || 1;
      if (!groups.has(semesterNumber)) {
        groups.set(semesterNumber, {
          semester_number: semesterNumber,
          parcours: this.form.value.parcours || '',
          lessons: []
        });
      }
      groups.get(semesterNumber)?.lessons.push(resource);
    });

    return Array.from(groups.values()).sort((a, b) => a.semester_number - b.semester_number);
  }

  patchEducation(edu: Education) {
    this.form.patchValue({
      id: edu.id,
      name: edu.name,
      description: edu.description,
      parcours: edu.parcours || ''
    });

    this.semesters.clear();

    if (edu.semesters && edu.semesters.length) {
      edu.semesters
        .slice()
        .sort((a, b) => a.semester_number - b.semester_number)
        .forEach((semester) => {
          this.semesters.push(this.fb.group({
            semester_number: [semester.semester_number, Validators.required],
            parcours: [semester.parcours || '', Validators.required],
            lessons: [semester.resourceList || []]
          }));
        });
    } else {
      const resources = edu.resourceList || [];
      const semesterGroups = this.groupResourcesBySemester(resources);

      if (semesterGroups.length) {
        semesterGroups.forEach((semester) => {
          this.semesters.push(this.fb.group({
            semester_number: [semester.semester_number, Validators.required],
            parcours: [semester.parcours, Validators.required],
            lessons: [semester.lessons || []]
          }));
        });
      } else {
        const defaultGroup = this.createSemesterGroup();
        defaultGroup.patchValue({ lessons: resources });
        this.semesters.push(defaultGroup);
      }
    }
  }

  getLessonsFromGroup(group: FormGroup): Lesson[] {
    return group.get('lessons')?.value || [];
  }

  isLessonSelected(l: Lesson, group: FormGroup): boolean {
    const lessons = this.getLessonsFromGroup(group);
    return lessons.some(sl => sl.id === l.id);
  }

  toggleLesson(l: Lesson, group: FormGroup) {
    const semesterGroup = group;
    const lessons: Lesson[] = [...this.getLessonsFromGroup(semesterGroup)];
    const existingIndex = lessons.findIndex(sl => sl.id === l.id);

    if (existingIndex !== -1) {
      lessons.splice(existingIndex, 1);
    } else {
      lessons.push(l);
    }

    semesterGroup.get('lessons')?.setValue(lessons);
  }

  private mapLessonToResourceId(lesson: Lesson) {
    return { id: lesson.id };
  }

  buildFormCreationPayload(): any {
    const formValue = this.form.value;
    const resourceList = [] as Array<{ id: number }>;

    (formValue.semesters || []).forEach((semester: any) => {
      (semester.lessons || []).forEach((lesson: Lesson) => {
        const resource = this.mapLessonToResourceId(lesson);
        if (!resourceList.some(r => r.id === resource.id)) {
          resourceList.push(resource);
        }
      });
    });

    const semesters = (formValue.semesters || []).map((semester: any) => ({
      semester_number: Number(semester.semester_number),
      parcours: semester.parcours,
      resourceList: (semester.lessons || []).map((lesson: Lesson) => this.mapLessonToResourceId(lesson))
    }));

    return {
      name: formValue.name,
      description: formValue.description,
      parcours: formValue.parcours,
      resourceList,
      semesters
    };
  }

  buildEducationUpdatePayload(): any {
    const formValue = this.form.value;
    const semesters = (formValue.semesters || []).map((semester: any) => ({
      semester_number: Number(semester.semester_number),
      parcours: semester.parcours,
      resourceList: (semester.lessons || []).map((lesson: Lesson) => this.mapLessonToResourceId(lesson))
    }));

    const resources: Array<{ id: number }> = [];
    semesters.forEach((semester: any) => {
      (semester.resourceList || []).forEach((lesson: { id: number }) => {
        if (!resources.some(r => r.id === lesson.id)) {
          resources.push(lesson);
        }
      });
    });

    return {
      formation: {
        id: formValue.id,
        name: formValue.name,
        description: formValue.description,
        parcours: formValue.parcours
      },
      semesters,
      resources
    };
  }

  goBack() {
    this.router.navigate(['/education-manager']);
  }

  submit() {
    if (this.form.invalid) return;

    if (this.form.value.id) {
      const edu = this.buildEducationUpdatePayload();
      this.service.updateEducation(edu).subscribe(() => {
        this.router.navigate(['/education-manager']);
        this.edService.loadEducations();
      });
    } else {
      const formation = this.buildFormCreationPayload();
      this.service.createEducation(formation).subscribe(() => {
        this.router.navigate(['/education-manager']);
        this.edService.loadEducations();
      });
    }
  }
}
