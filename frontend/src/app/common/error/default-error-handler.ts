import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { NO_RESPONSE_ERROR_MESSAGE } from 'src/const';
import { NoResponseError } from './no-response-error';

@Injectable()
export class DefaultErrorHandler implements ErrorHandler {

    constructor(
        private router: Router,
        private zone: NgZone
    ) { }

    handleError(error: any): void {
        const errorMessage = error instanceof NoResponseError ? NO_RESPONSE_ERROR_MESSAGE : error.originalError?.error;
        this.zone.run(() => {
            this.router.navigate(['error'], { state: { message: errorMessage } });
        });
    }

}
