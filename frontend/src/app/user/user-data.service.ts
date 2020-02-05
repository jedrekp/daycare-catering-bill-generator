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

  getUsersByDaycareRole(daycareRole: string) {
    let params = new HttpParams()
      .set('daycareRole', daycareRole)
    return this.httpClient.get<User[]>(`${API_URL}/appUsers`, { params: params })
  }

  deleteUserAccount(userId: number) {
    return this.httpClient.delete<any>(`${API_URL}/appUsers/${userId}`)
  }

  assignDaycareGroupToUser(userId: number, daycareGroupId: number) {
    return this.httpClient.put<User>(`${API_URL}/appUsers/${userId}/daycareGroups/${daycareGroupId}`, null)
  }

  removeDaycareGroupFromUser(userId: number, daycareGroupId: number) {
    return this.httpClient.delete<any>(`${API_URL}/appUsers/${userId}/daycareGroups/${daycareGroupId}`)
  }

  changeUserPassword(username: string, currentPassword: string, newPassword: string) {
    let params = new HttpParams()
      .set('username', username)
    let changeUserPasswordRequest = new ChangeUserPasswordRequest(currentPassword, newPassword)
    return this.httpClient.put<any>(`${API_URL}/appUsers`, changeUserPasswordRequest, { params: params })
  }

}

class ChangeUserPasswordRequest {
  constructor(
    private currentPassword: string,
    private newPassword: string
  ) { }
}
