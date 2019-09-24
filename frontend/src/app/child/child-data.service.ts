import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Child } from './child';


class AssignedOptionDTO {
  constructor(
    private effectiveDate: string,
    private cateringOptionId: number) { }
}

@Injectable({
  providedIn: 'root'
})

export class ChildDataService {

  constructor(private httpClient: HttpClient) { }

  retrieveChild(childId: number) {
    return this.httpClient.get<Child>(`http://localhost:8081/children/${childId}`)
  }

  retrieveChildrenByGroupID(daycareGroupId: number) {
    let params = new HttpParams().set('daycareGroupId', daycareGroupId.toString())
    return this.httpClient.get<Child[]>('http://localhost:8081/children', { params: params })
  }

  findChildrenBySearchPhrase(searchPhrase: string) {
    let params = new HttpParams().set('searchPhrase', searchPhrase)
    return this.httpClient.get<Child[]>('http://localhost:8081/children', { params: params })
  }

  createChild(child: Child) {
    return this.httpClient.post<Child>('http://localhost:8081/children/', child)
  }

  editChild(childId: number, child: Child) {
    return this.httpClient.put<Child>(`http://localhost:8081/children/${childId}`, child)
  }

  assignNewOptionToChild(childId: number, assignedOptionId: number, effectiveDate: string) {
    return this.httpClient.post<Child>(`http://localhost:8081/children/${childId}/assignedOptions`,
      new AssignedOptionDTO(effectiveDate, assignedOptionId))
  }

  removeAssignedOptionFromChild(childId: number, assignedOptionId: number) {
    return this.httpClient.delete(
      `http://localhost:8081/children/${childId}/assignedOptions/${assignedOptionId}`)
  }

}


