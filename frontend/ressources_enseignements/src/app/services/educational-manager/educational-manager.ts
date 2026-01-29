import { inject, Injectable, signal } from '@angular/core';
import { Education } from '../../models/education/education.model';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class EducationalManagerService {

  private readonly http = inject(HttpClient)

  api = "http://localhost:8080/api/education-manager"

  private readonly educationListDatabase = signal<Education[]>([]);
  educationList = this.educationListDatabase.asReadonly()

  constructor(private router: Router){
    this.loadEducations();
  }

  loadEducations(){
    this.http
    .get<Education[]>(`${this.api}/list`)
    .subscribe(t => this.educationListDatabase.set(t));
  }

  deleteEducation(id: number){
     this.http
    .delete<Education[]>(`${this.api}/${id}`)
    .subscribe(() => this.loadEducations());
  }

  createEducation(education: Education){
    console.log(education)
    this.http
    .post<Education>(`${this.api}`, education)
    .subscribe(() => {
      this.router.navigate(['/education-manager']);
      this.loadEducations();
    });
  }
  updateEducation(id: number){
    this.http
    .get<Education>(`${this.api}/${id}`)
    .subscribe(() => {
      this.router.navigate(['/education-manager/create']);

      this.loadEducations();
    })
    }
}
