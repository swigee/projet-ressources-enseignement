import { inject, Injectable, signal } from '@angular/core';
import { Education } from '../models/education.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class EducationalManager {
  
  private readonly http = inject(HttpClient)

  api = "http://localhost:8080/education-manager"

  private readonly educationListDatabase = signal<Education[]>([]);
  educationList = this.educationListDatabase.asReadonly()

  constructor() {
    this.loadEducations();
  }

  loadEducations(){
    this.http
    .get<Education[]>(`${this.api}/list`)
    .subscribe(t => this.educationListDatabase.set(t));
  }
}
