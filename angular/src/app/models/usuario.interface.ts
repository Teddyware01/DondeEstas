export interface Usuario {
  id?: number;
  nombre: string;
  apellido: string;
  email: string; // hacerlo obligatorio y not null
  telefono: string;
  barrio?: string;
  ciudad?: string;
  isAdmin: boolean;
  puntajes?: number;
  medallas?: string[];
  // falta la contra
}
