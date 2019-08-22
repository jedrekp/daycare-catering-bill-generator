import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PreschoolGroup } from './preschool-group';

@Injectable({
  providedIn: 'root'
})
export class PreschoolGroupDataService {

  constructor(
    private httpClient: HttpClient
  ) { }

  retrieveGroups() {
    return this.httpClient.get<PreschoolGroup[]>('http://localhost:8081/preschoolGroups')
  }
}
