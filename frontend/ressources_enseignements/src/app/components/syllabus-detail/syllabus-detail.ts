import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PageTitle } from '../../services/page-title/page-title-service';
import { SyllabusService } from '../../services/syllabus/syllabus.service';
import { SyllabusResource } from '../../models/syllabus/syllabus.model';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { Auth } from '../../services/auth/auth';

interface Section {
  id: string;
  label: string;
  content: string;
  isModified?: boolean;
}

@Component({
  selector: 'app-syllabus-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './syllabus-detail.html',
})
export class SyllabusDetailComponent implements OnInit, OnDestroy {

  resourceId: number = 0;
  isLoading: boolean = true;
  isEditor: boolean = false;
  errorMessage: string = '';

  resourceInfo = {
    title: '',
    code: '',
    meta: ''
  };

  // Sections from National Program
  nationalSections: Section[] = [];

  // Live model for saving
  universityData: any = {
    personalDescription: '',
    personalKnowledge: '',
    personalLearning: '',
    personalVolume: ''
  };

  // Initial data used only for the first render to avoid cursor jumping
  initialUniversityData: any = {
    personalDescription: '',
    personalKnowledge: '',
    personalLearning: '',
    personalVolume: ''
  };

  // Set of active section IDs (all active by default)
  activeSections: Set<string> = new Set();

  saveStatus: 'idle' | 'saving' | 'saved' | 'error' = 'idle';
  private update$ = new Subject<void>();
  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pageTitle: PageTitle,
    private syllabusService: SyllabusService,
    private auth: Auth
  ) {}

  ngOnInit(): void {
    this.resourceId = Number(this.route.snapshot.paramMap.get('id')) || 0;
    this.isEditor = this.auth.hasRole(['ADMIN', 'TEACHER']);
    this.loadResource();

    this.update$.pipe(
      debounceTime(1000),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.saveUniversityFields();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadResource(): void {
    this.isLoading = true;
    this.syllabusService.getById(this.resourceId).subscribe({
      next: (data: SyllabusResource) => {
        this.resourceInfo = {
          title: data.title || '',
          code: data.code || '',
          meta: `Semestre ${SyllabusService.getSemesterFromCode(data.code || '')} — Programme National BUT Informatique`
        };

        this.pageTitle.title.set(`Syllabus - ${data.code}`);

        this.nationalSections = this.buildSections(data);
        this.nationalSections.forEach(s => this.activeSections.add(s.id));

        const personalData = {
          personalDescription: data.personalDescription || '',
          personalKnowledge: data.personalKnowledge || '',
          personalLearning: data.personalLearning || '',
          personalVolume: data.personalVolume || ''
        };

        this.universityData = { ...personalData };
        this.initialUniversityData = { ...personalData };

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement détail:', err);
        this.errorMessage = 'Impossible de charger cette ressource.';
        this.isLoading = false;
      }
    });
  }

  onFieldChange(field: string, event: any): void {
    const content = event.target.innerHTML;
    this.universityData[field] = content;
    this.saveStatus = 'saving';
    this.update$.next();
  }

  copyToUniversity(sectionId: string, editorElement: HTMLDivElement): void {
    const nationalSection = this.nationalSections.find(s => s.id === sectionId);
    if (nationalSection) {
      const content = this.formatContent(nationalSection.content);
      editorElement.innerHTML = content;
      this.universityData[sectionId] = content;
      this.saveStatus = 'saving';
      this.update$.next();
    }
  }

  private saveUniversityFields(): void {
    this.syllabusService.updatePersonalFields(this.resourceId, this.universityData).subscribe({
      next: () => {
        this.saveStatus = 'saved';
        setTimeout(() => {
          if (this.saveStatus === 'saved') this.saveStatus = 'idle';
        }, 3000);
      },
      error: (err) => {
        console.error('Erreur sauvegarde:', err);
        this.saveStatus = 'error';
      }
    });
  }

  private buildSections(data: SyllabusResource): Section[] {
    const sections: Section[] = [];

    const isAdapted = (field: string) => !!(data as any)[field];

    if (data.description || isAdapted('personalDescription')) {
      const parts = (data.description || '').split(' | ');
      const content = parts.map(p => p.trim()).filter(p => p.length > 0).join('\n');
      sections.push({
        id: 'personalDescription',
        label: 'Descriptif',
        content: content,
        isModified: isAdapted('personalDescription')
      });
    }

    if (data.knowledge || isAdapted('personalKnowledge')) {
      const parts = (data.knowledge || '').split(' | ');
      const content = parts.map(p => p.trim()).filter(p => p.length > 0).join('\n');
      sections.push({
        id: 'personalKnowledge',
        label: 'Savoirs de référence',
        content: content,
        isModified: isAdapted('personalKnowledge')
      });
    }

    if (data.criticalLearning || isAdapted('personalLearning')) {
      const parts = (data.criticalLearning || '').split(' | ');
      const content = parts.map(p => p.trim()).filter(p => p.length > 0).join('\n');
      sections.push({
        id: 'personalLearning',
        label: 'Apprentissages critiques ciblés',
        content: content,
        isModified: isAdapted('personalLearning')
      });
    }

    if (data.officialVolume || isAdapted('personalVolume')) {
      sections.push({
        id: 'personalVolume',
        label: 'Volume horaire',
        content: data.officialVolume || '',
        isModified: isAdapted('personalVolume')
      });
    }

    return sections;
  }

  toggleSection(sectionId: string) {
    if (this.activeSections.has(sectionId)) {
      this.activeSections.delete(sectionId);
    } else {
      this.activeSections.add(sectionId);
    }
  }

  isSectionActive(sectionId: string): boolean {
    return this.activeSections.has(sectionId);
  }

  goBack() {
    this.router.navigate(['/syllabus']);
  }

  formatContent(text: string): string {
    if (!text) return '';
    return text.replace(/\n/g, '<br/>');
  }
}
