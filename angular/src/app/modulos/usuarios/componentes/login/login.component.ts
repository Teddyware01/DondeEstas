import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { LoginRequest } from '../../../../models/auth-request.interface';
import { Router } from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: LoginRequest = {
    email: '',
    contrasena: ''
  };

  errorLogin: string | null = null;

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.errorLogin = null;

    this.usuarioService.login(this.model).subscribe({
      next: (response) => {
        console.log('Login exitoso', response);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.errorLogin = 'Credenciales inválidas o error de conexión.';
        console.error('Error durante el login:', err);
      }
    });
  }
}

// Recuerda que para usar [(ngModel)] en este componente,
// el módulo 'UsuarioModule' debe importar FormsModule.
/*
<form (ngSubmit)="onSubmit()" #loginForm="ngForm">
  <div class="form-group">
    <label for="email">Email</label>
    <input type="email" class="form-control" id="email"
           required [(ngModel)]="model.email" name="email">
  </div>
  <div class="form-group">
    <label for="contrasena">Contraseña</label>
    <input type="password" class="form-control" id="contrasena"
           required [(ngModel)]="model.contrasena" name="contrasena">
  </div>
  <div *ngIf="errorLogin" class="alert alert-danger">{{ errorLogin }}</div>

  <button type="submit" class="btn btn-primary"
          [disabled]="!loginForm.form.valid">Ingresar</button>

  <p>¿No tienes cuenta? <a routerLink="/usuarios/registro">Regístrate aquí</a></p>
</form>
*/
