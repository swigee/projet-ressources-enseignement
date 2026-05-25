import { Component, inject, signal } from '@angular/core';
import { Router } from "@angular/router";
import { FormsModule } from '@angular/forms';
import { EducationalManagerService } from '../../services/educational-manager/educational-manager';
import { Education } from '../../models/education/education.model';
import { LessonService } from '../../services/lesson/lesson-service';
import { PageTitle } from '../../services/page-title/page-title-service';

@Component({
  selector: 'app-training-manager',
  imports: [FormsModule],
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
    this.edManager.deleteEducation(id);
  }

  updateEducation(etu: Education){
    this.router.navigate(['/education-manager/edit', etu.id]);
  }

  getUsers(id: number){
    this.edManager.getUsers(id);
  }

  // --- Duplication ---
  readonly showDuplicateModal = signal(false);
  duplicateSourceId = signal<number | null>(null);
  duplicateSourceName = signal('');
  duplicateNewName = signal('');
  duplicateLoading = signal(false);

  openDuplicateModal(ed: Education) {
    this.duplicateSourceId.set(ed.id!);
    this.duplicateSourceName.set(ed.name);
    this.duplicateNewName.set(ed.name + ' - Copie');
    this.showDuplicateModal.set(true);
  }

  closeDuplicateModal() {
    this.showDuplicateModal.set(false);
    this.duplicateLoading.set(false);
  }

  confirmDuplicate() {
    const id = this.duplicateSourceId();
    const name = this.duplicateNewName().trim();
    if (!id || !name) return;

    this.duplicateLoading.set(true);
    this.edManager.duplicateEducation(id, name).subscribe({
      next: () => {
        this.edManager.loadEducations();
        this.closeDuplicateModal();
      },
      error: (err) => {
        console.error('Erreur duplication:', err);
        this.duplicateLoading.set(false);
      }
    });
  }
}
