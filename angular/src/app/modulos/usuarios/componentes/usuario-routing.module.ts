import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RegistroComponent } from './registro/registro.component';
import { LoginComponent } from './login/login.component';
import { PerfilEdicionComponent } from './perfil-edicion/perfil-edicion.component';
import { PerfilDetalleComponent } from './perfil-detalle/perfil-detalle.component';
import { MainLayoutComponent } from './main-layout/main-layout.component';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'registro',
    component: RegistroComponent
  },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'main',
        pathMatch: 'full'
      },

      {
        path: 'main',
        component: DashboardComponent
      },
      {
        path: 'perfil/editar',
        component: PerfilEdicionComponent
      },
      {
        path: 'login',
        component: PerfilEdicionComponent
      },
      {
        path: 'perfil/:id',
        component: PerfilDetalleComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuarioRoutingModule { }
