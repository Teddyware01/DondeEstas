import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { UsuarioRoutingModule } from './usuario-routing.module';

import { RegistroComponent } from '../componentes/registro/registro.component';
import { LoginComponent } from '../componentes/login/login.component';
import { PerfilEdicionComponent } from '../componentes/perfil-edicion/perfil-edicion.component';
import { PerfilDetalleComponent } from '../componentes/perfil-detalle/perfil-detalle.component';


@NgModule({
imports: [
  CommonModule,
  UsuarioRoutingModule,
  FormsModule,
  ReactiveFormsModule,
  RegistroComponent,
  LoginComponent,
  PerfilEdicionComponent,
  PerfilDetalleComponent
]

})
export class UsuarioModule { }
