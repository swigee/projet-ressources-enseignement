import { Component, inject, signal, computed } from '@angular/core';
import { UserModel } from '../../models/user/user.model';
import { RoleModel } from '../../models/role/role.model';
import { UserService } from '../../services/user/user-service';
import { RoleService } from '../../services/role/role-service';
import { PageTitle } from '../../services/page-title/page-title-service';

@Component({
    selector: 'app-user-manager',
    imports: [],
    templateUrl: './user-manager.html'
})
export class UserManager {
  private readonly pageTitle = inject(PageTitle);
  private readonly userService = inject(UserService);
  private readonly roleService = inject(RoleService);
  errorMessage = '';
  isLoading = true;

  readonly tabRoles = signal<RoleModel[]>([]);
  readonly tabUsers = signal<UserModel[]>([]);
  readonly showFilter = signal(false);
  readonly searchName = signal('');

  readonly showAddRolePopup = signal<number | null>(null);
  readonly searchRoleText = signal('');
  readonly selectedRolesToAdd = signal<number[]>([]);

  readonly showImportModal = signal(false);
  importResult = signal<{ successCount: number; errorCount: number; errors: string[] } | null>(null);
  importLoading = signal(false);

  readonly selectedRoleFilter = signal<number[]>([]);

  ngOnInit() {
    this.pageTitle.title.set('Gestion des utilisateurs');
    this.loadUsers();
    this.loadRoles();
  }

  loadUsers() {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.tabUsers.set(users);
        this.isLoading = false;
        console.log(users);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs', err);
        this.errorMessage = 'Impossible de charger les utilisateurs.';
        this.isLoading = false;
      }
    });
  }

  loadRoles(){
    this.roleService.getAllRoles().subscribe({
      next: (roles) => {
      this.tabRoles.set(roles);
      console.log(roles)},
    error: (err) => {
        console.error('Erreur lors du chargement des rôles', err);
        this.errorMessage = 'Impossible de charger les rôles.';
      }
    });
  }

  readonly filteredUsers = computed(() => {
    return this.tabUsers().filter(user => {
      const fullName = `${user.firstname} ${user.lastname} ${user.username}`.toLowerCase();
      const matchName = fullName.includes(this.searchName().toLowerCase());
      let matchRole = true;
      if (this.selectedRoleFilter().length > 0) {
        matchRole = user.roles?.some(role => this.selectedRoleFilter().includes(role.id)) ?? false;
      }
      return matchName && matchRole;
    });
  });

  resetFilters() {
    this.searchName.set('');
    this.selectedRoleFilter.set([]);
  }

  onRoleFilterChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    const selected = Array.from(select.selectedOptions).map(opt => +opt.value);
    this.selectedRoleFilter.set(selected);
  }

  removeUserRole(iduser: number, id: number) {
    if (typeof iduser !== 'number' || typeof id !== 'number'){return};
    this.userService.deleteUserRole(iduser, id).subscribe({
      next: () => {
        this.loadUsers();
      },
      error: (err) => {
        console.error('Erreur lors de la suppression du rôle de l\'utilisateur', err);
        this.errorMessage = 'Impossible de supprimer le rôle de l\'utilisateur.';
      }
    });
  }

  openAddRolePopup(user: UserModel) {
    this.showAddRolePopup.set(user.iduser!);
    this.searchRoleText.set('');
    this.selectedRolesToAdd.set(user.roles?.map(r => r.id) ?? []);
  }

  closeAddRolePopup() {
    this.showAddRolePopup.set(null);
    this.selectedRolesToAdd.set([]);
    this.searchRoleText.set('');
  }

  toggleRoleToAdd(roleId: number) {
    const current = this.selectedRolesToAdd() ?? [];
    if (Array.isArray(current) && current.includes(roleId)) {
      this.selectedRolesToAdd.set(current.filter(id => id !== roleId));
    } else if (Array.isArray(current)) {
      this.selectedRolesToAdd.set([...current, roleId]);
    } else {
      this.selectedRolesToAdd.set([roleId]);
    }
  }

  getFilteredAvailableRoles(user: UserModel) {
    const userRoleIds = user.roles?.map(r => r.id) ?? [];
    return this.tabRoles().filter(role =>
      !userRoleIds.includes(role.id) &&
      (this.searchRoleText().trim() === '' || role.name.toLowerCase().includes(this.searchRoleText().toLowerCase()))
    );
  }

  addSelectedRolesToUser(user: UserModel) {
    if (!user.iduser || this.selectedRolesToAdd().length === 0) {
      this.userService.deleteAllUserRole(user.iduser).subscribe(() => {
        this.loadUsers();
      });
    };
    this.userService.updateUserRole(user.iduser, this.selectedRolesToAdd()).subscribe(() => {
      this.loadUsers();
      this.closeAddRolePopup();
    });
  }

  hasRole(user: UserModel, roleId: number): boolean {
    return user.roles?.some(r => r.id === roleId) ?? false;
  }

  openImportModal() {
    this.showImportModal.set(true);
    this.importResult.set(null);
  }

  closeImportModal() {
    this.showImportModal.set(false);
    this.importResult.set(null);
  }

  onCsvFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.importLoading.set(true);
    this.importResult.set(null);
    this.userService.importUsersFromCsv(file).subscribe({
      next: (result) => {
        this.importResult.set(result);
        this.importLoading.set(false);
        if (result.successCount > 0) this.loadUsers();
      },
      error: (err) => {
        console.error('Erreur import CSV:', err);
        this.importResult.set({ successCount: 0, errorCount: 1, errors: ['Erreur lors de l\'import.'] });
        this.importLoading.set(false);
      }
    });
    input.value = '';
  }
}
