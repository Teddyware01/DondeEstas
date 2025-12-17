export interface Mascota {
  id?: number;
  nombre: string;
  tamano: string;
  color: string;
  raza?: string;
  fechaPerdida: string | Date;
  ubicacion: string; // dsps pasar a objeto estoas
  estado: string;
  descripcionExtra?: string;
  usuarioId?: number;

  imagenBase64?: string;
}
