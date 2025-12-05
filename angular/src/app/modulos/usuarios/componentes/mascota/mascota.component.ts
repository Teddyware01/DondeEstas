import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MascotaService } from '../../../../services/mascota.service';
import { Mascota } from '../../../../models/mascota.interface';

@Component({
  selector: 'app-mascota',
  standalone: false,
  templateUrl: './mascota.component.html'
})
export class MascotaComponent implements OnInit {

  mascota: Mascota | null = null;

  constructor(
    private route: ActivatedRoute,
    private mascotaService: MascotaService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) {
        this.mascotaService.obtenerPorId(id).subscribe(data => {
          this.mascota = data;
        });
      }
    });
  }

  eliminar(): void {
    if(confirm('¿Está seguro?')) {
      // pasar el boolean a false
    }
  }
}
