import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Child } from './child';
import { API_URL } from '../const';
import { DailyCateringOrder, CateringBill } from '../catering-bill/catering-bill';


class AssignOptionToChildDTO {
  constructor(
    private effectiveDate: string,
    private cateringOptionId: number
  ) { }
}

class CateringBillDTO {
  constructor(
    private month: string,
    private year: number,
    private dailyCateringOrders: DailyCateringOrder[]
  ) { }
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

  removeAssignedOptionFromChild(childId: number, effectiveDate: string) {
    let params = new HttpParams().set("effectiveDate", effectiveDate)
    return this.httpClient.delete(`${API_URL}/children/${childId}/assignedOptions/`, { params: params })
  }

  saveCateringBill(childId: number, month: string, year: number, dailyCateringOrders: DailyCateringOrder[]) {
    return this.httpClient.post<any>(`${API_URL}/children/${childId}/cateringBills`, new CateringBillDTO(month, year, dailyCateringOrders))
  }

}


