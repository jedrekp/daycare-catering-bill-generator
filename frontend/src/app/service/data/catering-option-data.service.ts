import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { CateringOption } from 'src/app/catering-option/catering-option';
import { API_URL } from 'src/const';
import { CrudDataService } from './crud-data.service';

@Injectable({
  providedIn: 'root'
})
export class CateringOptionDataService extends CrudDataService {

  constructor(http: HttpClient) {
    super(`${API_URL}/cateringOptions`, http)
  }

  getAllByDisabled(disabled: boolean) {
    const params = new HttpParams()
      .set('disabled', disabled ? 'true' : 'false')

    return this.http.get<CateringOption[]>(`${this.resourceUrl}`)
      .pipe(
        catchError(this.handleError)
      )
  }

}
