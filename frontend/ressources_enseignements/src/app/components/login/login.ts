import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  loginForm: FormGroup

  constructor(private fb: FormBuilder, private authService: Auth, private router: Router) {



    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    }
    );
  }
  onSubmit(){

        if (this.loginForm.valid) {
          const { username, password } = this.loginForm.value;

          this.authService.login(username, password).subscribe({
            next: (user:any) => {
              console.log('Connexion réussie !', user);
              this.router.navigate(['/dashboard']); // Redirection vers l'accueil
            },
            error: (err:any) => {
              console.error('Erreur de connexion', err);
              alert('identifiant ou mot de passe incorrect');
            }
          });
        }
  }
}
