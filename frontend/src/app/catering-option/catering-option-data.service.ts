import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CateringOption } from './CateringOption';
import { API_URL } from '../const';

@Injectable({
  providedIn: 'root'
})
export class CateringOptionDataService {

  constructor(private httpClient: HttpClient) { }

  retrieveAllCateringOptions() {
    return this.httpClient.get<CateringOption[]>(`${API_URL}/cateringOptions`)
  }

  retreiveCateringOptionsByDisabled(disabled: boolean) {
    let params = new HttpParams().set('disabled', disabled.toString())
    return this.httpClient.get<CateringOption[]>(`${API_URL}/cateringOptions`, { params: params })
  }

  createCateringOption(cateringOption: CateringOption) {
    return this.httpClient.post<CateringOption>(`${API_URL}/cateringOptions`, cateringOption)
  }

  editCateringOption(cateringOption: CateringOption, cateringOptionId: number) {
    return this.httpClient.put<CateringOption>(`${API_URL}/cateringOptions/${cateringOptionId}`,
      cateringOption)
  }

}
