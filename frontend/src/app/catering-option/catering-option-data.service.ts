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

  retriveCateringOptionsByDisabled(disabled: boolean) {
    let params = new HttpParams().set('disabled', disabled.toString())
    return this.httpClient.get<CateringOption[]>('http://localhost:8081/cateringOptions', { params: params })
  }

  createCateringOption(cateringOption: CateringOption) {
    return this.httpClient.post<CateringOption>('http://localhost:8081/cateringOptions', cateringOption)
  }

  editCateringOption(cateringOption: CateringOption, cateringOptionId: number) {
    return this.httpClient.put<CateringOption>(`http://localhost:8081/cateringOptions/${cateringOptionId}`,
      cateringOption)
  }

}
