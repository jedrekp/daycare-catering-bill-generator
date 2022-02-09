import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { JwtAuthService } from './jwt-auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(
    private authService: JwtAuthService,
    private router: Router
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const authorizedUserRoles = route.data.authorizedUserRoles

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } })
      return false
    }

    else if (!this.isUserAuthorized(authorizedUserRoles)) {
      this.router.navigate(['error'], { state: { message: 'You are not authorized to access this page.' } })
      return false
    }
    
    return true
  }

  private isUserAuthorized(roles: string[]) {
    return !(roles && roles.indexOf(this.authService.getUserRole()) == -1)
  }

}
