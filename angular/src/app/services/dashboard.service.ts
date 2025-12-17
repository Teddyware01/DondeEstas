import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardStats } from '../models/dashboard.interface'; // Asegúrate de importar la interfaz

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  // Idealmente esto viene de tus environment.ts
  private apiUrl = 'http://localhost:8080/api/dashboard'; // Ajusta a tu puerto real

  constructor(private http: HttpClient) { }

  obtenerEstadisticas(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/stats`);
  }
}
