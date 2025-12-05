import { Component, OnInit } from '@angular/core';
import { MascotaService } from '../../../../services/mascota.service';
import { Mascota } from '../../../../models/mascota.interface';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  mostrarModal: boolean = false;
  modoEdicion: boolean = false;
  listaMascotas: Mascota[] = [];

  mascotaForm: Mascota = {
    nombre: '', tamano: '', color: '', fechaPerdida: '', ubicacion: '', estado: 'PERDIDO'
  };

  constructor(private mascotaService: MascotaService) { }

  ngOnInit(): void {
    this.cargarMascotas();
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
