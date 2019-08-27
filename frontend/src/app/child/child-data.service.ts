import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Child } from './child';

@Injectable({
  providedIn: 'root'
})
export class ChildDataService {

  constructor(private httpClient: HttpClient) { }

  retrieveChild(childId: number) {
    return this.httpClient.get<Child>(`http://localhost:8081/children/${childId}`)
  }

  createChild(child: Child) {
    return this.httpClient.post<Child>('http://localhost:8081/children/', child)
  }

  editChild(childId: number, child: Child) {
    return this.httpClient.put<Child>(`http://localhost:8081/children/${childId}`, child)
  }

  assignToPreschoolGroup(childId: number, groupId: number) {
    return this.httpClient.put<Child>(`http://localhost:8081/children/${childId}/preschoolGroups/${groupId}`, null)
  }

  assignNewDietToChild(childId: number, dietId: number, effectiveDate: string) {
    return this.httpClient.post<Child>(`http://localhost:8081/children/${childId}/assignedDiets`,
      new AssignedDietDTO(effectiveDate, dietId))
  }

}

class AssignedDietDTO {
  constructor(
    private effectiveDate: string,
    private dietId: number) { }
}
