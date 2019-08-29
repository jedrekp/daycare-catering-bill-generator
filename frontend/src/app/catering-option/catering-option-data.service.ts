import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CateringOption } from './CateringOption';

@Injectable({
  providedIn: 'root'
})
export class CateringOptionDataService {

  constructor(private httpClient: HttpClient) { }

  retrieveAllCateringOptions() {
    return this.httpClient.get<CateringOption[]>('http://localhost:8081/cateringOptions')
  }

  retrieveAllActiveCateringOptions() {
    let params = new HttpParams().set('disabled', 'false')
    return this.httpClient.get<CateringOption[]>('http://localhost:8081/cateringOptions', { params: params })
  }
}
