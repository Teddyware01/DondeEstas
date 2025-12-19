export interface Mascota {
  id?: number;
  nombre: string;
  tamano: string;
  color: string;
  raza?: string;
  fecha: string | Date;
  estado: string;
  descripcionExtra?: string;
  usuarioId?: number;
  telefono?:string;
  ubicacion?:string;
  tipoAnimal:string;
  provincia?:string;
  activo?:boolean;
  departamento?:string;
  municipio?:string;
  imagenesBase64?: string[];
}
