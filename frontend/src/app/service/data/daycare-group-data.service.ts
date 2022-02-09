import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { CateringBill } from 'src/app/catering-bill/catering-bill';
import { Child } from 'src/app/child/child';
import { DaycareGroup } from 'src/app/daycare-group/daycare-group';
import { API_URL } from 'src/const';
import { CrudDataService } from './crud-data.service';

@Injectable({
  providedIn: 'root'
})
export class DaycareGroupDataService extends CrudDataService {

  constructor(http: HttpClient) {
    super(`${API_URL}/daycareGroups`, http)
  }

  addChildToDaycareGroup(daycareGroupId: number, childId: number) {
    return this.http.put<DaycareGroup>(`${this.resourceUrl}/${daycareGroupId}/children/${childId}`, null)
      .pipe(
        catchError(this.handleError)
      )
  }

  removeChildFromDaycareGroup(daycareGroupId: number, childId: number) {
    return this.http.delete(`${this.resourceUrl}/${daycareGroupId}/children/${childId}`)
      .pipe(
        catchError(this.handleError)
      )
  }

  getAllByGroupSupervisorId(groupSupervisorId: number) {
    const params = new HttpParams()
      .set("groupSupervisorId", groupSupervisorId.toString())
    return this.http.get<DaycareGroup[]>(`${this.resourceUrl}`, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

  getAllChildrenFromGroup(daycareGroupId: number) {
    return this.http.get<Child[]>(`${this.resourceUrl}/${daycareGroupId}/children`)
      .pipe(
        catchError(this.handleError)
      )
  }

  getBillsFromSpecificMonthForAllChildrenInGroup(daycareGroupId: number, month: string, year: number) {
    const params = new HttpParams()
      .set('month', month)
      .set('year', year.toString())
    return this.http.get<CateringBill[]>(`${this.resourceUrl}/${daycareGroupId}/cateringBills`, { params: params })
  }

}
