import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { User } from 'src/app/user/user';
import { API_URL } from 'src/const';
import { CrudDataService } from './crud-data.service';

export interface NewPasswordRequest {
  currentPassword: string,
  newPassword: string
}

@Injectable({
  providedIn: 'root'
})
export class UserDataService extends CrudDataService {

  constructor(http: HttpClient) {
    super(`${API_URL}/appUsers`, http)
  }

  getSingleByUsername(username: string) {
    const params = new HttpParams()
      .set('username', username)
    return this.http.get<User>(`${this.resourceUrl}`, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

  getAllByDaycareRole(daycareRole: string) {
    const params = new HttpParams()
      .set('daycareRole', daycareRole)
    return this.http.get<User[]>(`${this.resourceUrl}`, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

  assignDaycareGroupToSupervisor(userId: number, daycareGroupId: number) {
    return this.http.put<User>(`${this.resourceUrl}/${userId}/daycareGroups/${daycareGroupId}`, null)
      .pipe(
        catchError(this.handleError)
      )
  }

  removeDaycareGroupFromSupervisor(userId: number, daycareGroupId: number) {
    return this.http.delete(`${this.resourceUrl}/${userId}/daycareGroups/${daycareGroupId}`)
      .pipe(
        catchError(this.handleError)
      )
  }

  deleteGroupSupervisorAccount(userId: number) {
    return this.http.delete(`${this.resourceUrl}/${userId}`)
      .pipe(
        catchError(this.handleError)
      )
  }

  changeUserPassword(newPasswordRequest: NewPasswordRequest, username: string) {
    const params = new HttpParams()
      .set('username', username)
    return this.http.put(`${this.resourceUrl}`, newPasswordRequest, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

}
