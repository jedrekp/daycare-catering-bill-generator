import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { JwtAuthService } from 'src/app/service/authentication/jwt-auth.service';
import { NO_RESPONSE_ERROR_MESSAGE } from 'src/const';
import { NoResponseError } from './no-response-error';
import { UnauthorizedError } from './unauthorized-error';

@Injectable()
export class DefaultErrorHandler implements ErrorHandler {

    constructor(
        private router: Router,
        private zone: NgZone,
        private authService: JwtAuthService,
        private snackbar: MatSnackBar
    ) { }

    handleError(error: any): void {
        if (error instanceof UnauthorizedError) {
            this.authService.logout()
            this.zone.run(() => {
                this.snackbar.open('Server is unable to authenticate you. Please login again.', null, { panelClass: 'dcbg-error-snackbar' })
                this.router.navigate(['login'])
            })
        } else {
            const errorMessage = error instanceof NoResponseError ? NO_RESPONSE_ERROR_MESSAGE : error.originalError?.error;
            this.zone.run(() => {
                this.router.navigate(['error'], { state: { message: errorMessage } })
            })
        }
    }

}
