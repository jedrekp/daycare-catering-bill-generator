import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CateringOption } from './CateringOption';

@Injectable({
  providedIn: 'root'
})
export class CateringOptionDataService {

  constructor(private httpClient: HttpClient) { }

  retrieveAllCateringOptions() {
    return this.httpClient.get<CateringOption[]>('http://localhost:8081/cateringOptions')
  }
}
