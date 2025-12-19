import { Component, OnInit, ElementRef, ViewChild, ChangeDetectorRef, NgZone } from '@angular/core'; // <--- Agregar NgZone
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MascotaService } from '../../../../services/mascota.service';
import { UsuarioService } from '../../../../services/usuario.service';
import { Mascota } from '../../../../models/mascota.interface';

import * as L from 'leaflet';

@Component({
  selector: 'app-detalle-mascota',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './mascota.component.html',
  styleUrls: ['./mascota.component.css']
})
export class MascotaComponent implements OnInit {

  mascota!: Mascota;
  cargando = true;
  errorCarga = false;

  indiceImagenActual = 0;
  imagenSeleccionada: string = '';
  imagenes: string[] = [];

  mostrarModalEditar = false;
  mostrarModalEliminar = false;
  mascotaForm: any = {};

  esDuenio = true;

  @ViewChild('mapEdit') mapElement!: ElementRef;
  private map!: L.Map;
  private marker!: L.Marker;

  estadosPosibles = [
    { clave: 'PERDIDO_PROPIO', label: 'Perdido por mí (Propio)' },
    { clave: 'PERDIDO_AJENO', label: 'Vi una mascota perdida (Ajeno)' },
    { clave: 'RECUPERADO', label: 'Ya fue recuperado' },
    { clave: 'ADOPTADO', label: 'Ya fue adoptado' }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private mascotaService: MascotaService,
    private usuarioService: UsuarioService,
    private cd: ChangeDetectorRef, // Para forzar actualizaciones manuales
    private ngZone: NgZone // <--- VITAL: Para meter eventos de Leaflet en Angular
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarDatos(id);
  }

  cargarDatos(id: number) {
    this.mascotaService.obtenerPorId(id).subscribe({
      next: (data) => {
        if (!data) {
          this.errorCarga = true;
          this.cargando = false;
          return;
        }

        this.mascota = data;

        this.imagenes = (data.imagenesBase64 && data.imagenesBase64.length > 0)
          ? data.imagenesBase64
          : ['/images/placeholder-pet.png'];

        this.actualizarImagenPrincipal();
        this.verificarDuenio();

        this.cargando = false;
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error('Error', err);
        this.errorCarga = true;
        this.cargando = false;
      }
    });
  }

  verificarDuenio(): void {
    const usuarioLogueadoId = this.usuarioService.obtenerUsuarioId();
    if (usuarioLogueadoId && this.mascota?.usuarioId) {
      this.esDuenio = (Number(usuarioLogueadoId) === Number(this.mascota.usuarioId));
    }
  }

  actualizarImagenPrincipal(): void {
    if (this.imagenes.length > 0) {
      this.imagenSeleccionada = this.imagenes[this.indiceImagenActual];
    }
  }

  anteriorImagen(): void {
    this.indiceImagenActual = (this.indiceImagenActual - 1 + this.imagenes.length) % this.imagenes.length;
    this.actualizarImagenPrincipal();
  }

  siguienteImagen(): void {
    this.indiceImagenActual = (this.indiceImagenActual + 1) % this.imagenes.length;
    this.actualizarImagenPrincipal();
  }

  volver(): void {
    this.router.navigate(['/todas-mascotas']);
  }

  contactar(): void {
    if (this.mascota.telefono) {
      window.open(`https://wa.me/${this.mascota.telefono}`, '_blank');
    } else {
      alert('El usuario no agregó un medio de contacto.');
    }
  }

  abrirModalEditar(): void {
    this.mascotaForm = { ...this.mascota };

    if (this.mascotaForm.fechaPerdida) {
      this.mascotaForm.fechaPerdida = new Date(this.mascotaForm.fechaPerdida).toISOString().split('T')[0];
    }

    this.mostrarModalEditar = true;

    if (typeof window !== 'undefined') {
      setTimeout(() => {
        this.initMap();
      }, 100);
    }
  }

  cerrarModalEditar(): void {
    this.mostrarModalEditar = false;
    if (this.map) {
      this.map.remove();
      // @ts-ignore
      this.map = null;
    }
  }

  guardarEdicion(): void {
    if (!this.mascotaForm.nombre) {
      alert("El nombre es obligatorio");
      return;
    }

    this.mascotaService.editarMascota(this.mascota.id, this.mascotaForm).subscribe({
      next: (updated) => {
        this.mascota = updated;

        if (updated.imagenesBase64 && updated.imagenesBase64.length > 0) {
          this.imagenes = updated.imagenesBase64;
          this.indiceImagenActual = 0;
          this.actualizarImagenPrincipal();
        }

        this.cd.detectChanges();

        this.cerrarModalEditar();
      },
      error: (err) => {
        console.error('Error al editar', err);
        alert('Error al guardar cambios.');
      }
    });
  }

  abrirModalEliminar(): void { this.mostrarModalEliminar = true; }
  cerrarModalEliminar(): void { this.mostrarModalEliminar = false; }

  confirmarEliminacion(): void {
    this.mascotaService.desactivarMascota(this.mascota.id).subscribe({
      next: (mascotaActualizada) => {
        console.log('Mascota desactivada:', mascotaActualizada);
        this.router.navigate(['/todas-mascotas']);
      },
      error: (err) => console.error('Error al desactivar mascota', err)
    });

  }


  private async initMap(): Promise<void> {
    if (typeof window === 'undefined' || !this.mapElement) return;

    const L = await import('leaflet');

    let coords: [number, number] = [-34.6037, -58.3816]; // Default BsAs
    if (this.mascotaForm.ubicacion && this.mascotaForm.ubicacion.includes(',')) {
      const split = this.mascotaForm.ubicacion.split(',');
      coords = [parseFloat(split[0]), parseFloat(split[1])];
    }

    if (this.map) {
      this.map.off();
      this.map.remove();
    }

    this.map = L.map(this.mapElement.nativeElement).setView(coords, 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    this.marker = L.marker(coords, { draggable: true }).addTo(this.map);


    this.marker.on('dragend', () => {
      this.ngZone.run(() => {
        const pos = this.marker.getLatLng();
        this.mascotaForm.ubicacion = `${pos.lat},${pos.lng}`;
        console.log("Nueva ubicación (drag):", this.mascotaForm.ubicacion);
      });
    });

    this.map.on('click', (e: any) => {
      this.ngZone.run(() => {
        this.marker.setLatLng(e.latlng);
        this.mascotaForm.ubicacion = `${e.latlng.lat},${e.latlng.lng}`;
        console.log("Nueva ubicación (click):", this.mascotaForm.ubicacion);
      });
    });

    setTimeout(() => { this.map.invalidateSize(); }, 100);
  }
}
