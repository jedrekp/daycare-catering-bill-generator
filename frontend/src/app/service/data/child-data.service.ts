import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { CateringBill } from 'src/app/catering-bill/catering-bill';
import { AssignedCateringOption } from 'src/app/child/child';
import { API_URL } from 'src/const';
import { CrudDataService } from './crud-data.service';

@Injectable({
  providedIn: 'root'
})
export class ChildDataService extends CrudDataService {

  constructor(http: HttpClient) {
    super(`${API_URL}/children`, http)
  }

  assignNewOptionToChild(childId: number, optionId: number, date: string) {
    const assignOptionToChildRequest = {
      effectiveDate: date,
      cateringOptionId: optionId
    }
    return this.http.post<AssignedCateringOption>(`${this.resourceUrl}/${childId}/assignedOptions`, assignOptionToChildRequest)
      .pipe(
        catchError(this.handleError)
      )
  }

  removeAssignedOptionFromChild(childId: number, effectiveDate: string) {
    const params = new HttpParams()
      .set("effectiveDate", effectiveDate)
    return this.http.delete(`${this.resourceUrl}/${childId}/assignedOptions`, { params: params })
  }

  saveCateringBill(cateringBill: CateringBill) {
    const newBillRequest = {
      month: cateringBill.month.toUpperCase(),
      year: cateringBill.year,
      dailyCateringOrders: cateringBill.dailyCateringOrders
    }
    return this.http.post<any>(`${this.resourceUrl}/${cateringBill.childId}/cateringBills`, newBillRequest)
      .pipe(
        catchError(this.handleError)
      )
  }

}
