import { Injectable, signal } from '@angular/core';

export interface Publicacion {
  id: number;
  titulo: string;
  descripcion: string;
  precio: number;
  categoria: 'Venta' | 'Alquiler' | 'Temporal';
  lat: number;
  lng: number;
  imagen: string;
}

@Injectable({ providedIn: 'root' })
export class PublicacionesService {

  private _publicaciones = signal<Publicacion[]>([
    {
      id: 1,
      titulo: 'Apartamento Moderno Centro',
      descripcion: 'Hermoso apartamento renovado cerca de la plaza mayor.',
      precio: 1200,
      categoria: 'Alquiler',
      lat: 40.4168,
      lng: -3.7038,
      imagen: 'https://images.unsplash.com/...'
    },
    // ... el resto
  ]);

  publicaciones = this._publicaciones.asReadonly();

  getAll() {
    return this.publicaciones();
  }
}
