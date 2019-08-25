import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Diet } from './Diet';

@Injectable({
  providedIn: 'root'
})
export class DietDataService {

  constructor(private httpClient: HttpClient) { }

  retrieveAllDiets() {
    return this.httpClient.get<Diet[]>('http://localhost:8081/diets')
  }
}
