import { Injectable } from '@angular/core';
import { NO_RESPONSE_MESSAGE, UNEXPECTED_ERROR_MESSAGE } from '../const';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(
    private router: Router
  ) { }

  getErrorMessage(err) {
    let statusCode = err.status
    if (statusCode == 0) {
      return NO_RESPONSE_MESSAGE
    } else if (statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 404 || statusCode == 409) {
      return err.error
    }
    return UNEXPECTED_ERROR_MESSAGE
  }

  redirectToErrorPage(err) {
    this.router.navigate(['error'], { state: { message: this.getErrorMessage(err) } })
  }

}
