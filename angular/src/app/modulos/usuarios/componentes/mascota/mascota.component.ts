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

// Validamos que data no sea null

        if (!data) {

          this.errorCarga = true;

          this.cargando = false;
          this.cd.detectChanges(); // <<--- Forzamos actualización
          return;

        }


        this.mascota = data;


// --- CORRECCIÓN AQUÍ ---

// Verificamos que exista Y que tenga elementos

        if (data.imagenesBase64 && data.imagenesBase64.length > 0) {

          this.imagenes = data.imagenesBase64;

          console.log("ENTRE")

        } else {

          this.imagenes = ['/images/placeholder-pet.png'];

        }

// -----------------------


        this.actualizarImagenPrincipal();

        this.cargando = false; // Importante: quitar el loading aquí
        this.cd.detectChanges(); // <<--- Forzamos actualización


      },

      error: (err) => {

        console.error('Error al cargar mascota:', err);

        this.errorCarga = true;

        this.cargando = false;

      }

    });

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

  ngAfterViewInit() {
    this.esDuenio = this.usuarioService.estaLogueado() &&  (this.usuarioService.obtenerUsuarioId() == this.mascota.usuarioId);
    console.log("ESDUENIO:", this.esDuenio)
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


// Simulación de guardado exitoso:

    /*

    this.mascotaService.editar(this.mascota.id, this.mascotaForm).subscribe(updated => {

    this.mascota = updated;

    this.cerrarModalEditar();

    });

    */


// Actualizamos localmente para ver el efecto (borrar esto al conectar backend)

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


    /* this.mascotaService.eliminar(this.mascota.id).subscribe(() => {

    this.cerrarModalEliminar();

    this.router.navigate(['/mascotas']);

    });

    */


// Simulación (borrar al conectar backend)

    this.cerrarModalEliminar();

    this.router.navigate(['/mascotas']);

  }

}

