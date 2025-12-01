import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PedagogicalScheduleComponent } from "./components/pedagogical-schedule/pedagogical-schedule";
import { RouterModule} from '@angular/router';
import { Navbar } from './navbar/navbar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, Navbar, PedagogicalScheduleComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('ressources_enseignement');
}
