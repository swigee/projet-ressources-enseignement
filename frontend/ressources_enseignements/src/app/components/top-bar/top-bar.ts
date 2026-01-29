import {Component, inject, signal} from '@angular/core';
import { PageTitle } from '../../services/page-title/page-title-service';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  templateUrl: './top-bar.html',
})
export class TopBar {
  pageTitleService = inject(PageTitle);
  title = this.pageTitleService.title;
}
