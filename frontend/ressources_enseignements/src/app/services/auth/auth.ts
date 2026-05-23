import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

import { User } from '../../interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private apiUrl = `${environment.apiUrl}/api/auth`;

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    if (typeof localStorage !== 'undefined') {
      const savedUser = localStorage.getItem('user');
      if (savedUser) {
        try {
          const user = JSON.parse(savedUser);
          this.currentUserSubject.next(user);
        } catch (e) {
          // Error parsing user, ignore
        }
      }
    }
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(user => {
        this.currentUserSubject.next(user);
        localStorage.setItem('user', JSON.stringify(user));
        localStorage.setItem('userId', user.id.toString());
      })
    );
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const isAuth = this.currentUserSubject.value !== null;

    if (isAuth) {
      return true;
    }

    if (typeof localStorage !== 'undefined') {
      const savedUser = localStorage.getItem('user');
      if (savedUser) {
        try {
          const user = JSON.parse(savedUser);
          this.currentUserSubject.next(user); // Restore state
          return true;
        } catch (e) {
          return false;
        }
      }
    }
    return false;
  }

  hasRole(requiredRoles: string[]): boolean {
    const user = this.currentUserSubject.value;
    if (!user || !user.roleList) {
      return false;
    }
    // Check if user has at least one of the required roles
    return user.roleList.some(role => requiredRoles.includes(role.name));
  }
}
