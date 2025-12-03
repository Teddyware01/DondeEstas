import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./modulos/usuarios/componentes/usuario.module')
      .then(m => m.UsuarioModule)
  }
];
