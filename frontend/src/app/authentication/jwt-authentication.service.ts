import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL, TOKEN } from '../const';
import { map } from 'rxjs/operators';
import * as jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class JwtAuthenticationService {

  constructor(
    private httpClient: HttpClient
  ) { }

  performJwtAuthentication(username: string, password: string) {

    return this.httpClient.post<any>(`${API_URL}/authenticate`,
      {
        username,
        password
      }).pipe(
        map(
          data => {
            sessionStorage.setItem(TOKEN, data.jwtToken)
            return data;
          }))
  }

  getToken() {
    return sessionStorage.getItem(TOKEN)
  }

  isUserLoggedIn() {
    if (!this.getToken()) {
      return false
    }
    let currentTimeInSeconds = Math.round(new Date().getTime() / 1000)
    return jwt_decode(this.getToken())['exp'] > currentTimeInSeconds
  }

  getUsername() {
    if (!this.getToken()) {
      return 'User not authenticated.'
    }
    return jwt_decode(this.getToken())['sub']
  }

  getUserRole(): string {
    if (!this.getToken()) {
      return 'User not authenticated.'
    }
    return jwt_decode(this.getToken())['userRole']
  }

  isUserAuthorized(authorizedUserRoles: string[]) {
    return authorizedUserRoles.indexOf(this.getUserRole()) > -1
  }


  logout() {
    sessionStorage.removeItem(TOKEN)
  }

}
