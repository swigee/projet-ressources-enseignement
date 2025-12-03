import {Component} from '@angular/core';
import {PageTitle} from '../../services/page-title';


@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  constructor(private pageTitle: PageTitle) {
  }

  ngOnInit() {
    this.pageTitle.title.set("Dashboard");
  }
}

