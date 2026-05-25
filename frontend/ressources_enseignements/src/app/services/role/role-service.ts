import {inject, Injectable, signal} from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoleModel } from '../../models/role/role.model';
import { PermissionModel } from '../../models/permission/permission.model';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private apiUrl = `${environment.apiUrl}/api/roles`;
  private readonly http = inject(HttpClient);

  getAllRoles(): Observable<RoleModel[]> {
    return this.http.get<RoleModel[]>(`${this.apiUrl}/list`);
  }

  getRoleById(id: number): Observable<RoleModel> {
    return this.http.get<RoleModel>(`${this.apiUrl}/${id}`);
  }

  createRole(role: Partial<RoleModel>): Observable<RoleModel> {
    return this.http.post<RoleModel>(`${this.apiUrl}`, role);
  }

  updateRole(id: number, role: Partial<RoleModel>): Observable<RoleModel> {
    return this.http.put<RoleModel>(`${this.apiUrl}/${id}`, role);
  }

  deleteRole(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  countUsersForRole(roleId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${roleId}/users/count`);
  }

  countUsersForAllRoles(): Observable<Record<number, number>> {
    return this.http.get<Record<number, number>>(`${this.apiUrl}/users/count`);
  }

  getPermissionsForRole(roleId: number): Observable<PermissionModel[]> {
    return this.http.get<PermissionModel[]>(`${this.apiUrl}/${roleId}/permissions`);
  }

  countPermissionsForRole(roleId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${roleId}/permissions/count`);
  }

  countPermissionsForAllRoles(): Observable<Record<number, number>> {
    return this.http.get<Record<number, number>>(`${this.apiUrl}/permissions/count`);
  }

  /** Remplace la liste de permissions d'un rôle (sauvegarde) */
  setRolePermissions(roleId: number, permissionIds: number[]): Observable<RoleModel> {
    return this.http.put<RoleModel>(`${this.apiUrl}/${roleId}/permissions`, permissionIds);
  }
}
