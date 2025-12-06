import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { RegistroRequest, LoginRequest, LoginResponse } from '../models/auth-request.interface';
import { Usuario } from '../models/usuario.interface';
import { HttpHeaders } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';
  private abrirRegistroSource = new Subject<void>();
  public abrirRegistro$ = this.abrirRegistroSource.asObservable();
  constructor(private http: HttpClient) { }

  public registrarUsuario(data: RegistroRequest): Observable<any> {
    console.log('Enviando datos de registro:', data);
    return this.http.post(`${this.apiUrl}/registro`, data);
  }

  public login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/autenticacion`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('usuario', JSON.stringify(response.email));
        }
      })
    );
  }

  public estaLogueado(): boolean {
    return !!localStorage.getItem('token');
  }

  public logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
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
}
