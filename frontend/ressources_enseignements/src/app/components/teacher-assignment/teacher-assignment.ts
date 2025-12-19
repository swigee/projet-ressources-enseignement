import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import {AffectationRow, DragData, Teacher } from '../../models/teacher.model';

@Component({
  selector: 'app-teacher-assignment',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './teacher-assignment.html',
  styleUrl: './teacher-assignment.css'
})
export class TeacherAssignmentComponent {
  selectedFormation: string = '';
  selectedYear: string = '';
  selectedSemester: string = '';
  searchQuery: string = '';
  draggedTeacher: Teacher | null = null;

  // Données des enseignants disponibles
  teachers: Teacher[] = [
    {
      id: 1,
      name: 'Martin GEANT',
      subject: 'Maths, Algorithmes',
      status: 'Permanent',
      hours: '192h restants',
      statusColor: 'bg-green-100 text-green-800'
    },
    {
      id: 2,
      name: 'Leroy Merlin',
      subject: 'Ergonomie', 
      status: 'Vacataire',
      hours: '100h restant',
      statusColor: 'bg-blue-100 text-blue-800'
    },
    {
      id: 3,
      name: 'Marine LEROUSSE',
      subject: 'Maths, Algorithmes', 
      status: 'Permanent',
      hours: '120 restants',
      statusColor: 'bg-green-100 text-green-800'
    }
  ];

  // Grille d'affectation
  affectationGrid: AffectationRow[] = [
    {
      id: 'dev-avancee',
      module: 'Dev Avancée',
      td: '25h',
      tp: '18h',
      cm: 'X',
      teachers: ['Martin GEANT']
    },
    {
      id: 'dev-qualite',
      module: 'Dev Qualité',
      td: '25h',
      tp: '18h',
      cm: 'X',
      teachers: []
    },
    {
      id: 'angular',
      module: 'Angular',
      td: '25h',
      tp: '18h',
      cm: 'X',
      teachers: []
    },
    {
      id: 'droit',
      module: 'Droit',
      td: '25h',
      tp: 'X',
      cm: '18h',
      teachers: ['Leroy Merlin']
    }
  ];

  // Filtrer les enseignants selon la recherche
  get filteredTeachers(): Teacher[] {
    if (!this.searchQuery) {
      return this.teachers;
    }
    return this.teachers.filter(teacher =>
      teacher.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      teacher.subject.toLowerCase().includes(this.searchQuery.toLowerCase()) // ← CORRIGÉ : subject
    );
  }

  // Drag & Drop - Début du drag
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

  // Drag & Drop - Survol de la zone de dépôt
  onDragOver(event: DragEvent): void {
    event.preventDefault();
    if (event.dataTransfer) {
      event.dataTransfer.dropEffect = 'copy';
    }
  }

  // Drag & Drop - Dépôt sur un module
  onDrop(event: DragEvent, moduleId: string): void {
    event.preventDefault();
    
    if (!event.dataTransfer) return;

    try {
      const data = event.dataTransfer.getData('application/json');
      const dragData: DragData = JSON.parse(data);

      if (dragData.type === 'teacher') {
        const module = this.affectationGrid.find(m => m.id === moduleId);
        if (module && !module.teachers.includes(dragData.teacherName)) {
          module.teachers.push(dragData.teacherName);
        }
      }
    } catch (error) {
      console.error('Erreur lors du drop:', error);
    }

    this.draggedTeacher = null;
  }

  // Drag & Drop - Fin du drag
  onDragEnd(): void {
    this.draggedTeacher = null;
  }

  // Retirer un enseignant d'un module
  removeTeacher(moduleId: string, teacherName: string): void {
    const module = this.affectationGrid.find(m => m.id === moduleId);
    if (module) {
      module.teachers = module.teachers.filter((t: string) => t !== teacherName); // ← CORRIGÉ : typage explicite
    }
  }

  // Afficher les enseignants affectés
  getTeachersDisplay(teachers: string[]): string {
    if (teachers.length === 0) {
      return 'Non Affecté';
    }
    if (teachers.length === 1) {
      return teachers[0];
    }
    return teachers[0] + ', ...';
  }

  // Vérifier si un module est vide
  isModuleEmpty(teachers: string[]): boolean {
    return teachers.length === 0;
  }
}