import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private apiUrl = 'http://localhost:8080/api/auth';

  // C'est ça la "variable globale". BehaviorSubject permet d'écouter les changements en temps réel.
  // null = pas connecté.
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  // Méthode pour se connecter
  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(user => {
        // Si le backend répond OK, on stocke l'utilisateur
        this.currentUserSubject.next(user);
        // On peut aussi stocker le token en localStorage ici si besoin
        localStorage.setItem('user', JSON.stringify(user));
      })
    );
  }

  // Méthode pour se déconnecter
  logout() {
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  // Utile pour savoir si on est connecté
  isAuthenticated(): boolean {
    return this.currentUserSubject.value !== null;
  }
}
