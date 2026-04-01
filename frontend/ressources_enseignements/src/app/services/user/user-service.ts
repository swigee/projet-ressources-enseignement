import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import { UserModel } from '../../models/user/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';
  private readonly http = inject(HttpClient);

  constructor() {}

  getAllUsers(): Observable<UserModel[]> {
    return this.http.get<any[]>(`${this.apiUrl}/list`);
  }

  getUserById(id: number): Observable<UserModel> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  updateUserRole(iduser: number, roles: number[]): Observable<any> {
    return this.http.put(`${this.apiUrl}/${iduser}/roles`, { roles });
  }

  deleteUserRole(iduser: number, idrole: number): Observable<UserModel> {
    return this.http.delete<UserModel>(`${this.apiUrl}/${iduser}/roles`, { body: { idrole } });
  }

  deleteAllUserRole(iduser: number): Observable<UserModel> {
      return this.http.delete<UserModel>(`${this.apiUrl}/${iduser}/allroles`);
    }
  validateService(userId: number, comment?: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/validate`, { comment: comment ?? null });
  }
}

export default UserService
