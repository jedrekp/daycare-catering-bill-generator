import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL, TOKEN } from 'src/const';
import { catchError, map } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { throwError } from 'rxjs';
import { UnauthorizedError } from '../../common/error/unauthorized-error';
import { AppError } from '../../common/error/app-error';

@Injectable({
  providedIn: 'root'
})
export class JwtAuthService {

  constructor(
    private http: HttpClient,
    private jwtHelper: JwtHelperService
  ) { }

  authenticate(credentials: { username: string, password: string }) {
    return this.http.post<any>(`${API_URL}/authenticate`, credentials).pipe(
      catchError(this.handleError),
      map(
        response => {
          localStorage.setItem(TOKEN, response.jwtToken)
          return true
        }))
  }

  getToken() {
    return localStorage.getItem(TOKEN)
  }

  isLoggedIn() {
    const token = this.getToken()
    if (!token) return false
    return !this.jwtHelper.isTokenExpired(token)
  }

  getUsername() {
    const token = this.getToken()
    if (!token) {
      return 'User not authenticated.'
    }
    return this.jwtHelper.decodeToken(token).sub
  }

  getFullName() {
    const token = this.getToken()
    if (!token) {
      return 'User not authenticated.'
    }
    return this.jwtHelper.decodeToken(token).fullName
  }

  getUserRole(): string {
    const token = this.getToken()
    if (!token) {
      return 'User not authenticated.'
    }
    return this.jwtHelper.decodeToken(token).userRole
  }

  isAuthorized(authorizedUserRoles: string[]) {
    return authorizedUserRoles.indexOf(this.getUserRole()) > -1
  }

  logout() {
    localStorage.removeItem(TOKEN)
  }

  private handleError(error: Response) {
    console.log(error.status)
    if (error.status === 401)
      return throwError(new UnauthorizedError(error))

    return throwError(new AppError(error))
  }

}
