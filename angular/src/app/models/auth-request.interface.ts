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
