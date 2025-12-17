import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RegistroComponent } from './registro/registro.component';
import { LoginComponent } from './login/login.component';
import { PerfilDetalleComponent } from './usuario/perfil-detalle/perfil-detalle.component';
import { MainLayoutComponent } from './main-layout/main-layout.component';
import { TodasMascotasComponent } from './todas-mascotas/todas-mascotas.component';
import { MascotaComponent } from './mascota/mascota.component';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'todas-mascotas',
        component: TodasMascotasComponent
      },
      {
        path: 'perfil/:id',
        component: PerfilDetalleComponent
      },
      { path: 'mascota/:id',
        component: MascotaComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuarioRoutingModule { }
