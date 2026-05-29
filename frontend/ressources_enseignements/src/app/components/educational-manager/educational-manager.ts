import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from "@angular/router";
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Education, EducationSemester } from '../../models/education/education.model';
import { LessonService } from '../../services/lesson/lesson-service';
import { PageTitle } from '../../services/page-title/page-title-service';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter } from 'rxjs/operators';
import { Lesson } from '../../models/lesson/lesson.model';

@Component({
  selector: 'educational-manager',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './educational-manager.html',
})
export class EducationalManager {

  edManager = inject(EducationalManagerService)
  lessonsService = inject(LessonService)
  userCounts: Record<number, number> = {};
  form!: FormGroup;
  popUp: boolean = false;
  private isPatchInProgress = false;

  constructor(private router: Router, private pageTitle: PageTitle, private fb: FormBuilder,) {}

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
    this.initializeForm();
    this.setupAutoSave();
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

  getAvailableLessons(group: FormGroup): Lesson[] {
    const semesterValue = group.get('semester_number')?.value;
    const selectedLessons = this.getLessonsFromGroup(group);
    const filteredLessons = this.allLessons.filter(lesson => {
      if (semesterValue == null || semesterValue === '') {
        return true;
      }
      return lesson.semester === semesterValue;
    });
    const allAvailable = [...filteredLessons];
    selectedLessons.forEach(lesson => {
      if (!allAvailable.some(l => l.id === lesson.id)) {
        allAvailable.push(lesson);
      }
    });
    return allAvailable;
  }

  get totalSelectedLessons(): number {
    return this.semesters.controls.reduce((total, group) => {
      const lessons: Lesson[] = group.get('lessons')?.value || [];
      return total + lessons.length;
    }, 0);
  }

  initializeForm() {
    this.form = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      description: [''],
      pathway: [''],
      semesters: this.fb.array([this.createSemesterGroup()])
    });
  }

  setupAutoSave() {
    this.form.valueChanges.pipe(
      debounceTime(800),
      distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)),
      filter(() => !this.isPatchInProgress),
      filter(() => !!this.form.value?.id)
    ).subscribe(() => {
      if (this.form.valid) {
        this.submit(false);
      }
    });
  }

  createSemesterGroup(): FormGroup {
    return this.fb.group({
      semester_number: [1, Validators.required],
      pathway: [''],
      lessons: [[]]
    });
  }

  addSemester() {
    this.semesters.push(this.createSemesterGroup());
  }

  removeSemester(index: number) {
    this.semesters.removeAt(index);
  }

  openCreate() {
    this.popUp = true;
    this.initializeForm();
  }

  detailsEducation(ed: Education) {
    this.popUp = true;
    this.form.reset();
    if (ed.id) {
      this.edManager.getEducationById(+ed.id).subscribe(edu => {
        this.patchEducation(edu);
      });
    }
  }

  private groupResourcesBySemester(resources: Lesson[]): Array<{ semester_number: number; pathway: string; lessons: Lesson[] }> {
    const groups = new Map<number, { semester_number: number; pathway: string; lessons: Lesson[] }>();

    resources.forEach((resource) => {
      const semesterNumber = Number(resource.semester || 1) || 1;
      if (!groups.has(semesterNumber)) {
        groups.set(semesterNumber, {
          semester_number: semesterNumber,
          pathway: this.form.value.pathway || '',
          lessons: []
        });
      }
      groups.get(semesterNumber)?.lessons.push(resource);
    });

    return Array.from(groups.values()).sort((a, b) => a.semester_number - b.semester_number);
  }

  patchEducation(edu: Education) {
    this.isPatchInProgress = true;
    this.form.patchValue({
      id: edu.id,
      name: edu.name,
      description: edu.description,
      pathway: edu.pathway || ''
    });

    this.semesters.clear();

    if (edu.semesters && edu.semesters.length) {
      edu.semesters
        .slice()
        .sort((a, b) => a.semester_number - b.semester_number)
        .forEach((semester) => {
          this.semesters.push(this.fb.group({
            semester_number: [semester.semester_number, Validators.required],
            pathway: [semester.pathway || ''],
            lessons: [semester.resources || []]
          }));
        });
    } else {
      const resources = edu.resources || [];
      const semesterGroups = this.groupResourcesBySemester(resources);

      if (semesterGroups.length) {
        semesterGroups.forEach((semester) => {
          this.semesters.push(this.fb.group({
            semester_number: [semester.semester_number, Validators.required],
            pathway: [semester.pathway || ''],
            lessons: [semester.lessons || []]
          }));
        });
      } else {
        const defaultGroup = this.createSemesterGroup();
        defaultGroup.patchValue({ lessons: resources });
        this.semesters.push(defaultGroup);
      }
    }

    this.isPatchInProgress = false;
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

  onSemesterChange(selectedSemester: number, group: FormGroup) {
    group.get('semester_number')?.setValue(selectedSemester);
    const filteredLessons = this.getLessonsFromGroup(group).filter(lesson =>
      lesson.semester === selectedSemester
    );
    group.get('lessons')?.setValue(filteredLessons);
  }

  clickDelete(id: number) {
    this.edManager.deleteEducation(id);
  }

  openDetails(){

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
      pathway: semester.pathway,
      resourceList: (semester.lessons || []).map((lesson: Lesson) => this.mapLessonToResourceId(lesson))
    }));

    return {
      name: formValue.name,
      description: formValue.description,
      pathway: formValue.pathway,
      resourceList,
      semesters
    };
  }

  buildEducationUpdatePayload(): any {
    const formValue = this.form.value;
    const semesters = (formValue.semesters || []).map((semester: any) => ({
      semester_number: Number(semester.semester_number),
      pathway: semester.pathway,
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
      program: {
        id: formValue.id,
        name: formValue.name,
        description: formValue.description,
        pathway: formValue.pathway
      },
      semesters,
      resources
    };
  }

  submit(closeOnSuccess: boolean = true) {
    this.form.updateValueAndValidity();
    if (this.form.invalid) {
      console.warn('Form invalid, cannot submit:', this.form.value, this.form.errors);
      this.form.markAllAsTouched();
      return;
    }

    if (this.form.value.id) {
      const edu = this.buildEducationUpdatePayload();
      console.log('Payload to save:', edu);
      this.edManager.updateEducation(edu).subscribe({
        next: () => {
          if (closeOnSuccess) {
            this.popUp = false;
          }
          this.edManager.loadEducations();
        },
        error: (err) => {
          console.error('Error updating education:', err);
        }
      });
    } else {
      const payload = this.buildFormCreationPayload();
      console.log('Payload to create:', payload);
      this.edManager.createEducation(payload).subscribe({
        next: () => {
          if (closeOnSuccess) {
            this.popUp = false;
          }
          this.edManager.loadEducations();
        },
        error: (err) => {
          console.error('Error creating education:', err);
        }
      });
    }
  }
}
