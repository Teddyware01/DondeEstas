import { Component, OnInit, ElementRef, ViewChild, ChangeDetectorRef, NgZone } from '@angular/core'; // <--- Agregar NgZone
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MascotaService } from '../../../../services/mascota.service';
import { UsuarioService } from '../../../../services/usuario.service';
import { Mascota } from '../../../../models/mascota.interface';

// Import dinámico de Leaflet se maneja en el método initMap,
// pero declaramos variables globales si usas TS estricto.
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

  // Carrusel
  indiceImagenActual = 0;
  imagenSeleccionada: string = '';
  imagenes: string[] = [];

  // Modales
  mostrarModalEditar = false;
  mostrarModalEliminar = false;
  mascotaForm: any = {};

  esDuenio = true;

  // Mapa
  @ViewChild('mapEdit') mapElement!: ElementRef;
  private map!: L.Map;
  private marker!: L.Marker;

  // Enums
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

  // Extraje esto a un método para poder re-usarlo si hiciera falta recargar todo
  cargarDatos(id: number) {
    this.mascotaService.obtenerPorId(id).subscribe({
      next: (data) => {
        if (!data) {
          this.errorCarga = true;
          this.cargando = false;
          return;
        }

        this.mascota = data;

        // Actualizar lista de imágenes
        this.imagenes = (data.imagenesBase64 && data.imagenesBase64.length > 0)
          ? data.imagenesBase64
          : ['/images/placeholder-pet.png'];

        this.actualizarImagenPrincipal();
        this.verificarDuenio();

        this.cargando = false;
        this.cd.detectChanges(); // <--- Asegura que la vista inicial pinte todo
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

  // --- Carrusel ---
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

  // --- Acciones ---
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

  // --- EDICIÓN (Aquí estaba el problema de flujo) ---

  abrirModalEditar(): void {
    // 1. Clonar objeto para no tocar la vista principal aún
    this.mascotaForm = { ...this.mascota };

    // 2. Formatear fecha para input type="date" (yyyy-MM-dd) si hace falta
    if (this.mascotaForm.fecha) {
      this.mascotaForm.fecha = new Date(this.mascotaForm.fecha).toISOString().split('T')[0];
    }

    this.mostrarModalEditar = true;

    // 3. Iniciar mapa con delay para asegurar que el DOM existe
    if (typeof window !== 'undefined') {
      setTimeout(() => {
        this.initMap();
      }, 100);
    }
  }

  cerrarModalEditar(): void {
    this.mostrarModalEditar = false;
    // Limpiamos mapa si existe para ahorrar memoria
    if (this.map) {
      this.map.remove();
      // @ts-ignore
      this.map = null;
    }
  }
  guardarEdicion(): void {
    const dto: any = { ...this.mascotaForm };

    // Mapear fecha al nombre que espera el backend
    if (dto.fecha) {
      dto.fechaPerdida = dto.fecha;
      delete dto.fecha;
    }

    this.mascotaService.editarMascota(this.mascota.id, dto).subscribe({
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


  // --- ELIMINAR ---
  abrirModalEliminar(): void { this.mostrarModalEliminar = true; }
  cerrarModalEliminar(): void { this.mostrarModalEliminar = false; }

  confirmarEliminacion(): void {
    this.mascotaService.desactivarMascota(this.mascota.id).subscribe({
      next: (mascotaActualizada) => {
        console.log('Mascota desactivada:', mascotaActualizada);
        // Actualizar la lista local si tienes
      },
      error: (err) => console.error('Error al desactivar mascota', err)
    });

    this.router.navigate(['/todas-mascotas']);
  }

  // --- MAPA LEAFLET (Corregido con NgZone) ---

  private async initMap(): Promise<void> {
    if (typeof window === 'undefined' || !this.mapElement) return;

    const L = await import('leaflet');

    // Parsear ubicación
    let coords: [number, number] = [-34.6037, -58.3816]; // Default BsAs
    if (this.mascotaForm.ubicacion && this.mascotaForm.ubicacion.includes(',')) {
      const split = this.mascotaForm.ubicacion.split(',');
      coords = [parseFloat(split[0]), parseFloat(split[1])];
    }

    // Reset mapa si ya existía
    if (this.map) {
      this.map.off();
      this.map.remove();
    }

    // Crear mapa
    this.map = L.map(this.mapElement.nativeElement).setView(coords, 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    this.marker = L.marker(coords, { draggable: true }).addTo(this.map);

    // --- AQUÍ ESTÁ LA MAGIA PARA EL FLUJO ---
    // Usamos ngZone.run() para volver a meter a Angular en la jugada
    // cuando ocurren eventos del mapa.

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
