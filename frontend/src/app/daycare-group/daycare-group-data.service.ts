import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DaycareGroup } from './daycare-group';

@Injectable({
  providedIn: 'root'
})
export class DaycareGroupDataService {

  constructor(
    private httpClient: HttpClient
  ) { }

  retrieveGroups() {
    return this.httpClient.get<DaycareGroup[]>('http://localhost:8081/daycareGroups')
  }

  addSingleChildToDaycareGroup(daycareGroupId: number, childId: number) {
    return this.httpClient.put<DaycareGroup>(
      `http://localhost:8081/daycareGroups/${daycareGroupId}/children/${childId}`, null)
  }

}
