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

  estadosPosibles = [
    { clave: 'PERDIDO_PROPIO', label: 'Perdido por mí (Propio)' },
    { clave: 'PERDIDO_AJENO', label: 'Vi una mascota perdida (Ajeno)' },
    { clave: 'RECUPERADO', label: 'Ya fue recuperado' },
    { clave: 'ADOPTADO', label: 'Ya fue adoptado' }
  ];

  // Mascota form con array de archivos
  mascotaForm: Mascota & { fotos?: File[] } = {
    nombre: '',
    tamano: '',
    color: '',
    fecha: '',
    ubicacion: '',
    estado: 'PERDIDO_PROPIO',
    fotos: []
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
    this.mascotaForm = {
      nombre: '',
      tamano: '',
      color: '',
      fecha: '',
      ubicacion: '',
      estado: 'PERDIDO_PROPIO',
      fotos: []
    };
    this.mostrarModal = true;
    if (typeof window !== 'undefined') setTimeout(() => this.initMap(), 200);
  }

  abrirModalEditar(mascota: Mascota): void {
    this.modoEdicion = true;
    this.mascotaForm = { ...mascota, fotos: [] };
    this.mostrarModal = true;
    if (typeof window !== 'undefined') setTimeout(() => this.initMap(), 200);
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.mascotaForm = {
      nombre: '',
      tamano: '',
      color: '',
      fecha: '',
      ubicacion: '',
      estado: 'PERDIDO_PROPIO',
      fotos: []
    };
  }

  guardarMascota(): void {
    if (typeof window === 'undefined') return;

    const usuarioId = localStorage.getItem('id');
    if (!usuarioId) {
      console.error('No hay usuario logueado');
      return;
    }

    if (this.modoEdicion) {
      console.log("Editando mascota (pendiente de implementar)");
      this.cerrarModal();
      return;
    }

    // Crear FormData compatible con @RequestPart("mascota") y @RequestPart("imagenes")
    const formData = new FormData();
    const payload = { ...this.mascotaForm, usuarioId: Number(usuarioId) };

    // Extraemos fotos para no incluirlas en el JSON
    const { fotos, ...payloadSinFotos } = payload;

    // JSON de la mascota
    formData.append('mascota', new Blob([JSON.stringify(payloadSinFotos)], { type: 'application/json' }));

    // Archivos
    if (fotos) {
      fotos.forEach(file => formData.append('imagenes', file));
    }

    this.mascotaService.crearMascota(formData).subscribe({
      next: (res) => {
        console.log('Mascota creada con éxito:', res);
        this.cerrarModal();
        this.cargarMascotas();
      },
      error: (err) => {
        console.error('Error creando mascota', err);
        alert('Error al crear la mascota. Verifica los datos o el tamaño de las imágenes.');
      }
    });
  }

  // Manejar selección de múltiples archivos
  onFilesSelected(event: any): void {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;
    this.mascotaForm.fotos = Array.from(input.files);
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
