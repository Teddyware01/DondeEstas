import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../../services/usuario.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-main-layout',
  standalone: false,
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent implements OnInit, OnDestroy {
  mostrarRegistro: boolean = false;
  private subscription: Subscription = new Subscription();

  constructor(public router: Router, public usuarioService: UsuarioService) {}

  logout(): void {
    console.log('Cerrando sesión...');
    this.router.navigate(['/login']);
  }

  abrirModalRegistro(): void {
    this.mostrarRegistro = true;
  }

  cerrarModalRegistro(): void {
    this.mostrarRegistro = false;
  }

  esPaginaLogin(): boolean {
    return this.router.url === '/login';
  }

  ngOnInit(): void {
    this.subscription = this.usuarioService.abrirRegistro$.subscribe(() => {
      this.mostrarRegistro = true;
    });
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  isLogged(): boolean {
    return this.usuarioService.estaLogueado();
  }

  onLogout(): void {
    this.usuarioService.logout();
    this.router.navigate(['/login']);
  }

  idUsuario(): number {
    return <number>this.usuarioService.obtenerUsuarioId();
  }

}
