import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth/auth';


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.html',
})
export class Login {
  loginForm: FormGroup;
  forgotPasswordForm: FormGroup;
  showPassword = signal(false);
  isForgotPasswordMode = signal(false);

  constructor(private fb: FormBuilder, private authService: Auth, private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });

    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  togglePassword() {
    this.showPassword.update(value => !value);
  }

  toggleForgotPasswordMode() {
    this.isForgotPasswordMode.update(value => !value);
    // Reset forms when switching? Optional but good UX.
    if (!this.isForgotPasswordMode()) {
      this.forgotPasswordForm.reset();
    } else {
      this.loginForm.reset();
    }
  }

  onForgotPasswordSubmit() {
    if (this.forgotPasswordForm.valid) {
      const email = this.forgotPasswordForm.value.email;
      // Simulate API call
      setTimeout(() => {
        alert(`Simulation : Un email de réinitialisation a été envoyé à ${email}`);
        this.toggleForgotPasswordMode(); // Go back to login
      }, 1000);
    }
  }
  onSubmit() {

    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      this.authService.login(username, password).subscribe({
        next: (user: any) => {
          this.router.navigate(['/dashboard']); // Redirection vers l'accueil
        },
        error: (err: any) => {
          alert('identifiant ou mot de passe incorrect');
        }
      });
    }
  }
}
