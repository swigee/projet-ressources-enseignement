import { Component, inject, signal } from '@angular/core';
import { PageTitle } from '../../services/page-title/page-title-service';
import { Auth } from '../../services/auth/auth';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  templateUrl: './top-bar.html',
})
export class TopBar {
  pageTitleService = inject(PageTitle);
  authService = inject(Auth);
  title = this.pageTitleService.title;

  readonly currentUser = signal<any>(null);

  constructor() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser.set(user);
    });
  }

  getRole() {
    if (this.currentUser().roleList.length != 0) {
      return this.currentUser().roleList[0].name;
    }
    return "";
  }

  isConnected() {
    return this.currentUser() != null
  }

  logout() {
    this.authService.logout();
  }

}
