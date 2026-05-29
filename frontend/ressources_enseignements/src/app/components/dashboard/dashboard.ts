import {Component, inject, signal} from '@angular/core';
import {PageTitle} from '../../services/page-title/page-title-service';
import {RessourcesService} from '../../services/ressources/ressources-service';
import {ResourcesTableResponse} from '../../models/ressources/ressources.model';
import {RoleModel} from '../../models/role/role.model';


@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
})
export class Dashboard {
  private readonly ressourcesService:RessourcesService = inject(RessourcesService);
  hourForecast = signal<number>(0);
  isLoading = true;

  constructor(private pageTitle: PageTitle) {
  }

  ngOnInit() {
    this.pageTitle.title.set("Tableau de bord");
    this.loadData();
  }

  loadData(){
    this.isLoading = true;
    this.ressourcesService.getResourcesTable().subscribe( (data: ResourcesTableResponse) =>{
      this.hourForecast.set(data.availableTeachers[0].assignedHours);
      this.isLoading = false;
      console.log(data)
    })
  }

}

