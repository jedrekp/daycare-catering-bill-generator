import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DaycareGroup } from './daycare-group';
import { Child } from '../child/child';
import { API_URL } from '../const';

@Injectable({
  providedIn: 'root'
})
export class DaycareGroupDataService {

  constructor(
    private httpClient: HttpClient
  ) { }

  retrieveDaycareGroups() {
    return this.httpClient.get<DaycareGroup[]>(`${API_URL}/daycareGroups`)
  }

  retrieveSingleDaycareGroup(daycareGroupId: number) {
    return this.httpClient.get<DaycareGroup>(`${API_URL}/daycareGroups/${daycareGroupId}`)
  }

  createDaycareGroup(daycareGroup: DaycareGroup) {
    return this.httpClient.post<DaycareGroup>(`${API_URL}/daycareGroups`, daycareGroup)
  }

  editDaycareGroup(daycareGroupId: number, daycareGroup: DaycareGroup) {
    return this.httpClient.put<DaycareGroup>(`${API_URL}/daycareGroups/${daycareGroupId}`,
      daycareGroup)
  }

  deleteDaycareGroup(daycareGroupId: number) {
    return this.httpClient.delete(`${API_URL}/daycareGroups/${daycareGroupId}`)
  }

  getChildrenFromGroup(daycareGroupId: number) {
    return this.httpClient.get<Child[]>(`${API_URL}/daycareGroups/${daycareGroupId}/children`)
  }

  addChildToDaycareGroup(daycareGroupId: number, childId: number) {
    return this.httpClient.put<DaycareGroup>(
      `${API_URL}/daycareGroups/${daycareGroupId}/children/${childId}`, null)
  }

  removeChildFromDaycareGroup(daycareGroupId: number, childId: number) {
    return this.httpClient.delete<DaycareGroup>(`${API_URL}/daycareGroups/${daycareGroupId}/children/${childId}`)
  }

}
