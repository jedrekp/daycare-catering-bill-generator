import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CateringBill } from './catering-bill';
import { API_URL } from '../const';

@Injectable({
  providedIn: 'root'
})

export class CateringBillOperationsService {

  constructor(
    private httpClient: HttpClient
  ) { }

  getCateringBillPreview(childId: number, month: string, year: number) {
    let params = new HttpParams()
      .set('childId', childId.toString())
      .set('month', month)
      .set('year', year.toString())
    return this.httpClient.get<CateringBill>(`${API_URL}/cateringBills/generate-preview`, { params: params })
  }

}
