import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {TopBar} from './components/top-bar/top-bar';
import { RouterModule} from '@angular/router';
import { Navbar } from './navbar/navbar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TopBar, RouterModule, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
