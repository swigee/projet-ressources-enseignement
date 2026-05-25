import { Component, inject } from '@angular/core'; 
import { RouterLink, RouterLinkActive, RouterModule } from '@angular/router';
import { Auth } from '../../services/auth/auth'; 
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, RouterModule, CommonModule], 
  templateUrl: './navbar.html',
})
export class Navbar {
  auth = inject(Auth);

  logout() {
    this.auth.logout();
  }
}
