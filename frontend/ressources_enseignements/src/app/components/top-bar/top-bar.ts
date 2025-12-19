import {Component, inject, signal} from '@angular/core';
import { PageTitle } from '../../services/page-title';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  templateUrl: './top-bar.html',
  styleUrl: './top-bar.css'
})
export class TopBar {
  pageTitleService = inject(PageTitle);
  title = signal(this.pageTitleService.title());
}
