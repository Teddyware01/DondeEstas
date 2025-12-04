import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Mascota } from '../models/mascota.interface';

@Injectable({
  providedIn: 'root'
})
export class MascotaService {

  private apiUrl = 'http://localhost:8080/api/mascotas';

  constructor(private http: HttpClient) { }

  obtenerMascotasPerdidas(): Observable<Mascota[]> {
    const mascotasMock: Mascota[] = [
      { id: 1, nombre: 'Firulais', raza: 'Labrador', fechaPerdida: '2023-10-15', fotoUrl: 'https://placedog.net/300/300?id=1', estado: 'PERDIDO', ubicacion: 'Belgrano' },
      { id: 2, nombre: 'Rex', raza: 'Ovejero', fechaPerdida: '2023-10-20', fotoUrl: 'https://placedog.net/300/300?id=2', estado: 'PERDIDO', ubicacion: 'Palermo' },
      { id: 3, nombre: 'Lola', raza: 'Caniche', fechaPerdida: '2023-11-01', fotoUrl: 'https://placedog.net/300/300?id=3', estado: 'PERDIDO', ubicacion: 'Centro' },
      { id: 4, nombre: 'Rocky', raza: 'Bulldog', fechaPerdida: '2023-11-05', fotoUrl: 'https://placedog.net/300/300?id=4', estado: 'PERDIDO', ubicacion: 'Caballito' },
      { id: 5, nombre: 'Luna', raza: 'Mestizo', fechaPerdida: '2023-11-10', fotoUrl: 'https://placedog.net/300/300?id=5', estado: 'PERDIDO', ubicacion: 'La Plata' },
      { id: 6, nombre: 'Coco', raza: 'Golden', fechaPerdida: '2023-11-12', fotoUrl: 'https://placedog.net/300/300?id=6', estado: 'PERDIDO', ubicacion: 'Tigre' },
    ];

    return of(mascotasMock);
    // return this.http.get<Mascota[]>(`${this.apiUrl}/perdidas`);
  }
}
