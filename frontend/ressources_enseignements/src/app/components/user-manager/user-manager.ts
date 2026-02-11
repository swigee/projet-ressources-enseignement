import { Component, inject, signal, computed } from '@angular/core';
import { UserModel } from '../../models/user/user.model';
import { RoleModel } from '../../models/role/role.model';
import { UserService } from '../../services/user/user-service';
import { RoleService } from '../../services/role/role-service';
import { PageTitle } from '../../services/page-title/page-title-service';

@Component({
  selector: 'app-user-manager',
  templateUrl: './user-manager.html'
})
export class UserManager {
  private readonly pageTitle = inject(PageTitle);
  private readonly userService = inject(UserService);
  private readonly roleService = inject(RoleService);

  readonly tabRoles = signal<RoleModel[]>([]);
  readonly tabUsers = signal<UserModel[]>([]);
  readonly showFilter = signal(false);
  readonly searchName = signal('');
  readonly selectedRoles = signal<string[]>([]);
  readonly editingUserId = signal<number | null>(null);

  readonly showAddRolePopup = signal<number | null>(null);
  readonly searchRoleText = signal('');
  readonly selectedRolesToAdd = signal<number[]>([]);

  readonly selectedRoleFilter = signal<number[]>([]);

  // Suppression des signaux temporaires et méthodes associées à la validation différée des filtres

  readonly filteredUsers = computed(() => {
    return this.tabUsers().filter(user => {
      const fullName = `${user.firstname} ${user.lastname}`.toLowerCase();
      const matchName = fullName.includes(this.searchName().toLowerCase());
      let matchRole = true;
      if (this.selectedRoleFilter().length > 0) {
        matchRole = user.roles?.some(role => this.selectedRoleFilter().includes(role.id)) ?? false;
      }
      return matchName && matchRole;
    });
  });

  ngOnInit() {
    this.pageTitle.title.set('Gestion des utilisateurs');
    this.loadUsers();
    this.loadRoles();
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe(users => {
      this.tabUsers.set(users);
      console.log(users)
    });
  }

  loadRoles(){
    this.roleService.getAllRoles().subscribe(roles => {
      this.tabRoles.set(roles);
      console.log(roles)});
  }

  resetFilters() {
    this.searchName.set('');
    this.selectedRoleFilter.set([]);
  }

  onRoleFilterChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    const selected = Array.from(select.selectedOptions).map(opt => +opt.value);
    this.selectedRoleFilter.set(selected);
  }

  removeUser(id: number) {
    console.log(id);
    this.userService.deleteUser(id).subscribe(() => {
      this.loadUsers();

    });
  }

  removeUserRole(iduser: number, id: number) {
    if (typeof iduser !== 'number' || typeof id !== 'number'){return;};
    this.userService.deleteUserRole(iduser, id).subscribe(() => {
      this.loadUsers();
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

}
