import { Injectable, Inject, PLATFORM_ID } from '@angular/core'; // <--- Importa Inject y PLATFORM_ID
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common'; // <--- Importa esta función

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  // Inyectamos el ID de la plataforma para saber dónde estamos corriendo
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    // Verificamos si estamos en el navegador antes de tocar localStorage
    if (isPlatformBrowser(this.platformId)) {

      const token = localStorage.getItem('token');

      if (token) {
        const authReq = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
        return next.handle(authReq);
      }
    }

    // Si estamos en el servidor o no hay token, dejamos pasar la petición tal cual
    return next.handle(request);
  }
}
