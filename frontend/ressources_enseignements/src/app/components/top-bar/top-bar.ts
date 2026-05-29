import { Component, inject, OnInit, signal } from '@angular/core';
import { PageTitle } from '../../services/page-title/page-title-service';
import { Auth } from '../../services/auth/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './top-bar.html',
})
export class TopBar implements OnInit {
  pageTitleService = inject(PageTitle);
  authService = inject(Auth);

  title = this.pageTitleService.title;
  readonly currentUser = signal<any>(null);

  constructor() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser.set(user);
    });
  }

  ngOnInit() {
  }

  getRole() {
    if (this.currentUser() && this.currentUser().roles && this.currentUser().roles.length != 0) {
      return this.currentUser().roles[0].name;
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
