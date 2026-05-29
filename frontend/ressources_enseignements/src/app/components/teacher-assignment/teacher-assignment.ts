import { Component, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  TeacherAssignment,
  AssignmentRow,
  AssignmentGrid,
  Teacher,
  AssignmentValidationResponse,
  CreateAssignment,
  AssignmentStatistics
} from '../../models/teacher/teacher.model';
import {TeacherAssignmentService} from '../../services/teacher-assignment/teacher-assignment-service'
import {PageTitle} from '../../services/page-title/page-title-service';

interface DragData {
  type: string;
  teacherId: number;
  teacherName: string;
}

@Component({
  selector: 'app-teacher-assignment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './teacher-assignment.html',
})
export class TeacherAssignmentComponent implements OnInit {
  selectedProgram: string = 'Informatique';
  selectedYear: string = '1';
  selectedClass: string = '';
  selectedSemester: string = '';
  searchQuery: string = '';
  draggedTeacher: Teacher | null = null;
  isLoading: boolean = false;

  activeTab: 'TD' | 'TP' | 'CM' = 'TD';
  readonly tabs: ('TD' | 'TP' | 'CM')[] = ['TD', 'TP', 'CM'];
  isMobileView = false;
  showAssignModal = false;
  modalResourceId: number | null = null;
  modalLessonType: 'TD' | 'TP' | 'CM' | null = null;
  modalSearchQuery = '';

  @HostListener('window:resize')
  onResize() {
    this.isMobileView = window.innerWidth < 1024;
  }

  teachers: Teacher[] = [];
  assignmentGrid: AssignmentRow[] = [];
  statistics: any = null;
  availableGroups: string[] = [];

  constructor(private teacherService: TeacherAssignmentService, private pageTitle: PageTitle) {}

  ngOnInit() {
    this.pageTitle.title.set("Affectation des enseignants");
    this.isMobileView = window.innerWidth < 1024;
    this.loadGroups();
    this.loadData();
  }

  get allClassesMode(): boolean {
    return !this.selectedClass;
  }

  loadGroups(): void {
    this.teacherService.getAvailableClasses(this.selectedYear, this.selectedProgram).subscribe({
      next: (groups) => {
        this.availableGroups = groups;
        if (this.selectedClass && !groups.includes(this.selectedClass)) {
          this.selectedClass = '';
        }
      },
      error: () => { this.availableGroups = []; }
    });
  }

  loadData(): void {
    this.isLoading = true;
    this.teacherService.getAssignmentGrid(
      this.selectedProgram,
      this.selectedYear,
      this.selectedClass,
      this.selectedSemester
    ).subscribe({
      next: (data: AssignmentGrid) => {
        console.log('Data received from backend:', data);
        console.log('Number of resources:', data.assignmentGrid?.length || 0);
        console.log('Number of teachers:', data.availableTeachers?.length || 0);

        this.teachers = data.availableTeachers || [];
        this.assignmentGrid = data.assignmentGrid || [];
        this.statistics = data.statistics;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading data:', error);
        this.isLoading = false;
        this.assignmentGrid = [];
        alert('Error loading data. Check the console for details.');
      }
    });
  }

  onFilterChange(): void {
    this.loadGroups();
    this.loadData();
  }

  get filteredTeachers(): Teacher[] {
    if (!this.searchQuery) {
      return this.teachers;
    }
    const query = this.searchQuery.toLowerCase();
    return this.teachers.filter(teacher =>
      teacher.name.toLowerCase().includes(query) ||
      (teacher.subject && teacher.subject.toLowerCase().includes(query)) ||
      teacher.status.toLowerCase().includes(query)
    );
  }

  onDragStart(event: DragEvent, teacher: Teacher): void {
    this.draggedTeacher = teacher;
    if (event.dataTransfer) {
      event.dataTransfer.effectAllowed = 'copy';
      const dragData: DragData = {
        type: 'teacher',
        teacherId: teacher.id,
        teacherName: teacher.name
      };
      event.dataTransfer.setData('application/json', JSON.stringify(dragData));
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    if (event.dataTransfer) {
      event.dataTransfer.dropEffect = 'copy';
    }
  }

  onDrop(event: DragEvent, resourceId: number, lessonType: 'TD' | 'TP' | 'CM'): void {
    event.preventDefault();
    if (!event.dataTransfer) return;

    try {
      const data = event.dataTransfer.getData('application/json');
      const dragData: DragData = JSON.parse(data);

      if (dragData.type !== 'teacher') return;

      const module = this.assignmentGrid.find(m => m.resourceId === resourceId);
      if (!module) return;

      let teacherList: TeacherAssignment[];
      let moduleHours: number;

      switch (lessonType) {
        case 'TD':
          teacherList = module.tdTeachers;
          moduleHours = module.tdHours || 10;
          break;
        case 'TP':
          teacherList = module.tpTeachers;
          moduleHours = module.tpHours || 10;
          break;
        case 'CM':
          teacherList = module.cmTeachers;
          moduleHours = module.cmHours || 10;
          break;
        default:
          return;
      }

      const usedHours = teacherList.reduce((sum, t) => sum + t.assignedHours, 0);
      const remainingHours = moduleHours - usedHours;

      const defaultHours = remainingHours > 0 ? remainingHours : moduleHours;
      let hoursStr = prompt(`Combien d'heures pour le ${lessonType} ? (quota : ${moduleHours}h, utilisées : ${usedHours}h)`, `${defaultHours}`);
      if (!hoursStr) {
        this.draggedTeacher = null;
        return;
      }

      let hours = parseInt(hoursStr);
      if (isNaN(hours) || hours <= 0) {
        alert(`Nombre d'heures invalide.`);
        this.draggedTeacher = null;
        return;
      }

      if (usedHours + hours > moduleHours) {
        const over = usedHours + hours - moduleHours;
        if (!confirm(`Attention : cette affectation dépasse le quota de ${moduleHours}h de ${over}h. Confirmer quand même ?`)) {
          this.draggedTeacher = null;
          return;
        }
      }

      const existingAssignment = teacherList.find(t => t.teacherId === dragData.teacherId);
      if (existingAssignment) {
        existingAssignment.assignedHours += hours;
      } else {
        const assignment: CreateAssignment = {
          userId: dragData.teacherId,
          resourceId: resourceId,
          lessonType: lessonType,
          assignedTimes: hours
        };

        this.teacherService.createAssignment(assignment).subscribe({
          next: () => {
            console.log('Assignment created successfully');
            this.loadData();
          },
          error: (error) => {
            console.error('Error creating assignment:', error);
            alert('Error creating assignment: ' + (error.error?.message || error.message));
          }
        });
      }

    } catch (error) {
      console.error('Error on drop:', error);
    } finally {
      this.draggedTeacher = null;
    }
  }

  onDragEnd(): void {
    this.draggedTeacher = null;
  }

  editTeacherHours(teacher: TeacherAssignment, resourceId: number, lessonType: 'TD' | 'TP' | 'CM'): void {
    const hoursStr = prompt(`Modifier les heures de ${teacher.teacherName} (${lessonType}) :`, `${teacher.assignedHours}`);
    if (!hoursStr) return;

    const hours = parseInt(hoursStr);
    if (isNaN(hours) || hours <= 0) {
      alert('Nombre d\'heures invalide.');
      return;
    }

    const dto: CreateAssignment = {
      userId: teacher.teacherId,
      resourceId,
      lessonType,
      assignedTimes: hours
    };

    this.teacherService.updateAssignment(teacher.assignmentId, dto).subscribe({
      next: () => this.loadData(),
      error: (error) => {
        console.error('Update error:', error);
        alert('Erreur lors de la modification des heures.');
      }
    });
  }

  removeTeacher(assignmentId: number): void {
    if (confirm('Voulez-vous retirer cet enseignant de ce module ?')) {
      this.teacherService.deleteAssignment(assignmentId).subscribe({
        next: () => {
          console.log('Assignment deleted');
          this.loadData();
        },
        error: (error) => {
          console.error('Error deleting assignment:', error);
          alert('Erreur lors de la suppression');
        }
      });
    }
  }

  getTeachersDisplay(teachers: TeacherAssignment[]): string {
    if (!teachers || teachers.length === 0) {
      return 'Non Affecté';
    }
    if (teachers.length === 1) {
      return teachers[0].teacherName;
    }
    return teachers[0].teacherName + ', +' + (teachers.length - 1);
  }

  isModuleEmpty(teachers: TeacherAssignment[]): boolean {
    return !teachers || teachers.length === 0;
  }

  getLessonTypeBadgeColor(lessonType: string): string {
    switch (lessonType) {
      case 'CM': return 'bg-purple-100 text-purple-800';
      case 'TD': return 'bg-blue-100 text-blue-800';
      case 'TP': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  validateAssignments(): void {
    if (!this.selectedYear || !this.selectedClass) {
      alert('Veuillez sélectionner une année et une classe');
      return;
    }

    this.teacherService.validateAssignments(this.selectedYear, this.selectedClass).subscribe({
      next: (response) => {
        if (response.success) {
          let message = response.message;
          if (response.warnings && response.warnings.length > 0) {
            message += '\n\nAvertissements:\n' + response.warnings.join('\n');
          }
          alert(message);
        } else {
          let message = response.message;
          if (response.errors && response.errors.length > 0) {
            message += '\n\nErreurs:\n' + response.errors.join('\n');
          }
          alert(message);
        }
      },
      error: (error) => {
        console.error('Validation error:', error);
        alert('Erreur lors de la validation des affectations');
      }
    });
  }


  saveAssignments(): void {
    alert('Les affectations ont été enregistrées avec succès !');
    this.loadData();
  }

  setActiveTab(tab: 'TD' | 'TP' | 'CM') {
    this.activeTab = tab;
  }

  openAssignModal(resourceId: number, lessonType: 'TD' | 'TP' | 'CM') {
    this.modalResourceId = resourceId;
    this.modalLessonType = lessonType;
    this.modalSearchQuery = '';
    this.showAssignModal = true;
  }

  closeModal() {
    this.showAssignModal = false;
    this.modalResourceId = null;
    this.modalLessonType = null;
  }

  get modalFilteredTeachers(): Teacher[] {
    if (!this.modalSearchQuery) return this.teachers;
    const q = this.modalSearchQuery.toLowerCase();
    return this.teachers.filter(t =>
      t.name.toLowerCase().includes(q) ||
      (t.subject && t.subject.toLowerCase().includes(q))
    );
  }

  assignFromModal(teacher: Teacher) {
    if (this.modalResourceId === null || !this.modalLessonType) return;

    const module = this.assignmentGrid.find(m => m.resourceId === this.modalResourceId);
    if (!module) return;

    let teacherList: TeacherAssignment[];
    let moduleHours: number;

    switch (this.modalLessonType) {
      case 'TD': teacherList = module.tdTeachers; moduleHours = module.tdHours || 10; break;
      case 'TP': teacherList = module.tpTeachers; moduleHours = module.tpHours || 10; break;
      case 'CM': teacherList = module.cmTeachers; moduleHours = module.cmHours || 10; break;
      default: return;
    }

    const usedHours = teacherList.reduce((sum, t) => sum + t.assignedHours, 0);
    const remainingHours = moduleHours - usedHours;
    const defaultHours = remainingHours > 0 ? remainingHours : moduleHours;

    const hoursStr = prompt(
      `Heures pour ${teacher.name} en ${this.modalLessonType} ? (quota : ${moduleHours}h, utilisées : ${usedHours}h)`,
      `${defaultHours}`
    );
    if (!hoursStr) return;

    const hours = parseInt(hoursStr);
    if (isNaN(hours) || hours <= 0) { alert('Nombre d\'heures invalide.'); return; }

    if (usedHours + hours > moduleHours) {
      const over = usedHours + hours - moduleHours;
      if (!confirm(`Dépasse le quota de ${moduleHours}h de ${over}h. Confirmer quand même ?`)) return;
    }

    const assignment: CreateAssignment = {
      userId: teacher.id,
      resourceId: this.modalResourceId,
      lessonType: this.modalLessonType,
      assignedTimes: hours
    };

    this.teacherService.createAssignment(assignment).subscribe({
      next: () => { this.closeModal(); this.loadData(); },
      error: (error) => {
        console.error('Assignment creation error:', error);
        alert('Erreur lors de l\'affectation: ' + (error.error?.message || error.message));
      }
    });
  }
}
