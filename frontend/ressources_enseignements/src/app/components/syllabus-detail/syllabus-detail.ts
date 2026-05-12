import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { PageTitle } from '../../services/page-title/page-title-service';

interface Section {
  id: string;
  label: string;
  content: string;
}

@Component({
  selector: 'app-syllabus-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './syllabus-detail.html',
})
export class SyllabusDetailComponent implements OnInit {
  
  resourceId: string = '';
  
  // Fake data for the selected resource
  resourceInfo = {
    title: 'Programmation avancée',
    code: 'R5.Real.05',
    meta: 'Enseignant: Prof Martin Dubois - modifié le 15/11/2024'
  };

  sections: Section[] = [
    { id: 'competences', label: 'Compétence ciblées', content: "Compétences ciblées :\n- Développer — c'est-à-dire concevoir, coder, tester et intégrer — une solution informatique pour un client.\n- Proposer des applications informatiques optimisées en fonction de critères spécifiques : temps d'exécution, précision, consommation de ressources." },
    { id: 'sae', label: 'SAE', content: "SAE associées :\n- SAE 5.01 : Développement d'une application distribuée.\n- SAE 5.02 : Optimisation de code." },
    { id: 'descriptif', label: 'Descriptif', content: "Descriptif de la ressource :\nCe module approfondit les concepts de la programmation orientée objet et fonctionnelle, les patrons de conception (Design Patterns), et l'optimisation algorithmique avancée." },
    { id: 'savoirs', label: 'Savoirs de référence étudiés', content: "Savoirs de référence :\n- Patrons de conception (GoF)\n- Programmation réactive\n- Algorithmique des graphes\n- Tests unitaires et d'intégration" },
    { id: 'prolongements', label: 'Prolongements suggérés', content: "Prolongements suggérés :\n- Frameworks Spring Boot avancés\n- Architecture microservices\n- Clean Architecture" },
    { id: 'apprentissages', label: 'Apprentissages critiques ciblés', content: "Apprentissages critiques :\n- AC1 : Concevoir une architecture logicielle robuste.\n- AC2 : Mesurer et améliorer les performances d'un programme." },
    { id: 'mots_cles', label: 'Mots clés', content: "Mots clés :\nJava, Spring, Design Patterns, Clean Code, TDD, Algorithmique" },
    { id: 'volume', label: 'Volume horaire', content: "Volume horaire :\n- CM : 10h\n- TD : 15h\n- TP : 15h" }
  ];

  // Set of active section IDs (all active by default)
  activeSections: Set<string> = new Set();

  constructor(
    private route: ActivatedRoute, 
    private router: Router,
    private pageTitle: PageTitle
  ) {}

  ngOnInit(): void {
    // Get ID from URL
    this.resourceId = this.route.snapshot.paramMap.get('id') || '';
    
    // In a real app, we would fetch data using this.resourceId here.
    // For now, we update the title and activate all sections.
    this.pageTitle.title.set(`Syllabus - ${this.resourceInfo.code}`);
    
    this.sections.forEach(s => this.activeSections.add(s.id));
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
    return text.replace(/\n/g, '<br/>');
  }
}
