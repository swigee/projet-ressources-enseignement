import {Component} from '@angular/core';
import {PageTitle} from '../../services/page-title/page-title-service';


@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
})
export class Dashboard {
  constructor(private pageTitle: PageTitle) {
  }

  ngOnInit() {
    this.pageTitle.title.set("Dashboard");
  }
}

