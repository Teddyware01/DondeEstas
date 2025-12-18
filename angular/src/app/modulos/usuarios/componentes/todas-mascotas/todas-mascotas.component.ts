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
  mostrarModal: boolean = false;       // Modal de formulario (Crear/Editar)
  mostrarModalLogin: boolean = false;  // Modal de aviso "Necesitas login"
  modoEdicion: boolean = false;

  // --- Listas de Datos ---
  // listaMascotasOriginal: Mantiene la "fuente de verdad" traída del backend.
  listaMascotasOriginal: Mascota[] = [];
  // listaMascotasFiltrada: Es la que se itera en el HTML.
  listaMascotasFiltrada: Mascota[] = [];

  // --- Filtros (Selección Múltiple) ---
  filtros: { texto: string, estados: string[], tamanos: string[] } = {
    texto: '',
    estados: [], // Array vacío = "Todos"
    tamanos: []  // Array vacío = "Todos"
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
    tamano: '',
    color: '',
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
    this.mascotaService.obtenerMascotasPerdidas().subscribe({
      next: (data) => {
        this.listaMascotasOriginal = data;
        // Al cargar, aplicamos los filtros actuales para refrescar la vista
        this.aplicarFiltros();
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al cargar mascotas', err)
    });
  }

  // Toggle para Estado (Selección Múltiple)
  toggleEstado(estadoClave: string): void {
    const index = this.filtros.estados.indexOf(estadoClave);
    if (index > -1) {
      this.filtros.estados.splice(index, 1); // Quitar si existe
    } else {
      this.filtros.estados.push(estadoClave); // Agregar si no existe
    }
    this.aplicarFiltros();
  }

  // Toggle para Tamaño (Selección Múltiple)
  toggleTamano(tamanoValor: string): void {
    const index = this.filtros.tamanos.indexOf(tamanoValor);
    if (index > -1) {
      this.filtros.tamanos.splice(index, 1);
    } else {
      this.filtros.tamanos.push(tamanoValor);
    }
    this.aplicarFiltros();
  }

  aplicarFiltros(): void {
    const textoBusqueda = this.filtros.texto.toLowerCase().trim();

    this.listaMascotasFiltrada = this.listaMascotasOriginal.filter(mascota => {

      // A. Filtro Texto (Nombre, Municipio o Provincia)
      const coincideTexto = !textoBusqueda ||
        mascota.nombre?.toLowerCase().includes(textoBusqueda) ||
        mascota.municipio?.toLowerCase().includes(textoBusqueda) ||
        mascota.provincia?.toLowerCase().includes(textoBusqueda);

      // B. Filtro Estado (Si el array está vacío, pasan todos. Si no, debe incluirse)
      const coincideEstado = this.filtros.estados.length === 0 ||
        this.filtros.estados.includes(mascota.estado);

      // C. Filtro Tamaño
      const coincideTamano = this.filtros.tamanos.length === 0 ||
        this.filtros.tamanos.includes(mascota.tamano);

      return coincideTexto && coincideEstado && coincideTamano;
    });
  }

  limpiarFiltros(): void {
    this.filtros = {
      texto: '',
      estados: [],
      tamanos: []
    };
    this.listaMascotasFiltrada = [...this.listaMascotasOriginal];
  }

  // =============================================================
  // 2. GESTIÓN DE MODALES
  // =============================================================

  abrirModalCrear(): void {
    // Verificamos si está logueado
    if (!this.usuarioService.estaLogueado()) {
      // En lugar de alert, mostramos el modal de login
      this.mostrarModalLogin = true;
      return;
    }

    // Si está logueado, procedemos normal
    this.modoEdicion = false;
    this.reiniciarFormulario();
    this.mostrarModal = true;

    if (typeof window !== 'undefined') {
      setTimeout(() => this.initMap(), 200);
    }
  }

  // Métodos para el Modal de Aviso de Login
  cerrarModalLogin(): void {
    this.mostrarModalLogin = false;
  }

  irALogin(): void {
    this.cerrarModalLogin();
    this.router.navigate(['/login']);
  }

  // Métodos para el Modal de Formulario Mascota
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
      tamano: '',
      color: '',
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
