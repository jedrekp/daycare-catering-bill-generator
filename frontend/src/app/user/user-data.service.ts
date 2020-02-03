import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { User } from './user';
import { API_URL } from '../const';

@Injectable({
  providedIn: 'root'
})
export class UserDataService {

  constructor(
    private httpClient: HttpClient
  ) { }

  createNewUserAccount(user: User) {
    return this.httpClient.post<User>(`${API_URL}/appUsers`, user)
  }

  getUserByUsername(username: string) {
    let params = new HttpParams()
      .set('username', username)
    return this.httpClient.get<User>(`${API_URL}/appUsers`, { params: params })
  }

}
