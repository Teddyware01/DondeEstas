import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MascotaService } from '../../../../services/mascota.service';
import { Mascota } from '../../../../models/mascota.interface';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './todas-mascotas.component.html',
  styleUrls: ['./todas-mascotas.component.css']
})
export class TodasMascotasComponent implements OnInit {
  mostrarModal: boolean = false;
  modoEdicion: boolean = false;
  listaMascotas: Mascota[] = [];

  mascotaForm: Mascota = {
    nombre: '', tamano: '', color: '', fechaPerdida: '', ubicacion: '', estado: 'PERDIDO'
  };

  constructor(private mascotaService: MascotaService,
              private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.mascotaService.obtenerMascotasPerdidas().subscribe({
      next: (data) => {
        this.listaMascotas = data;
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error("Error al cargar mascotas:", err);
      }
    });
  }

  cargarMascotas() {
    this.mascotaService.obtenerMascotasPerdidas().subscribe({
      next: (data) => {
        this.listaMascotas = data;
      },
      error: (err) => {
        console.error('Error al cargar mascotas', err);
      }
    });
  }
  abrirModalCrear() {
    this.modoEdicion = false;
    this.mascotaForm = { nombre: '', tamano: '', color: '', fechaPerdida: '', ubicacion: '', estado: 'PERDIDO' };
    this.mostrarModal = true;
  }
  abrirModalEditar(mascota: Mascota) {
    this.modoEdicion = true;
    this.mascotaForm = { ...mascota };
    this.mostrarModal = true;
  }
  cerrarModal() {
    this.mostrarModal = false;
  }
  guardarMascota() {
    if (this.modoEdicion) {
      // llama a editarMascota de servicio
    } else {
      // llama a crearMascota de servicio
    }
    this.cerrarModal();
  }
}
