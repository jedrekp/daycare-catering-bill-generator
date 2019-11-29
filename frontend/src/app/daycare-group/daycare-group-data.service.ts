import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DaycareGroup } from './daycare-group';
import { Child } from '../child/child';

@Injectable({
  providedIn: 'root'
})
export class DaycareGroupDataService {

  constructor(
    private httpClient: HttpClient
  ) { }

  retrieveDaycareGroups() {
    return this.httpClient.get<DaycareGroup[]>('http://localhost:8081/daycareGroups')
  }

  retrieveSingleDaycareGroup(daycareGroupId: number) {
    return this.httpClient.get<DaycareGroup>(`http://localhost:8081/daycareGroups/${daycareGroupId}`)
  }

  createDaycareGroup(daycareGroup: DaycareGroup) {
    return this.httpClient.post<DaycareGroup>('http://localhost:8081/daycareGroups', daycareGroup)
  }

  editDaycareGroup(daycareGroupId: number, daycareGroup: DaycareGroup) {
    return this.httpClient.put<DaycareGroup>(`http://localhost:8081/daycareGroups/${daycareGroupId}`,
      daycareGroup)
  }

  deleteDaycareGroup(daycareGroupId: number) {
    return this.httpClient.delete(`http://localhost:8081/daycareGroups/${daycareGroupId}`)
  }

  getChildrenFromGroup(daycareGroupId: number) {
    return this.httpClient.get<Child[]>(`http://localhost:8081/daycareGroups/${daycareGroupId}/children`)
  }

  addChildToDaycareGroup(daycareGroupId: number, childId: number) {
    return this.httpClient.put<DaycareGroup>(
      `http://localhost:8081/daycareGroups/${daycareGroupId}/children/${childId}`, null)
  }

  removeChildFromDaycareGroup(daycareGroupId: number, childId: number){
    return this.httpClient.delete<DaycareGroup>(`http://localhost:8081/daycareGroups/${daycareGroupId}/children/${childId}`)
  }

}
