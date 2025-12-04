import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { UsuarioRoutingModule } from './usuario-routing.module';

import { MainLayoutComponent } from './main-layout/main-layout.component';
import { RegistroComponent } from '../componentes/registro/registro.component';
import { LoginComponent } from '../componentes/login/login.component';
import { PerfilEdicionComponent } from '../componentes/perfil-edicion/perfil-edicion.component';
import { PerfilDetalleComponent } from '../componentes/perfil-detalle/perfil-detalle.component';


@NgModule({
  declarations: [
    RegistroComponent,
    LoginComponent,
    PerfilEdicionComponent,
    PerfilDetalleComponent,
    MainLayoutComponent
  ],

  imports: [
    CommonModule,
    UsuarioRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class UsuarioModule { }
