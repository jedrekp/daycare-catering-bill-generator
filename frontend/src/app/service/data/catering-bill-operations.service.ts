import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { CateringBill } from 'src/app/catering-bill/catering-bill';
import { API_URL } from 'src/const';
import { CrudDataService } from './crud-data.service';

@Injectable({
  providedIn: 'root'
})
export class CateringBillOperationsService extends CrudDataService {

  constructor(http: HttpClient) {
    super(`${API_URL}/cateringBills`, http)
  }

  getBillPreview(childId: number, month: string, year: number) {
    const params = new HttpParams()
      .set('childId', childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.http.get<CateringBill>(`${API_URL}/cateringBills/generate-preview`, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

  sendCateringBillToParent(billId: number) {
    const params = new HttpParams()
      .set('cateringBillId', billId.toString())
    return this.http.get(`${API_URL}/cateringBills/send-to-parent`, { params: params })
      .pipe(
        catchError(this.handleError)
      )
  }

}
