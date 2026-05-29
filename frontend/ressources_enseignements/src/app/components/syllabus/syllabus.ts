import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PageTitle } from '../../services/page-title/page-title-service';
import { SyllabusService } from '../../services/syllabus/syllabus.service';
import { SyllabusResource } from '../../models/syllabus/syllabus.model';
import { Auth } from '../../services/auth/auth';

interface ResourceDisplay {
  id: number;
  code: string;
  title: string;
  semester: string;
  pathway: string;
  description: string;
  isAdapted: boolean;
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
  selectedPathway: string = 'Tous';
  searchQuery: string = '';

  semesters = ['Tous', 'S1', 'S2', 'S3', 'S4', 'S5', 'S6'];
  pathwayOptions = ['Tous', 'RA', 'AGED', 'DACS'];

  // Data loaded from backend
  resources: ResourceDisplay[] = [];
  isLoading: boolean = true;
  isUploading: boolean = false;
  isAdmin: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private pageTitle: PageTitle,
    private router: Router,
    private syllabusService: SyllabusService,
    private auth: Auth
  ) {}

  ngOnInit(): void {
    this.pageTitle.title.set('Syllabus - Liste des ressources');
    this.isAdmin = this.auth.hasRole(['ADMIN']);
    this.loadResources();
  }

  loadResources(): void {
    this.isLoading = true;
    this.syllabusService.getAll().subscribe({
      next: (data: SyllabusResource[]) => {
        this.resources = data.map(r => ({
          id: r.id,
          code: r.code || '',
          title: r.title || '',
          semester: SyllabusService.getSemesterFromCode(r.code || ''),
          pathway: SyllabusService.getPathwayFromCode(r.code || ''),
          description: r.description || '',
          isAdapted: !!(r.personalDescription || r.personalKnowledge || r.personalLearning || r.personalVolume)
        }));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement syllabus:', err);
        this.errorMessage = 'Impossible de charger les ressources du syllabus.';
        this.isLoading = false;
      }
    });
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.isUploading = true;
      this.errorMessage = '';
      this.successMessage = '';

      this.syllabusService.uploadCsv(file).subscribe({
        next: (response) => {
          this.successMessage = 'Programme National importé avec succès !';
          this.isUploading = false;
          this.loadResources();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (err) => {
          this.errorMessage = 'Erreur lors de l\'importation : ' + err.message;
          this.isUploading = false;
        }
      });
    }
  }

  onSemesterChange() {
    if (this.selectedSemester === 'S1' || this.selectedSemester === 'S2') {
      this.selectedPathway = 'Tous';
    }
  }

  get filteredResources(): ResourceDisplay[] {
    return this.resources.filter(res => {
      const matchesSearch = res.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                            res.code.toLowerCase().includes(this.searchQuery.toLowerCase());

      const matchesSemester = this.selectedSemester === 'Tous' || res.semester === this.selectedSemester;

      let matchesPathway = true;
      if (this.selectedPathway !== 'Tous') {
        if (this.selectedSemester === 'S1' || this.selectedSemester === 'S2') {
           matchesPathway = res.pathway === 'Tronc Commun';
        } else {
           matchesPathway = res.pathway === this.selectedPathway || res.pathway === 'Tronc Commun';
        }
      }

      return matchesSearch && matchesSemester && matchesPathway;
    });
  }

  goToDetail(id: number) {
    this.router.navigate(['/syllabus', id]);
  }
}
