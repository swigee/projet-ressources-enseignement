import { Component, inject, signal, computed } from '@angular/core';
import { UserModel } from '../../models/user/user.model';
import { UserService } from '../../services/user/user-service';
import { PageTitle } from '../../services/page-title/page-title-service';

@Component({
  selector: 'app-user-manager',
  templateUrl: './user-manager.html'
})
export class UserManager {
  private readonly pageTitle = inject(PageTitle);
  private readonly userService = inject(UserService);

  readonly tabUsers = signal<UserModel[]>([]);
  readonly showFilter = signal(false);
  readonly showEditRole = signal(false);
  readonly searchName = signal('');
  readonly selectedRoles = signal<string[]>([]);


  readonly filteredUsers = computed(() => {
      return this.tabUsers().filter(user => {
        const fullName = `${user.firstname} ${user.lastname}`.toLowerCase();
        const matchName = fullName.includes(this.searchName().toLowerCase());

//         // On compare les idrole
//         const matchRole =
//           this.selectedRoles().length === 0 ||
//           user.roles?.some(role => this.selectedRoles().includes(role.idrole));

//         return matchName && matchRole;
        return matchName;
      });
    });

  ngOnInit() {
    this.pageTitle.title.set('Gestion des utilisateurs');
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe(users => {
      this.tabUsers.set(users);
      console.log(users)
    });
  }

//   toggleRole(roleId: number) {
//       const roles = this.selectedRoles();
//       if (roles.includes(roleId)) {
//         this.selectedRoles.set(roles.filter(r => r !== roleId));
//       } else {
//         this.selectedRoles.set([...roles, roleId]);
//       }
//     }

  getAllRoles() {
       const rolesMap = new Map<number, string>();
       if (this.tabUsers().length === 0) {
         return [];
       }
       this.tabUsers().forEach(user => {
         if (user.roles && Array.isArray(user.roles)) {
           user.roles.forEach(role => rolesMap.set(role.idrole, role.rights));
         }
       });
       return Array.from(rolesMap.entries()).map(([idrole, rights]) => ({ idrole, rights }));
     }


  resetFilters() {
    this.searchName.set('');
    //this.selectedRoles.set([]);
  }

  removeUser(id: number) {
    console.log("il est SUPPPRIMÉ");
    this.userService.deleteUser(id).subscribe(() => {
      this.loadUsers();
    });
  }

  editRoleUser(user: UserModel) {
//     console.log("il est MODIFIÉ");
//     // On utilise la propriété iduser et roles du UserModel
//     this.userService.updateUserRole(user.iduser!, user.roles ?? []).subscribe(() => {
//       this.loadUsers();
//     });
    this.showEditRole.set(true)
  }

  removeUserRole(iduser: number | undefined, idrole: number) {
    if (typeof iduser !== 'number' || typeof idrole !== 'number') return;
    this.userService.deleteUserRole(iduser, idrole).subscribe(() => {
      this.loadUsers();
    });
  }
}
