import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../../../services/dashboard.service'; // Importar servicio
import { DashboardStats } from '../../../../models/dashboard.interface';       // Importar interfaz
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-home-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  // Inicializamos en 0 para que no falle la vista mientras carga
  stats: DashboardStats = {
    totalUsuarios: 0,
    busquedasActivas: 0,
    reencuentrosFelices: 0,
    barriosCubiertos: 0,
    provinciasCubiertas:0
  };

  // Variable opcional para mostrar un spinner de carga si quisieras
  cargando: boolean = true;

  constructor(private dashboardService: DashboardService,
              private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    this.dashboardService.obtenerEstadisticas().subscribe({
      next: (data) => {
        this.stats = data;
        this.cargando = false;
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error('Error al cargar estadísticas', err);
        this.cargando = false;
        this.cd.detectChanges();
      }
    });
  }

}
