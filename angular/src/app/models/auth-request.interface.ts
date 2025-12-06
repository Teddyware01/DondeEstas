export interface RegistroRequest {
  nombre: string;
  apellido: string;
  email: string;
  contrasena: string;
  telefono: string;
  barrio: string;
  ciudad: string;
}

export interface LoginRequest {
  email: string;
  contrasena: string;
}

export interface LoginResponse {
  token: string;
  expiration: number;
  email: string;
  id: number;
}
