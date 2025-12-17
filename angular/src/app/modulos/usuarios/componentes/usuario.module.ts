import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { UsuarioRoutingModule } from './usuario-routing.module';

import { MainLayoutComponent } from './main-layout/main-layout.component';
import { RegistroComponent } from '../componentes/registro/registro.component';
import { LoginComponent } from '../componentes/login/login.component';
import { PerfilDetalleComponent } from './usuario/perfil-detalle/perfil-detalle.component';
import { TodasMascotasComponent } from './todas-mascotas/todas-mascotas.component';
import { RankingComponent } from './ranking/ranking.component';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../../../interceptors/auth.interceptor';
import {DashboardComponent} from './dashboard/dashboard.component';

@NgModule({
  declarations: [
    RegistroComponent,
    LoginComponent,
    PerfilDetalleComponent,
    MainLayoutComponent,
    TodasMascotasComponent,
    RankingComponent,
    DashboardComponent
  ],

  imports: [
    CommonModule,
    UsuarioRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
providers: [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }
]
})
export class UsuarioModule { }
