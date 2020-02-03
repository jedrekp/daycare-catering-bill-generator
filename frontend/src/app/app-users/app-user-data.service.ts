import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AppUser } from './appUser';
import { API_URL } from '../const';

@Injectable({
  providedIn: 'root'
})
export class AppUserDataService {

  constructor(
    private httpClient: HttpClient
  ) { }

  createNewAppUserAccount(appUser: AppUser) {
    return this.httpClient.post<AppUser>(`${API_URL}/appUsers`, appUser)
  }

  getAppUserByUsername(username: string) {
    let params = new HttpParams()
      .set('username', username)
    return this.httpClient.get<AppUser>(`${API_URL}/appUsers`, { params: params })
  }

}
