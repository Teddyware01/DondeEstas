import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';

import { ActivatedRoute, Router, RouterModule } from '@angular/router'; // Agregado Router

import { FormsModule } from '@angular/forms'; // Agregado FormsModule

import { MascotaService } from '../../../../services/mascota.service';

import { UsuarioService } from '../../../../services/usuario.service';

import { Mascota } from '../../../../models/mascota.interface';
import { ChangeDetectorRef } from '@angular/core';

@Component({

  selector: 'app-detalle-mascota',

  standalone: true,

  imports: [

    CommonModule,

    RouterModule,

    FormsModule // Necesario para [(ngModel)] en el modal

  ],

  templateUrl: './mascota.component.html',

  styleUrls: ['./mascota.component.css']

})

export class MascotaComponent implements OnInit {


  mascota!: Mascota;

  cargando = true;

  errorCarga = false;


// Variables para carrusel

  indiceImagenActual = 0;

  imagenSeleccionada: string = '';

  imagenes: string[] = [];


// Variables para Modales y Edición

  mostrarModalEditar = false;

  mostrarModalEliminar = false;

  mascotaForm: any = {}; // Objeto copia para el formulario


// Control de dueño (Lógica pendiente por tu parte, por defecto true para ver botones)

  esDuenio = false;


// Opciones para el select de estado

  estadosPosibles = [

    { clave: 'PERDIDO_PROPIO', label: 'Perdido' },

    { clave: 'ENCONTRADO', label: 'Encontrado' },

    { clave: 'ADOPTADO', label: 'Adoptado' },

    { clave: 'RECUPERADO', label: 'Recuperado' }

  ];


  constructor(

    private route: ActivatedRoute,

    private router: Router, // Inyectamos Router para navegar

    private mascotaService: MascotaService,

    private usuarioService:UsuarioService,
    private cd:ChangeDetectorRef,

  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.mascotaService.obtenerPorId(id).subscribe({
      next: (data) => {
        if (!data) {
          this.errorCarga = true;
          this.cargando = false;
          return;
        }

        this.mascota = data;

        // ... tu lógica de imágenes ...
        if (data.imagenesBase64 && data.imagenesBase64.length > 0) {
          this.imagenes = data.imagenesBase64;
        } else {
          this.imagenes = ['/images/placeholder-pet.png'];
        }
        this.actualizarImagenPrincipal();

        // --- CORRECCIÓN AQUÍ ---
        // Calculamos si es dueño AHORA que ya tenemos los datos de la mascota
        this.verificarDuenio();
        // -----------------------

        this.cargando = false;
        this.cd.detectChanges(); // Esto actualizará la vista con el nuevo valor de esDuenio
      },
      error: (err) => {
        console.error('Error', err);
        this.errorCarga = true;
        this.cargando = false;
      }
    });
  }

// Ya no necesitas ngAfterViewInit para esto, puedes borrarlo o dejarlo vacío
  ngAfterViewInit() {}

// Crea un método helper para mantener el código limpio
  verificarDuenio(): void {
    const usuarioLogueadoId = this.usuarioService.obtenerUsuarioId();
console.log("soy",usuarioLogueadoId)
    // Validamos que tengamos ambos datos antes de comparar
    if (usuarioLogueadoId && this.mascota && this.mascota.usuarioId) {
      // Usamos '==' por si uno es string y el otro number, o '===' si estás seguro del tipo
      this.esDuenio = (usuarioLogueadoId == this.mascota.usuarioId);

      console.log("Soy dueño?:", this.esDuenio);
    } else {
      this.esDuenio = true;
    }
  }



// --- Lógica del Carrusel ---


  actualizarImagenPrincipal(): void {

    this.imagenSeleccionada = this.imagenes[this.indiceImagenActual];

  }


  anteriorImagen(): void {

    this.indiceImagenActual =

      (this.indiceImagenActual - 1 + this.imagenes.length) % this.imagenes.length;

    this.actualizarImagenPrincipal();

  }


  siguienteImagen(): void {

    this.indiceImagenActual =

      (this.indiceImagenActual + 1) % this.imagenes.length;

    this.actualizarImagenPrincipal();

  }


// --- Navegación y Contacto ---


  volver(): void {

    this.router.navigate(['/todas-mascotas']); // Ajusta la ruta a tu listado

  }


  contactar(): void {

    if (this.mascota.telefono) {

// Opción 1: Mostrar alerta simple

      alert(`Contactar al dueño: ${this.mascota.telefono}`);


    } else {

// Caso: No hay teléfono

      alert('El usuario no agregó un medio de contacto.');

    }

  }


// --- Lógica Modal Editar ---


  abrirModalEditar(): void {

// Creamos una copia para no modificar la vista hasta guardar

    this.mascotaForm = { ...this.mascota };

    this.mostrarModalEditar = true;


// Si vas a usar mapa en el modal, inicializalo aquí con un setTimeout

  }


  cerrarModalEditar(): void {

    this.mostrarModalEditar = false;

  }


  guardarEdicion(): void {

// Aquí llamarías al servicio update

    console.log('Guardando cambios...', this.mascotaForm);

    this.mascotaService.editarMascota(this.mascota.id, this.mascotaForm).subscribe(updated => {

    this.mascota = updated;

    this.cerrarModalEditar();

    });



    this.mascota = { ...this.mascotaForm };

    this.cerrarModalEditar();

  }


// --- Lógica Modal Eliminar ---


  abrirModalEliminar(): void {

    this.mostrarModalEliminar = true;

  }


  cerrarModalEliminar(): void {

    this.mostrarModalEliminar = false;

  }


  confirmarEliminacion(): void {

    console.log('Eliminando mascota...', this.mascota.id);


    this.mascotaService.desactivarMascota(this.mascota.id).subscribe(() => {

    this.cerrarModalEliminar();

    this.router.navigate(['/todas-mascotas']);

    });

    this.cerrarModalEliminar();
    this.router.navigate(['/todas-mascotas']);




  }

}

