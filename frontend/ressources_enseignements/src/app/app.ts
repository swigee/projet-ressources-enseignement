import { Component } from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import { RouterModule} from '@angular/router';
import { Navbar } from './components/navbar/navbar';
import { TopBar } from "./components/top-bar/top-bar";
import {filter} from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, Navbar, TopBar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  showNavBar: boolean = true;

  constructor(private router: Router) {}

  ngOnInit() {

    const hiddenRoutes = ['/login'];

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {

      // On regarde si l'URL actuelle contient l'un des mots de la liste
      const isHidden = hiddenRoutes.some(route => event.urlAfterRedirects.includes(route));

      if (isHidden) {
        this.showNavBar = false;
      } else {
        this.showNavBar = true;
      }

    });
  }
}
