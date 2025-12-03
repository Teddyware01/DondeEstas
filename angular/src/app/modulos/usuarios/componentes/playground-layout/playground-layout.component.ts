import { Component } from '@angular/core';

@Component({
  selector: 'app-playground-layout',
  standalone: false,
  templateUrl: './playground-layout.component.html',
  styleUrls: ['./playground-layout.component.css']
})
export class PlaygroundLayoutComponent {

  usuariosDePrueba = [
    { id: 1, nombre: 'Mock User A', rol: 'Administrador' },
    { id: 2, nombre: 'Mock User B', rol: 'Cliente' }
  ];

  constructor() { }
}
