import { Injectable } from '@angular/core';
import { JwtAuthenticationService } from './jwt-authentication.service';
import { Router, ActivatedRouteSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService {

  constructor(
    private authenticationService: JwtAuthenticationService,
    private router: Router
  ) { }

  canActivate(route: ActivatedRouteSnapshot) {
    if (!this.authenticationService.isUserLoggedIn()) {
      this.router.navigate(['login'])
      return false;
    }
    if (route.data.authorizedUserRoles && route.data.authorizedUserRoles.indexOf(this.authenticationService.getUserRole()) == -1) {
      this.router.navigate(['error'], { state: { message: 'You are not authorized to access this page.' } })
      return false;
    }
    return true;
  }

}