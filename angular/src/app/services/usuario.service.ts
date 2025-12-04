import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegistroRequest, LoginRequest } from '../models/auth-request.interface';
import { Usuario } from '../models/usuario.interface';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';
  private authUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  public registrarUsuario(data: RegistroRequest): Observable<any> {
    console.log('Enviando datos de registro:', data);
    return this.http.post(`${this.apiUrl}/registro`, data);
  }

  public login(credentials: LoginRequest): Observable<any> {
    return this.http.post(`${this.authUrl}/login`, credentials);
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
}
