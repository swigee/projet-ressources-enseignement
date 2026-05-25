import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { PageTitle } from '../../services/page-title/page-title-service';
import { RoleService } from '../../services/role/role-service';
import { RoleModel } from '../../models/role/role.model';
import { PermissionService } from '../../services/permission/permission';
import { PermissionModel } from '../../models/permission/permission.model';

@Component({
  selector: 'app-permission-manager',
  imports: [],
  templateUrl: './permission-manager.html'
})
export class PermissionManager {
  private readonly pageTitle = inject(PageTitle);
  private readonly roleService = inject(RoleService);
  private readonly permissionService = inject(PermissionService);

  errorMessage = '';
  isLoading = true;

  readonly tabRoles = signal<RoleModel[]>([]);
  readonly selectedRole = signal<RoleModel | null>(null);
  readonly allPermissions = signal<PermissionModel[]>([]);
  readonly selectedRolePermissionIds = signal<Set<number>>(new Set());
  readonly roleUserCounts = signal<Record<number, number>>({});
  readonly rolePermissionCounts = signal<Record<number, number>>({});

  // Modal "Créer un rôle"
  readonly isCreateRoleModalOpen = signal(false);
  readonly newRoleName = signal('');
  readonly newRoleColorHex = signal('#3b5bdb');
  isCreatingRole = false;

  permissionsDirty = false;

  ngOnInit() {
    this.pageTitle.title.set('Gestion des rôles et permissions');
    this.loadRoles();
  }

  loadRoles() {
    this.isLoading = true;
    this.roleService.getAllRoles().subscribe({
      next: (roles) => {
        this.tabRoles.set(roles);
        this.isLoading = false;
        this.loadRoleUserCounts();
        this.loadRolePermissionCounts();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des rôles', err);
        this.errorMessage = 'Impossible de charger les rôles.';
        this.isLoading = false;
      }
    });
  }

  loadRoleUserCounts() {
    this.roleService.countUsersForAllRoles().subscribe({
      next: (counts) => this.roleUserCounts.set(counts ?? {}),
      error: (err) => {
        console.warn('Impossible de charger le nombre de membres par rôle', err);
        this.roleUserCounts.set({});
      }
    });
  }

  loadRolePermissionCounts() {
    this.roleService.countPermissionsForAllRoles().subscribe({
      next: (counts) => this.rolePermissionCounts.set(counts ?? {}),
      error: (err) => {
        console.warn('Impossible de charger le nombre de permissions par rôle', err);
        this.rolePermissionCounts.set({});
      }
    });
  }

  loadPermissionsForRole(roleId: number) {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      all: this.permissionService.getAllPermissions(),
      rolePerms: this.roleService.getPermissionsForRole(roleId),
    }).subscribe({
      next: ({ all, rolePerms }) => {
        this.allPermissions.set(all);
        this.selectedRolePermissionIds.set(new Set(rolePerms.map(p => p.id)));
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des permissions', err);
        this.errorMessage = 'Impossible de charger les permissions.';
        this.isLoading = false;
      }
    });
  }

  selectRole(role: RoleModel) {
    this.selectedRole.set(role);
    this.permissionsDirty = false;
    this.loadPermissionsForRole(role.id);
  }

  isPermissionChecked(permissionId: number): boolean {
    return this.selectedRolePermissionIds().has(permissionId);
  }

  togglePermission(permissionId: number, checked: boolean) {
    if (permissionId == null) return;

    const current = new Set(this.selectedRolePermissionIds());

    if (checked) {
      current.add(permissionId);
    } else {
      current.delete(permissionId);
    }

    this.selectedRolePermissionIds.set(current);
    this.permissionsDirty = true;
  }

  openCreateRoleModal() {
    this.errorMessage = '';
    this.newRoleName.set('');
    this.newRoleColorHex.set('#3b5bdb');
    this.isCreateRoleModalOpen.set(true);
  }

  closeCreateRoleModal() {
    this.isCreateRoleModalOpen.set(false);
  }

  openNoSavedRoleModal() {
    this.errorMessage = '';
    this.newRoleName.set('');
    this.newRoleColorHex.set('#3b5bdb');
    this.isCreateRoleModalOpen.set(true);
  }

  closeNoSavedRoleModal() {
    this.isCreateRoleModalOpen.set(false);
  }

  selectNewRoleColor(colorHex: string) {
    this.newRoleColorHex.set(colorHex);
  }

  createRole() {
    const name = this.newRoleName().trim();
    const colorHex = this.newRoleColorHex();

    if (!name) {
      this.errorMessage = 'Le nom du rôle est obligatoire.';
      return;
    }

    this.isCreatingRole = true;
    this.roleService.createRole({ name, colorHex }).subscribe({
      next: (created) => {
        this.isCreatingRole = false;
        this.closeCreateRoleModal();
        this.loadRoles();
        this.selectRole(created);
      },
      error: (err) => {
        console.error('Erreur lors de la création du rôle', err);
        this.errorMessage = 'Impossible de créer le rôle.';
        this.isCreatingRole = false;
      },
    });
  }

  saveSelectedRolePermissions() {
    const role = this.selectedRole();
    if (!role) return;

    const permissionIds = Array.from(this.selectedRolePermissionIds());

    this.errorMessage = '';

    this.roleService.setRolePermissions(role.id, permissionIds).subscribe({
      next: () => {
        this.permissionsDirty = false;
        this.loadRolePermissionCounts();
        this.loadPermissionsForRole(role.id);
      },
      error: (err) => {
        console.error('Erreur lors de la sauvegarde des permissions', err);
        this.errorMessage = 'Impossible de sauvegarder les permissions.';
      }
    });
  }

  resetSelectedRolePermissions() {
    const role = this.selectedRole();
    if (!role) return;

    this.permissionsDirty = false;
    this.loadPermissionsForRole(role.id);
  }

  getSelectedRoleUserCount(): number {
    const role = this.selectedRole();
    if (!role) return 0;
    return this.roleUserCounts()[role.id] ?? 0;
  }

  getSelectedRolePermissionCount(): number {
    const role = this.selectedRole();
    if (!role) return 0;
    return this.rolePermissionCounts()[role.id] ?? 0;
  }
}
