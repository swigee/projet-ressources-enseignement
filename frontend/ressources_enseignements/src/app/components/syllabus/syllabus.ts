import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PageTitle } from '../../services/page-title/page-title-service';

interface ResourceSummary {
  id: string;
  code: string;
  title: string;
  semester: string;
  parcours: string;
  description: string;
}

@Component({
  selector: 'app-syllabus-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './syllabus.html',
})
export class SyllabusComponent implements OnInit {
  
  // Filters
  selectedSemester: string = 'Tous';
  selectedParcours: string = 'Tous';
  searchQuery: string = '';

  semesters = ['Tous', 'S1', 'S2', 'S3', 'S4', 'S5', 'S6'];
  parcoursOptions = ['Tous', 'RA', 'AGED', 'DACS'];

  // Dummy Data
  resources: ResourceSummary[] = [
    { id: '1', code: 'R1.01', title: 'Initiation au développement', semester: 'S1', parcours: 'Tronc Commun', description: 'Bases de la programmation.' },
    { id: '2', code: 'R2.01', title: 'Développement orienté objet', semester: 'S2', parcours: 'Tronc Commun', description: 'Concepts POO en Java.' },
    { id: '3', code: 'R3.01', title: 'Développement WEB', semester: 'S3', parcours: 'RA', description: 'HTML, CSS, JS, frameworks front.' },
    { id: '4', code: 'R3.02', title: 'Administration système', semester: 'S3', parcours: 'DACS', description: 'Linux, droits, bash.' },
    { id: '5', code: 'R5.Real.05', title: 'Programmation avancée', semester: 'S5', parcours: 'RA', description: 'Design patterns, optimisation.' },
    { id: '6', code: 'R5.A.04', title: 'Qualité algorithmique', semester: 'S5', parcours: 'AGED', description: 'Big data et traitement de graphes.' },
  ];

  constructor(private pageTitle: PageTitle, private router: Router) {}

  ngOnInit(): void {
    this.pageTitle.title.set('Syllabus - Liste des ressources');
  }

  // Lógica inteligente pour forcer Tronc Commun sur S1/S2
  onSemesterChange() {
    if (this.selectedSemester === 'S1' || this.selectedSemester === 'S2') {
      this.selectedParcours = 'Tous';
    }
  }

  get filteredResources(): ResourceSummary[] {
    return this.resources.filter(res => {
      // Filtre texte
      const matchesSearch = res.title.toLowerCase().includes(this.searchQuery.toLowerCase()) || 
                            res.code.toLowerCase().includes(this.searchQuery.toLowerCase());
      
      // Filtre Semestre
      const matchesSemester = this.selectedSemester === 'Tous' || res.semester === this.selectedSemester;
      
      // Filtre Parcours (Ignoré si S1/S2, ou si "Tous")
      let matchesParcours = true;
      if (this.selectedParcours !== 'Tous') {
        if (this.selectedSemester === 'S1' || this.selectedSemester === 'S2') {
           // S1 et S2 sont en tronc commun, ils ne matchent jamais un parcours spécifique
           matchesParcours = res.parcours === 'Tronc Commun';
        } else {
           matchesParcours = res.parcours === this.selectedParcours || res.parcours === 'Tronc Commun';
        }
      }

      return matchesSearch && matchesSemester && matchesParcours;
    });
  }

  goToDetail(id: string) {
    this.router.navigate(['/syllabus', id]);
  }
}
