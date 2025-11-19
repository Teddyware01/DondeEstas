import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsuarioService } from '../../../../services/usuario.service';
import { Usuario } from '../../../../models/usuario.interface';
import {DatePipe, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-perfil-detalle',
  templateUrl: './perfil-detalle.component.html',
  imports: [
    DatePipe,
    NgIf,
    NgForOf
  ],
  styleUrls: ['./perfil-detalle.component.css']
})
export class PerfilDetalleComponent implements OnInit {

  usuario: Usuario | null = null;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private usuarioService: UsuarioService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const userIdParam = params.get('id');

      if (userIdParam) {
        const userId = Number(userIdParam);
        this.loading = true;
        this.error = null;

        this.usuarioService.obtenerPerfil(userId).subscribe({
          next: (data) => {
            this.usuario = data;
            this.loading = false;
            console.log('Perfil cargado:', data);
          },
          error: (err) => {
            this.error = 'No se pudo cargar el perfil del usuario. Es posible que el ID no exista.';
            this.loading = false;
            console.error('Error al cargar perfil:', err);
          }
        });
      } else {
        this.error = 'ID de usuario no proporcionado en la ruta.';
        this.loading = false;
      }
    });
  }
}
