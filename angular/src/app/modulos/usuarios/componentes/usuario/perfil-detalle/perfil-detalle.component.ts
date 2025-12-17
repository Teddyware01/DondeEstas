import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../../../../services/usuario.service';
import { Usuario } from '../../../../../models/usuario.interface';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-perfil-detalle',
  standalone: false,
  styleUrls: ['./perfil-detalle.component.css'],
  templateUrl: './perfil-detalle.component.html',
})
export class PerfilDetalleComponent implements OnInit {

  usuario: Usuario | null = null;
  loading = true;
  error: string | null = null;

  esPerfilPropio = false;

  /* ===== MODAL / FORM ===== */
  mostrarModalEditar = false;
  perfilForm!: FormGroup;
  errorEdicion: string | null = null;
  edicionExitosa = false;
  guardando = false;

  /* ===== MODAL ELIMINAR ===== */
  mostrarModalEliminar = false;
  eliminando = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private usuarioService: UsuarioService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const userIdParam = params.get('id');

      if (!userIdParam) {
        this.error = 'ID de usuario no proporcionado en la ruta.';
        this.loading = false;
        return;
      }

      const userId = Number(userIdParam);
      this.loading = true;
      this.error = null;

      this.usuarioService.obtenerPerfil(userId).subscribe({
        next: (data) => {
          this.usuario = data;

          const idUsuarioLogueado = this.usuarioService.obtenerUsuarioId();
          this.esPerfilPropio = data.id === idUsuarioLogueado;

          this.inicializarFormulario(data);

          this.loading = false;
          console.log('Perfil cargado:', data);
        },
        error: (err) => {
          this.error = 'No se pudo cargar el perfil del usuario. Es posible que el ID no exista.';
          this.loading = false;
          console.error('Error al cargar perfil:', err);
        }
      });
    });
  }

  /* ================= FORM ================= */

  inicializarFormulario(usuario: Usuario): void {
    this.perfilForm = this.fb.group({
      nombre: [usuario.nombre, Validators.required],
      apellido: [usuario.apellido, Validators.required],
      telefono: [usuario.telefono],
      ciudad: [usuario.ciudad, Validators.required],
      barrio: [usuario.barrio]
    });
  }

  abrirModalEditar(): void {
    this.errorEdicion = null;
    this.edicionExitosa = false;
    this.mostrarModalEditar = true;
  }

  cerrarModalEditar(): void {
    if (this.usuario) {
      this.perfilForm.reset({
        nombre: this.usuario.nombre,
        apellido: this.usuario.apellido,
        telefono: this.usuario.telefono,
        ciudad: this.usuario.ciudad,
        barrio: this.usuario.barrio
      });
    }

    this.errorEdicion = null;
    this.mostrarModalEditar = false;
  }

  guardarCambios(): void {
    if (this.perfilForm.invalid || !this.usuario || this.guardando) {
      return;
    }

    this.guardando = true;
    this.errorEdicion = null;

    const datosActualizados: Usuario = {
      ...this.usuario,
      ...this.perfilForm.value
    };

    this.usuarioService.editarPerfil(<number>this.usuarioService.obtenerUsuarioId(), datosActualizados).subscribe({
      next: (usuarioActualizado) => {
        this.usuario = usuarioActualizado;
        this.guardando = false;
        this.edicionExitosa = true;

        setTimeout(() => {
          this.edicionExitosa = false;
          this.cerrarModalEditar();
        }, 1200);
      },
      error: (err) => {
        this.guardando = false;
        this.errorEdicion = 'No se pudieron guardar los cambios.';
        console.error('Error al actualizar perfil:', err);
      }
    });
  }

  /* ================= OTROS ================= */

  getPosicionRanking(): number | string {
    if (this.usuario && this.usuario.puntajes) {
      return 'Nivel ' + Math.floor(this.usuario.puntajes / 100);
    }
    return 'Sin ranking';
  }

  /* ================= ELIMINAR CUENTA ================= */

  eliminarCuenta(): void {
    this.mostrarModalEliminar = true;
  }

  cerrarModalEliminar(): void {
    this.mostrarModalEliminar = false;
  }

  confirmarEliminarCuenta(): void {
    if (!this.usuario || !this.usuario.id) return;

    this.eliminando = true;

    // Se asume que el método desactivar(id) existe en UsuarioService
    // y hace la llamada PUT /desactivar/{id}
    this.usuarioService.desactivar(this.usuario.id).subscribe({
      next: () => {
        // Limpiar sesión
        localStorage.removeItem('token');
        localStorage.removeItem('usuario');

        this.eliminando = false;
        this.mostrarModalEliminar = false;

        // Redirigir al login o home
        this.router.navigate(['/login']);
        alert('Tu cuenta ha sido desactivada correctamente.');
      },
      error: (err) => {
        console.error('Error al desactivar cuenta', err);
        this.eliminando = false;
        alert('Ocurrió un error al intentar eliminar la cuenta. Intente nuevamente.');
      }
    });
  }

  verMascota(id: number): void {
    // Lógica para ver mascota
  }
}
