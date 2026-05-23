import {inject, Injectable, signal} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, of, tap} from 'rxjs';
import { RoleModel } from '../../models/role/role.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private apiUrl = `${environment.apiUrl}/api/roles`;
  private readonly http = inject(HttpClient);

  constructor() {}

  getAllRoles(): Observable<RoleModel[]> {
    return this.http.get<any[]>(`${this.apiUrl}/list`);
  }

}
