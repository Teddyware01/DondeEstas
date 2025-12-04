import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  // Simulamos datos para que no te de error visualmente por ahora
  publicaciones = [
    {
      id: 1,
      nombre: 'Firulais',
      descripcion: 'Se perdió cerca de la plaza. Tiene collar rojo.',
      imagen: 'https://placedog.net/300/200?id=1', // Imagen de prueba
      ubicacion: 'Palermo, CABA'
    },
    {
      id: 2,
      nombre: 'Rex',
      descripcion: 'Encontré este perrito asustado. Busco a sus dueños.',
      imagen: 'https://placedog.net/300/200?id=2',
      ubicacion: 'Belgrano, CABA'
    },
    {
      id: 3,
      nombre: 'Lola',
      descripcion: '¡Lola ya tiene un nuevo hogar!',
      imagen: 'https://placedog.net/300/200?id=3',
      ubicacion: 'La Plata, PBA'
    }
  ];

  constructor() { }

  ngOnInit(): void {
    console.log('Dashboard cargado correctamente');
  }
}
