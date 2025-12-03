import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { LoginRequest } from '../../../../models/auth-request.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
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
