import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SoftwareService } from '../../services/software/software.service';
import { SoftwareModel } from '../../models/software/software.model';
import { Auth } from '../../services/auth/auth';
import { PageTitle } from '../../services/page-title/page-title-service';
import { User } from '../../interfaces/user.interface';

@Component({
  selector: 'app-software-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './software-list.html'
})
export class SoftwareList implements OnInit {
  private softwareService = inject(SoftwareService);
  private auth = inject(Auth);

  currentUser = signal<User | null>(null);

  softwareList = signal<SoftwareModel[]>([]);
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  infoModal = signal<SoftwareModel | null>(null);
  editingSoftware = signal<SoftwareModel | null>(null);
  confirmDeleteId = signal<number | null>(null);

  resourceTagInput = signal<string>('');
  editResourceTagInput = signal<string>('');

  form = {
    name: '',
    version: '',
    plugins: '',
    license: 'Gratuite',
    period: '',
    resourceNames: [] as string[]
  };

  editForm = {
    name: '',
    version: '',
    plugins: '',
    license: 'Gratuite',
    period: '',
    resourceNames: [] as string[]
  };

  readonly currentYear = '2026-2027';

  requestedSoftware = computed(() => this.softwareList().filter(s => s.status === 'REQUESTED'));

  constructor(private pageTitle: PageTitle) {
    this.auth.currentUser$.subscribe(user => this.currentUser.set(user));
  }

  ngOnInit(): void {
    this.pageTitle.title.set('Liste des logiciels');
    this.loadSoftware();
  }

  loadSoftware(): void {
    this.isLoading.set(true);
    this.softwareService.getAll().subscribe({
      next: (data) => {
        this.softwareList.set(data);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  addResourceTag(event?: Event): void {
    if (event) event.preventDefault();
    const tag = this.resourceTagInput().trim();
    if (tag && !this.form.resourceNames.includes(tag)) {
      this.form.resourceNames = [...this.form.resourceNames, tag];
    }
    this.resourceTagInput.set('');
  }

  onResourceBlur(): void {
    const tag = this.resourceTagInput().trim();
    if (tag && !this.form.resourceNames.includes(tag)) {
      this.form.resourceNames = [...this.form.resourceNames, tag];
      this.resourceTagInput.set('');
    }
  }

  removeResourceTag(tag: string): void {
    this.form.resourceNames = this.form.resourceNames.filter(t => t !== tag);
  }

  addEditResourceTag(event?: Event): void {
    if (event) event.preventDefault();
    const tag = this.editResourceTagInput().trim();
    if (tag && !this.editForm.resourceNames.includes(tag)) {
      this.editForm.resourceNames = [...this.editForm.resourceNames, tag];
    }
    this.editResourceTagInput.set('');
  }

  onEditResourceBlur(): void {
    const tag = this.editResourceTagInput().trim();
    if (tag && !this.editForm.resourceNames.includes(tag)) {
      this.editForm.resourceNames = [...this.editForm.resourceNames, tag];
      this.editResourceTagInput.set('');
    }
  }

  removeEditResourceTag(tag: string): void {
    this.editForm.resourceNames = this.editForm.resourceNames.filter(t => t !== tag);
  }

  createSoftware(): void {
    if (!this.form.name.trim()) {
      this.errorMessage.set('Le nom du logiciel est obligatoire');
      setTimeout(() => this.errorMessage.set(''), 3000);
      return;
    }

    const user = this.currentUser();
    const dto = {
      name: this.form.name,
      version: this.form.version,
      plugins: this.form.plugins,
      license: this.form.license,
      period: this.form.period,
      status: 'REQUESTED',
      year: this.currentYear,
      resourceNames: this.form.resourceNames.join(','),
      userId: user?.id
    };

    this.softwareService.create(dto).subscribe({
      next: (created) => {
        this.softwareList.update(list => [...list, created]);
        this.resetForm();
        this.successMessage.set('Logiciel créé avec succès !');
        setTimeout(() => this.successMessage.set(''), 3000);
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la création du logiciel');
        setTimeout(() => this.errorMessage.set(''), 3000);
      }
    });
  }

  openEdit(software: SoftwareModel): void {
    this.editingSoftware.set(software);
    this.editResourceTagInput.set('');
    this.editForm = {
      name: software.name,
      version: software.version,
      plugins: software.plugins || '',
      license: software.license,
      period: software.period,
      resourceNames: [...(software.resourceNames || [])]
    };
  }

  saveEdit(): void {
    const editing = this.editingSoftware();
    if (!editing?.id) return;

    const dto = {
      name: this.editForm.name,
      version: this.editForm.version,
      plugins: this.editForm.plugins,
      license: this.editForm.license,
      period: this.editForm.period,
      status: editing.status,
      year: editing.year,
      resourceNames: this.editForm.resourceNames.join(',')
    };

    this.softwareService.update(editing.id, dto).subscribe({
      next: (updated) => {
        this.softwareList.update(list => list.map(s => s.id === updated.id ? updated : s));
        this.editingSoftware.set(null);
        this.successMessage.set('Logiciel modifié avec succès !');
        setTimeout(() => this.successMessage.set(''), 3000);
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la modification');
        setTimeout(() => this.errorMessage.set(''), 3000);
      }
    });
  }

  cancelEdit(): void {
    this.editingSoftware.set(null);
  }

  requestDelete(id: number): void {
    this.confirmDeleteId.set(id);
  }

  cancelDelete(): void {
    this.confirmDeleteId.set(null);
  }

  confirmDelete(): void {
    const id = this.confirmDeleteId();
    if (id === null) return;
    this.softwareService.delete(id).subscribe({
      next: () => {
        this.softwareList.update(list => list.filter(s => s.id !== id));
        this.confirmDeleteId.set(null);
        this.successMessage.set('Logiciel supprimé.');
        setTimeout(() => this.successMessage.set(''), 3000);
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la suppression');
        setTimeout(() => this.errorMessage.set(''), 3000);
        this.confirmDeleteId.set(null);
      }
    });
  }

  resetForm(): void {
    this.form = {
      name: '',
      version: '',
      plugins: '',
      license: 'Gratuite',
      period: '',
      resourceNames: []
    };
    this.resourceTagInput.set('');
  }

  exportSoftware(): void {
    const all = this.softwareList();
    const header = ['Name', 'Version', 'Plugins', 'License', 'Period', 'Status', 'Year', 'Resources', 'Contact', 'Email'];
    const rows = all.map(s => [
      s.name ?? '', s.version ?? '', s.plugins ?? '', s.license ?? '',
      s.period ?? '', s.status ?? '', s.year ?? '',
      (s.resourceNames ?? []).join(';'), s.userFullName ?? '', s.userEmail ?? ''
    ]);
    const csv = [header, ...rows].map(r => r.map(v => `"${v}"`).join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'software.csv';
    link.click();
  }
}
