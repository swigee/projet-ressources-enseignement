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

  logout() {
    this.authService.logout();
  }
}
