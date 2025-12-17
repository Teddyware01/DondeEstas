import { Component, OnInit, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { MascotaService } from '../../../../services/mascota.service';
import { Mascota } from '../../../../models/mascota.interface';
import * as L from 'leaflet';

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

  // Cambiamos foto a string (base64)
  mascotaForm: Mascota & { foto?: string | null } = {
    nombre: '',
    tamano: '',
    color: '',
    fechaPerdida: '',
    ubicacion: '',
    estado: 'PERDIDO',
    foto: null
  };

  @ViewChild('map') mapElement!: ElementRef;
  private map!: L.Map;
  private marker!: L.Marker;

  constructor(private mascotaService: MascotaService,
              private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.cargarMascotas();
  }

  cargarMascotas(): void {
    this.mascotaService.obtenerMascotasPerdidas().subscribe({
      next: (data) => {
        this.listaMascotas = data;
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar mascotas', err)
    });
  }

  abrirModalCrear(): void {
    this.modoEdicion = false;
    this.mascotaForm = { nombre: '', tamano: '', color: '', fechaPerdida: '', ubicacion: '', estado: 'PERDIDO', foto: null };
    this.mostrarModal = true;

    if (typeof window !== 'undefined') {
      setTimeout(() => this.initMap(), 200);
    }
  }

  abrirModalEditar(mascota: Mascota): void {
    this.modoEdicion = true;
    this.mascotaForm = { ...mascota, foto: null };
    this.mostrarModal = true;

    if (typeof window !== 'undefined') {
      setTimeout(() => this.initMap(), 200);
    }
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.mascotaForm = { nombre: '', tamano: '', color: '', fechaPerdida: '', ubicacion: '', estado: 'PERDIDO', foto: null };
  }

  guardarMascota(): void {

    const usuarioId = localStorage.getItem('id'); // o donde lo tengas
    if (!usuarioId) {
      console.error('No hay usuario logueado');
      return;
    }

    const payload = { ...this.mascotaForm, usuarioId: Number(usuarioId) };

    if (this.modoEdicion) {
      console.log("EDITANDO");
      /* algo asi...
      this.mascotaService.editarMascota(payload).subscribe({
        next: (res) => {
          console.log('Mascota editada:', res);
          this.cerrarModal();
          this.cargarMascotas();
        },
        error: (err) => console.error('Error editando mascota', err)
      });
       */
    } else {
      console.log("enviandoMASCOTA");
      this.mascotaService.crearMascota(payload).subscribe({
        next: (res) => {
          console.log('Mascota creada:', res);
          this.cerrarModal();
          this.cargarMascotas();
        },
        error: (err) => console.error('Error creando mascota', err)
      });
    }
    this.cerrarModal();
    this.cargarMascotas();
  }

  // Convertir archivo a base64
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      this.mascotaForm.foto = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  private async initMap(): Promise<void> {
    if (typeof window === 'undefined' || !this.mapElement) return;

    const L = await import('leaflet');

    const coords = this.mascotaForm.ubicacion
      ? this.mascotaForm.ubicacion.split(',').map(Number)
      : [-34.6037, -58.3816];

    this.map = L.map(this.mapElement.nativeElement).setView(coords as [number, number], 12);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.marker = L.marker(coords as [number, number], { draggable: true }).addTo(this.map);

    this.marker.on('dragend', () => {
      const pos = this.marker.getLatLng();
      this.mascotaForm.ubicacion = `${pos.lat},${pos.lng}`;
    });

    this.map.on('click', (e: any) => {
      this.marker.setLatLng(e.latlng);
      this.mascotaForm.ubicacion = `${e.latlng.lat},${e.latlng.lng}`;
    });
  }
}
