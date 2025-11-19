import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { Usuario } from '../../../../models/usuario.interface';
import { Router } from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-perfil-edicion',
  templateUrl: './perfil-edicion.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./perfil-edicion.component.css']
})
export class PerfilEdicionComponent implements OnInit {

  usuario: Usuario = {
    id: 0,
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    barrio: '',
    ciudad: '',
    isAdmin: false
  };

  edicionExitosa = false;
  errorEdicion: string | null = null;

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  ngOnInit(): void {
    const userId = 1; // por ej

    this.usuarioService.obtenerPerfil(userId).subscribe({
      next: (data) => {
        this.usuario = data;
      },
      error: (err) => {
        console.error('Error al cargar el perfil:', err);
        this.errorEdicion = 'No se pudo cargar la información del perfil.';
      }
    });
  }

  onGuardarCambios(): void {
    this.edicionExitosa = false;
    this.errorEdicion = null;

    if (this.usuario.id) {
      this.usuarioService.editarPerfil(this.usuario.id, this.usuario).subscribe({
        next: (response) => {
          this.edicionExitosa = true;
          this.errorEdicion = null;
          console.log('Perfil actualizado con éxito.', response);
        },
        error: (err) => {
          this.errorEdicion = 'Ocurrió un error al intentar guardar los cambios.';
          console.error('Error durante la edición:', err);
        }
      });
    }
  }

  onSolicitarBaja(): void {
    if (confirm('¿Está seguro que desea solicitar la baja del sistema? Esta acción es irreversible.')) {
      this.usuarioService.borrarUsuario(this.usuario.id!).subscribe({
        next: () => {
          alert('Su cuenta ha sido eliminada.');
          this.router.navigate(['/']);
        },
        error: (err) => {
          alert('Error al procesar la baja. Intente nuevamente.');
        }
      });
    }
  }
}
