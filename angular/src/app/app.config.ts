import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
// 1. Agregamos HTTP_INTERCEPTORS y withInterceptorsFromDi a los imports
import { provideHttpClient, withFetch, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';

// 2. Importa tu interceptor (asegúrate que la ruta sea correcta)
import { AuthInterceptor } from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideClientHydration(withEventReplay()),

    // 3. Modificamos provideHttpClient
    provideHttpClient(
      withFetch(),
      withInterceptorsFromDi() // <--- ESTO habilita el soporte para interceptores de clase antigua
    ),

    // 4. Registramos tu interceptor
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
};
