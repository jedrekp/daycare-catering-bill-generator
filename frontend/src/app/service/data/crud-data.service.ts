import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AppError } from 'src/app/common/error/app-error';
import { ConflictError } from 'src/app/common/error/conflict-error';
import { NoResponseError } from 'src/app/common/error/no-response-error';
import { NotFoundError } from 'src/app/common/error/not-found-error';
import { UnauthorizedError } from 'src/app/common/error/unauthorized-error';
import { UnprocessableEntityError } from 'src/app/common/error/unprocessable-entity-error';

@Injectable({
  providedIn: 'root'
})
export class CrudDataService {

  constructor(
    public resourceUrl: string,
    public http: HttpClient) { }


  create(resource: any) {
    return this.http.post<any>(this.resourceUrl, resource)
      .pipe(
        catchError(this.handleError)
      )
  }

  getAll() {
    return this.http.get<any[]>(this.resourceUrl)
      .pipe(
        catchError(this.handleError)
      )
  }

  getSingle(id: number) {
    return this.http.get<any>(`${this.resourceUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      )
  }

  update(resource: any, id: number) {
    return this.http.put<any>(`${this.resourceUrl}/${id}`, resource)
      .pipe(
        catchError(this.handleError)
      )
  }

  delete(id: number) {
    return this.http.delete(`${this.resourceUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      )

  }

  protected handleError(error: Response) {
    console.log(error)
    switch (error.status) {
      case 0:
        return throwError(new NoResponseError(error))
      case 401:
        return throwError(new UnauthorizedError(error))
      case 404:
        return throwError(new NotFoundError(error))
      case 409:
        return throwError(new ConflictError(error))
      case 422:
        return throwError(new UnprocessableEntityError(error))
      default:
        return throwError(new AppError(error))
    }
  }


}
