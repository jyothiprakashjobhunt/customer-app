import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        const message = this.resolveErrorMessage(error);
        console.error('[ErrorInterceptor]', error.status, message);
        return throwError(() => new Error(message));
      })
    );
  }

  private resolveErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Cannot connect to the server. Please ensure the backend is running.';
    }

    // 400 validation errors return { field: message } — join them into a readable string
    if (error.status === 400 && error.error && typeof error.error === 'object') {
      const messages = Object.values(error.error as Record<string, string>);
      return messages.length > 0 ? messages.join(' ') : 'Validation failed.';
    }

    // 404, 500 return { status, message, timestamp }
    if (error.error?.message) {
      return error.error.message;
    }

    return 'An unexpected error occurred.';
  }
}
