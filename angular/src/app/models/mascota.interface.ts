export interface Mascota {
  id: number;
  nombre: string;
  raza: string;
  fechaPerdida: string | Date;
  fotoUrl: string;
  ubicacion?: string;
  estado: string;
}
