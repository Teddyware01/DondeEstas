import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { UsuarioService } from '../../../../services/usuario.service';
import { RankingEntry } from '../../../../models/ranking.interface';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})

export class RankingComponent implements OnInit {
  rankingList: RankingEntry[] = [];
  isLoading: boolean = true;
  errorMessage: string | null = null;

  constructor(private usuarioService: UsuarioService,
              private cdRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.usuarioService.obtenerRanking().subscribe({
      next: (data) => {
        this.rankingList = data;
        this.isLoading = false;
        this.cdRef.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar el ranking de rescatistas.';
        this.isLoading = false;
        this.cdRef.detectChanges();
        console.error(err);
      }
    });
  }
}
