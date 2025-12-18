import { Component, OnInit, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { MascotaService } from '../../../../services/mascota.service';
import { Mascota } from '../../../../models/mascota.interface';
import * as L from 'leaflet';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../../services/usuario.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './todas-mascotas.component.html',
  styleUrls: ['./todas-mascotas.component.css']
})
export class TodasMascotasComponent implements OnInit {

  // --- Control de UI ---
  mostrarModal: boolean = false;
  mostrarModalLogin: boolean = false;
  modoEdicion: boolean = false;

  // --- Listas de Datos ---
  listaMascotasOriginal: Mascota[] = [];
  listaMascotasFiltrada: Mascota[] = [];

  // --- Filtros (Selección Múltiple) ---
  filtros: { texto: string, estados: string[], tamanos: string[], tipos: string[] } = {
    texto: '',
    estados: [],
    tamanos: [],
    tipos: []
  };

  estadosPosibles = [
    { clave: 'PERDIDO_PROPIO', label: 'Perdido por mí (Propio)' },
    { clave: 'PERDIDO_AJENO', label: 'Vi una mascota perdida (Ajeno)' },
    { clave: 'RECUPERADO', label: 'Ya fue recuperado' },
    { clave: 'ADOPTADO', label: 'Ya fue adoptado' }
  ];

  // --- Formulario ---
  mascotaForm: Mascota & { fotos?: File[] } = {
    nombre: '',
    tipoAnimal: 'PERRO',
    tamano: '',
    color: '',
    descripcionExtra: '',
    fecha: '',
    ubicacion: '',
    estado: 'PERDIDO_PROPIO',
    fotos: []
  };

  // --- Mapa (Leaflet) ---
  @ViewChild('map') mapElement!: ElementRef;
  private map!: L.Map;
  private marker!: L.Marker;

  constructor(
    private mascotaService: MascotaService,
    private usuarioService: UsuarioService,
    private cd: ChangeDetectorRef,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarMascotas();
  }

  // =============================================================
  // 1. LÓGICA DE CARGA Y FILTRADO
  // =============================================================

  cargarMascotas(): void {
    this.mascotaService.obtenerMascotasTodas().subscribe({
      next: (data) => {
        this.listaMascotasOriginal = data;
        this.aplicarFiltros();
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar mascotas', err)
    });
  }

  // Toggle para Estado
  toggleEstado(estadoClave: string): void {
    const index = this.filtros.estados.indexOf(estadoClave);
    if (index > -1) {
      this.filtros.estados.splice(index, 1);
    } else {
      this.filtros.estados.push(estadoClave);
    }
    this.aplicarFiltros();
  }

  // Toggle para Tamaño
  toggleTamano(tamanoValor: string): void {
    const index = this.filtros.tamanos.indexOf(tamanoValor);
    if (index > -1) {
      this.filtros.tamanos.splice(index, 1);
    } else {
      this.filtros.tamanos.push(tamanoValor);
    }
    this.aplicarFiltros();
  }

  // Toggle para Tipo de Animal
  toggleTipo(tipoValor: string): void {
    const index = this.filtros.tipos.indexOf(tipoValor);
    if (index > -1) {
      this.filtros.tipos.splice(index, 1);
    } else {
      this.filtros.tipos.push(tipoValor);
    }
    this.aplicarFiltros();
  }

  aplicarFiltros(): void {
    const textoBusqueda = this.filtros.texto.toLowerCase().trim();

    this.listaMascotasFiltrada = this.listaMascotasOriginal.filter(mascota => {

      // A. Filtro Texto (Nombre, Municipio, Provincia O Descripción Extra)
      const coincideTexto = !textoBusqueda ||
        mascota.nombre?.toLowerCase().includes(textoBusqueda) ||
        mascota.municipio?.toLowerCase().includes(textoBusqueda) ||
        mascota.provincia?.toLowerCase().includes(textoBusqueda) ||
        mascota.descripcionExtra?.toLowerCase().includes(textoBusqueda); // <--- AGREGADO AQUÍ

      // B. Filtro Estado
      const coincideEstado = this.filtros.estados.length === 0 ||
        this.filtros.estados.includes(mascota.estado);

      // C. Filtro Tamaño
      const coincideTamano = this.filtros.tamanos.length === 0 ||
        this.filtros.tamanos.includes(mascota.tamano);

      // D. Filtro Tipo Animal (Versión Robusta)
      const tipoBackend = mascota.tipoAnimal ? String(mascota.tipoAnimal).toUpperCase() : '';

      const coincideTipo = this.filtros.tipos.length === 0 ||
        this.filtros.tipos.includes(tipoBackend);

      return coincideTexto && coincideEstado && coincideTamano && coincideTipo;
    });
  }

  limpiarFiltros(): void {
    this.filtros = {
      texto: '',
      estados: [],
      tamanos: [],
      tipos: []
    };
    this.listaMascotasFiltrada = [...this.listaMascotasOriginal];
  }

  // =============================================================
  // 2. GESTIÓN DE MODALES
  // =============================================================

  abrirModalCrear(): void {
    if (!this.usuarioService.estaLogueado()) {
      this.mostrarModalLogin = true;
      return;
    }

    this.modoEdicion = false;
    this.reiniciarFormulario();
    this.mostrarModal = true;

    if (typeof window !== 'undefined') {
      setTimeout(() => this.initMap(), 200);
    }
  }

  cerrarModalLogin(): void {
    this.mostrarModalLogin = false;
  }

  irALogin(): void {
    this.cerrarModalLogin();
    this.router.navigate(['/login']);
  }

  abrirModalEditar(mascota: Mascota): void {
    this.modoEdicion = true;
    this.mascotaForm = { ...mascota, fotos: [] };
    this.mostrarModal = true;

    if (typeof window !== 'undefined') {
      setTimeout(() => this.initMap(), 200);
    }
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.reiniciarFormulario();
  }

  reiniciarFormulario(): void {
    this.mascotaForm = {
      nombre: '',
      tipoAnimal: 'PERRO',
      tamano: '',
      color: '',
      descripcionExtra: '',
      fecha: '',
      ubicacion: '',
      estado: 'PERDIDO_PROPIO',
      fotos: []
    };
  }

  // =============================================================
  // 3. LÓGICA DE GUARDADO (CRUD)
  // =============================================================

  guardarMascota(): void {
    if (typeof window === 'undefined') return;

    const usuarioId = localStorage.getItem('id');
    if (!usuarioId) {
      console.error('No hay usuario logueado');
      return;
    }

    if (this.modoEdicion) {
      console.log("Editando mascota (pendiente de implementar en backend)");
      this.cerrarModal();
      return;
    }

    const formData = new FormData();
    const payload = { ...this.mascotaForm, usuarioId: Number(usuarioId) };
    const { fotos, ...payloadSinFotos } = payload;

    formData.append('mascota', new Blob([JSON.stringify(payloadSinFotos)], { type: 'application/json' }));

    if (fotos && fotos.length > 0) {
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

  onFilesSelected(event: any): void {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;
    this.mascotaForm.fotos = Array.from(input.files);
  }

  // =============================================================
  // 4. MAPA (Leaflet)
  // =============================================================

  private async initMap(): Promise<void> {
    if (typeof window === 'undefined' || !this.mapElement) return;

    const L = await import('leaflet');

    const coords = this.mascotaForm.ubicacion
      ? this.mascotaForm.ubicacion.split(',').map(Number)
      : [-34.6037, -58.3816];

    if (this.map) {
      this.map.remove();
    }

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

    setTimeout(() => {
      this.map.invalidateSize();
    }, 100);
  }
}
