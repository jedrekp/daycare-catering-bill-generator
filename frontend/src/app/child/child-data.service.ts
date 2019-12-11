import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Child } from './child';
import { API_URL } from '../const';


class AssignOptionToChildDTO {
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
    return this.httpClient.get<Child>(`${API_URL}/children/${childId}`)
  }

  retrieveArchivedChildren() {
    let params = new HttpParams().set('archived', 'true')
    return this.httpClient.get<Child[]>(`${API_URL}/children`, { params: params })
  }

  findChildrenBySearchPhrase(searchPhrase: string) {
    let params = new HttpParams().set('searchPhrase', searchPhrase)
    return this.httpClient.get<Child[]>(`${API_URL}/children`, { params: params })
  }

  createChild(child: Child) {
    return this.httpClient.post<Child>(`${API_URL}/children`, child)
  }

  editChild(childId: number, child: Child) {
    return this.httpClient.put<Child>(`${API_URL}/children/${childId}`, child)
  }

  assignNewOptionToChild(childId: number, assignedOptionId: number, effectiveDate: string) {
    return this.httpClient.post(`${API_URL}/children/${childId}/assignedOptions`,
      new AssignOptionToChildDTO(effectiveDate, assignedOptionId))
  }

  removeAssignedOptionFromChild(childId: number, assignedOptionId: number) {
    return this.httpClient.delete(`${API_URL}/children/${childId}/assignedOptions/${assignedOptionId}`)
  }

}


