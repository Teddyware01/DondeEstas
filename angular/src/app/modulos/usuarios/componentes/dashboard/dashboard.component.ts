import {Component, Inject, OnInit} from '@angular/core';
import { DashboardService } from '../../../../services/dashboard.service';
import { DashboardStats } from '../../../../models/dashboard.interface';
import { ChangeDetectorRef, signal, PLATFORM_ID } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-home-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  stats: DashboardStats = {
    totalUsuarios: 0,
    busquedasActivas: 0,
    reencuentrosFelices: 0,
    barriosCubiertos: 0,
    provinciasCubiertas:0
  };
  public currentUser = signal<any>(null);
  loadingStats = signal(true);
  cargando: boolean = true;

  constructor(private dashboardService: DashboardService,
              private cd: ChangeDetectorRef,
              private usuarioService: UsuarioService,
              private router: Router,
              @Inject(PLATFORM_ID) private platformId: Object) {
      if (isPlatformBrowser(this.platformId)) {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
          this.currentUser.set(JSON.parse(savedUser));
        }
      }
    }

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    this.dashboardService.obtenerEstadisticas().subscribe({
      next: (data) => {
        this.stats = data;
        this.loadingStats.set(false);
        this.cargando = false;
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar estadísticas', err);
        this.cargando = false;
        this.cd.detectChanges();
      },
    });
  }
  onRegistrar() {
    this.usuarioService.abrirRegistro();
  }

  onPublicar() {
    this.router.navigate(['/mascotas']);
    this.usuarioService.abrirPublicar();
  }
}
