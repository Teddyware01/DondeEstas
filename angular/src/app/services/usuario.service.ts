import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { RegistroRequest, LoginRequest, LoginResponse } from '../models/auth-request.interface';
import { Usuario } from '../models/usuario.interface';
import { HttpHeaders } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RankingEntry } from '../models/ranking.interface';
import { map } from 'rxjs/operators';

  @Injectable({
  providedIn: 'root'
})

export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';
  private abrirRegistroSource = new Subject<void>();
  public abrirRegistro$ = this.abrirRegistroSource.asObservable();

  constructor(private http: HttpClient,
              @Inject(PLATFORM_ID) private platformId: Object) { }


  public registrarUsuario(data: RegistroRequest): Observable<any> {
    console.log('Enviando datos de registro:', data);
    return this.http.post(`${this.apiUrl}`, data);
  }

  public login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/autenticacion`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('usuario', JSON.stringify(response.email));
          localStorage.setItem('id', JSON.stringify(response.id));
        }
      })
    );
  }

  public estaLogueado(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('token');
    }
    return false;
  }

  public logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
      localStorage.removeItem('usuario');
    }
  }

  public editarPerfil(id: number, data: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, data);
  }

  public obtenerPerfil(id: number): Observable<Usuario> {
    const tokenSimulado = id.toString() + '123456';
    const headers = new HttpHeaders().set('token', tokenSimulado);
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`, { headers: headers });
  }

  public borrarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  public solicitarRegistro(): void {
    this.abrirRegistroSource.next();
  }

  public obtenerUsuarioId(): number | null {
    if (isPlatformBrowser(this.platformId)) {
      const id = localStorage.getItem('id');
      return id ? Number(JSON.parse(id)) : null;
    }
    return null;
  }

    public obtenerRanking(): Observable<RankingEntry[]> {
      const rankingUrl = 'http://localhost:8080/api/ranking';

      return this.http.get<any[]>(rankingUrl).pipe(
        map(rankingItemsDesdeJava => {
          console.log('Datos recibidos del backend:', rankingItemsDesdeJava); // <-- Log 1

          // Verificamos si el array es nulo o no un array
          if (!Array.isArray(rankingItemsDesdeJava)) {
            console.error('El backend no retornó un array.');
            return []; // Retornar array vacío para evitar errores
          }

          const listaMapeada = rankingItemsDesdeJava.map(item => {
            // Validación de existencia de 'usuario' y 'totalPuntos'
            const nombre = item.usuario?.nombre || 'N/A';
            const apellido = item.usuario?.apellido || 'N/A';
            const puntos = item.totalPuntos || 0;

            return { nombre, apellido, puntos } as RankingEntry;
          });

          console.log('Datos transformados para Angular:', listaMapeada); // <-- Log 2
          return listaMapeada;
        })
      );
    }
}
