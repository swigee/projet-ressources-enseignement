import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  TeacherAssignment,
  AffectationRow,
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
  selectedFormation: string = 'Informatique';
  selectedYear: string = '1';
  selectedClass: string = 'G1';
  selectedSemester: string = '';
  searchQuery: string = '';
  draggedTeacher: Teacher | null = null;
  isLoading: boolean = false;

  teachers: Teacher[] = [];
  affectationGrid: AffectationRow[] = [];
  statistics: any = null;

  constructor(private teacherService: TeacherAssignmentService, private pageTitle: PageTitle) {}

  ngOnInit() {
    this.pageTitle.title.set("Affectation des enseignants");
    console.log('Initialisation avec:', {
      formation: this.selectedFormation,
      year: this.selectedYear,
      class: this.selectedClass
    });
    this.loadData();
  }

  loadData(): void {
    if (!this.selectedYear || !this.selectedClass) {
      console.warn('Année ou classe non sélectionnée');
      this.affectationGrid = [];
      return;
    }

    console.log('Chargement des données pour:', {
      formation: this.selectedFormation,
      year: this.selectedYear,
      class: this.selectedClass
    });

    this.isLoading = true;
    this.teacherService.getAssignmentGrid(
      this.selectedFormation,
      this.selectedYear,
      this.selectedClass,
      this.selectedSemester
    ).subscribe({
      next: (data: AssignmentGrid) => {
        console.log('Données reçues du backend:', data);
        console.log('Nombre de ressources:', data.affectationGrid?.length || 0);
        console.log('Nombre d\'enseignants:', data.availableTeachers?.length || 0);

        this.teachers = data.availableTeachers || [];
        this.affectationGrid = data.affectationGrid || [];
        this.statistics = data.statistics;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des données:', error);
        this.isLoading = false;
        this.affectationGrid = [];
        alert('Erreur lors du chargement des données. Vérifiez la console pour plus de détails.');
      }
    });
  }

  /**
   * Reload data when filters change
   */
  onFilterChange(): void {
    console.log('Changement de filtre détecté:', {
      formation: this.selectedFormation,
      year: this.selectedYear,
      class: this.selectedClass,
      semester: this.selectedSemester
    });
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

  onDrop(event: DragEvent, ressourceId: number, lessonType: 'TD' | 'TP' | 'CM'): void {
    event.preventDefault();
    if (!event.dataTransfer) return;

    try {
      const data = event.dataTransfer.getData('application/json');
      const dragData: DragData = JSON.parse(data);

      if (dragData.type !== 'teacher') return;

      const module = this.affectationGrid.find(m => m.resourceId === ressourceId);
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
          resourceId: ressourceId,
          lessonType: lessonType,
          assignedTimes: hours
        };

        this.teacherService.createAssignment(assignment).subscribe({
          next: () => {
            console.log('Affectation créée avec succès');
            this.loadData();
          },
          error: (error) => {
            console.error('Erreur lors de la création:', error);
            alert('Erreur lors de la création de l\'affectation: ' + (error.error?.message || error.message));
          }
        });
      }

    } catch (error) {
      console.error('Erreur lors du drop:', error);
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
        console.error('Erreur mise à jour:', error);
        alert('Erreur lors de la modification des heures.');
      }
    });
  }

  removeTeacher(assignmentId: number): void {
    if (confirm('Voulez-vous retirer cet enseignant de ce module ?')) {
      this.teacherService.deleteAssignment(assignmentId).subscribe({
        next: () => {
          console.log('Affectation supprimée');
          this.loadData();
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
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
        console.error('Erreur lors de la validation:', error);
        alert('Erreur lors de la validation des affectations');
      }
    });
  }


  saveAssignments(): void {
    alert('Les affectations ont été enregistrées avec succès !');
    this.loadData();
  }
}
