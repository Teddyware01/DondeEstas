import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  standalone: false,
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent {
  mostrarRegistro: boolean = false;
  constructor(private router: Router) {}

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
}
