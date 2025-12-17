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

  //ubicacion queda para enviar lat y long juntos.
  ubicacion?:string;

  provincia?:string;
  departamento?:string;
  municipio?:string;
  imagenesBase64?: string[];
}
