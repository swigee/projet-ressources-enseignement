import { Component, inject } from '@angular/core'; // Added inject
import { RouterLink, RouterLinkActive, RouterModule } from '@angular/router';
import { Auth } from '../../services/auth/auth'; // Added Auth import
import { CommonModule } from '@angular/common'; // Added CommonModule for *ngIf

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, RouterModule, CommonModule], // Added CommonModule
  templateUrl: './navbar.html',
})
export class Navbar {
  auth = inject(Auth);

  logout() {
    this.auth.logout();
  }
}
