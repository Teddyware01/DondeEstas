import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RegistroComponent } from '../componentes/registro/registro.component';
import { LoginComponent } from '../componentes/login/login.component';
import { PerfilEdicionComponent } from '../componentes/perfil-edicion/perfil-edicion.component';
import { PerfilDetalleComponent } from '../componentes/perfil-detalle/perfil-detalle.component';

const routes: Routes = [

  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },

  {
    path: 'registro',
    component: RegistroComponent
  },

  {
    path: 'login',
    component: LoginComponent
  },

  {
    path: 'perfil/editar',
    component: PerfilEdicionComponent
  },

  {
    path: 'perfil/:id',
    component: PerfilDetalleComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuarioRoutingModule { }
