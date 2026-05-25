import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VacataireService } from '../../services/vacataire/vacataire.service';
import { VacataireModel } from '../../models/vacataire/vacataire.model';
import { PageTitle } from '../../services/page-title/page-title-service';

type View = 'list' | 'wizard' | 'summary';

const emptyForm = (): VacataireModel => ({
  responsableRecrutement: '',
  qualiteResponsable: '',
  departement: '',
  dateEntretien: '',
  prenom: '',
  nom: '',
  formationVisee: '',
  natureVacation: '',
  profilTechnique: '',
  profilPedagogique: '',
  competences: '',
  siteBourgenBresse: '',
  siteVilleurbanneDoua: '',
  siteVilleurbanneGratteCiel: '',
  transmisCV: '',
  signatureResponsable: '',
  sourceConnaissances: '',
  sourceConnaissanceAutre: '',
  statut: 'A_CONTACTER',
});

export const SOURCE_OPTIONS = [
  { value: 'COLLEGUE',      label: 'Par un collègue' },
  { value: 'PERSONNEL_IUT', label: 'Par un personnel de l\'IUT' },
  { value: 'SITE_IUT',      label: 'Par le site de l\'IUT' },
  { value: 'RESPONSABLE',   label: 'Par la personne en charge du recrutement' },
  { value: 'PROCHE',        label: 'Par un proche' },
  { value: 'BOUCHE_OREILLE',label: 'Par le bouche à oreille' },
  { value: 'AUTRE',         label: 'Autre (préciser)' },
];

@Component({
  selector: 'app-vacataire-manager',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './vacataire-manager.html',
})
export class VacataireManager implements OnInit {
  private readonly vacataireService = inject(VacataireService);
  private readonly pageTitle = inject(PageTitle);

  readonly sourceOptions = SOURCE_OPTIONS;
  readonly ouiNonOptions = ['Oui', 'Non'];

  view = signal<View>('list');
  step = signal<1 | 2 | 3>(1);
  isLoading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  vacataires = signal<VacataireModel[]>([]);
  selectedVacataire = signal<VacataireModel | null>(null);
  editId = signal<number | null>(null);
  searchTerm = signal('');

  formData = signal<VacataireModel>(emptyForm());

  ngOnInit() {
    this.pageTitle.title.set('Gestionnaire des vacataires');
    this.loadAll();
  }

  loadAll() {
    this.isLoading.set(true);
    this.vacataireService.getAll().subscribe({
      next: (data) => { this.vacataires.set(data); this.isLoading.set(false); },
      error: () => { this.errorMessage.set('Erreur lors du chargement'); this.isLoading.set(false); }
    });
  }

  openCreate() {
    this.editId.set(null);
    this.formData.set(emptyForm());
    this.step.set(1);
    this.errorMessage.set('');
    this.view.set('wizard');
  }

  openEdit(v: VacataireModel) {
    this.editId.set(v.id!);
    this.formData.set({ ...v });
    this.step.set(1);
    this.errorMessage.set('');
    this.view.set('wizard');
  }

  openSummary(v: VacataireModel) {
    this.selectedVacataire.set(v);
    this.view.set('summary');
  }

  backToList() {
    this.view.set('list');
    this.errorMessage.set('');
    this.successMessage.set('');
  }

  nextStep() {
    if (this.step() === 1 && !this.validateStep1()) return;
    if (this.step() < 3) this.step.set((this.step() + 1) as 1 | 2 | 3);
  }

  prevStep() {
    if (this.step() > 1) this.step.set((this.step() - 1) as 1 | 2 | 3);
  }

  validateStep1(): boolean {
    const f = this.formData();
    if (!f.nom?.trim() || !f.prenom?.trim()) {
      this.errorMessage.set('Le nom et le prénom du candidat sont obligatoires.');
      return false;
    }
    this.errorMessage.set('');
    return true;
  }

  save() {
    const data = this.formData();
    const id = this.editId();
    const request = id
      ? this.vacataireService.update(id, data)
      : this.vacataireService.create(data);

    this.isLoading.set(true);
    request.subscribe({
      next: () => {
        this.successMessage.set(id ? 'Vacataire mis à jour.' : 'Vacataire créé avec succès.');
        this.loadAll();
        this.view.set('list');
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la sauvegarde.');
        this.isLoading.set(false);
      }
    });
  }

  delete(v: VacataireModel) {
    if (!confirm(`Supprimer ${v.prenom} ${v.nom} ?`)) return;
    this.vacataireService.delete(v.id!).subscribe({
      next: () => { this.successMessage.set('Vacataire supprimé.'); this.loadAll(); },
      error: () => this.errorMessage.set('Erreur lors de la suppression.')
    });
  }

  onSearch(term: string) {
    this.searchTerm.set(term);
    if (term.trim().length < 2) { this.loadAll(); return; }
    this.vacataireService.search(term).subscribe({
      next: (data) => this.vacataires.set(data),
    });
  }

  updateField(field: keyof VacataireModel, value: any) {
    this.formData.set({ ...this.formData(), [field]: value });
  }

  isSourceChecked(value: string): boolean {
    return (this.formData().sourceConnaissances ?? '').split(',').includes(value);
  }

  toggleSource(value: string) {
    const current = (this.formData().sourceConnaissances ?? '')
      .split(',').filter(v => v.length > 0);
    const idx = current.indexOf(value);
    if (idx >= 0) current.splice(idx, 1);
    else current.push(value);
    this.updateField('sourceConnaissances', current.join(','));
  }

  statutLabel(statut?: string): string {
    const map: Record<string, string> = {
      A_CONTACTER: 'À contacter',
      EN_COURS: 'En cours',
      VALIDE: 'Validé',
    };
    return statut ? (map[statut] ?? statut) : '-';
  }

  statutClass(statut?: string): string {
    const map: Record<string, string> = {
      A_CONTACTER: 'bg-orange-100 text-orange-800',
      EN_COURS: 'bg-blue-100 text-blue-800',
      VALIDE: 'bg-green-100 text-green-800',
    };
    return statut ? (map[statut] ?? 'bg-gray-100 text-gray-600') : 'bg-gray-100 text-gray-600';
  }

  sourcesLabel(sources?: string): string {
    if (!sources) return '-';
    const map: Record<string, string> = {
      COLLEGUE:       'Par un collègue',
      PERSONNEL_IUT:  'Par un personnel de l\'IUT',
      SITE_IUT:       'Par le site de l\'IUT',
      RESPONSABLE:    'Par la personne en charge du recrutement',
      PROCHE:         'Par un proche',
      BOUCHE_OREILLE: 'Par le bouche à oreille',
      AUTRE:          'Autre',
    };
    return sources.split(',').map(v => map[v] ?? v).filter(Boolean).join(', ') || '-';
  }
}
