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
    return this.http.get<Mascota[]>(`${this.apiUrl}/perdidas`);

  }

  obtenerMascotasTodas(): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${this.apiUrl}/todos`);

  }

  obtenerPorId(id: number): Observable<Mascota> {
    return this.http.get<Mascota>(`${this.apiUrl}/${id}`);
  }

  crearMascota(mascota: FormData): Observable<Mascota> {
    return this.http.post<Mascota>(`${this.apiUrl}`, mascota);
  }

  editarMascota(id: number | undefined, mascota: Mascota): Observable<Mascota> {
    return this.http.put<Mascota>(`${this.apiUrl}/${id}`, mascota);
  }

  desactivarMascota(id: number | undefined): Observable<Mascota> {

    return this.http.put<Mascota>(`${this.apiUrl}/desactivar/${id}`, {}, );
  }

}
