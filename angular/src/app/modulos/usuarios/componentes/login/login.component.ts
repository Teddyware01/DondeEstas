import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { LoginRequest } from '../../../../models/auth-request.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: 'login.component.css'
})
export class LoginComponent implements OnInit {

  model: LoginRequest = {
    email: '',
    contrasena: ''
  };

  errorLogin: string | null = null;
  errorMensaje: string = '';

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  ngOnInit(): void {
  }
  onLogin(): void {
    this.errorMensaje = '';
    this.usuarioService.login(this.model).subscribe({
      next: (data) => {
        this.router.navigate(['/todas-mascotas']);
      },
      error: (err) => {
        console.error('Error:', err);
        this.errorMensaje = 'Credenciales incorrectas o error de servidor';
      }
    });
  }
  abrirRegistroClick(): void {
    this.usuarioService.solicitarRegistro();
  }
}
