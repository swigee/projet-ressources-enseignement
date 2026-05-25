import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PermissionModel} from '../../models/permission/permission.model';

@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  private readonly apiUrl = 'http://localhost:8080/api/permissions';
  private readonly http = inject(HttpClient);

  getAllPermissions(): Observable<PermissionModel[]> {
    return this.http.get<PermissionModel[]>(`${this.apiUrl}/list`);
  }

  getPermissionById(id: number): Observable<PermissionModel> {
    return this.http.get<PermissionModel>(`${this.apiUrl}/${id}`);
  }

  createPermission(permission: Partial<PermissionModel>): Observable<PermissionModel> {
    return this.http.post<PermissionModel>(`${this.apiUrl}`, permission);
  }

  updatePermission(id: number, permission: Partial<PermissionModel>): Observable<PermissionModel> {
    return this.http.put<PermissionModel>(`${this.apiUrl}/${id}`, permission);
  }

  deletePermission(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
