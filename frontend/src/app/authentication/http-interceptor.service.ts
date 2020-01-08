import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { JwtAuthenticationService } from './jwt-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {

  constructor(private authenticationService: JwtAuthenticationService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    let jwtToken = this.authenticationService.getToken();
    if (jwtToken) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${jwtToken}`
        }
      })
    }
    return next.handle(request);
  }

}