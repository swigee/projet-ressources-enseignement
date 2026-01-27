import {Component, inject, signal} from '@angular/core';
import {UserModel} from '../../models/user/user-model';
import {UserService} from '../../services/user/user-service';
import {PageTitle} from '../../services/page-title/page-title';


@Component({
  selector: 'app-user-manager',
  templateUrl: './user-manager.html'
})
export class UserManager {
  private readonly  pageTitle = inject(PageTitle);
  private readonly userService = inject(UserService)

  readonly tabUsers= signal<UserModel[]>([]);

  ngOnInit() {
    this.pageTitle.title.set("Gestion des utilisateurs");
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe(users => {
      this.tabUsers.set(users);
    });
  }

}
