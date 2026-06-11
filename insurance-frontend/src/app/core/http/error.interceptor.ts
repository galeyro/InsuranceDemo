import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

export interface ApiError {
  status: number;
  error: string;
  message: string;
  details?: Record<string, string>;
  timestamp: string;
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const apiError: ApiError = {
        status: error.status,
        error: error.error?.error || error.statusText,
        message: error.error?.message || 'Ocurrió un error inesperado',
        details: error.error?.details,
        timestamp: error.error?.timestamp || new Date().toISOString(),
      };

      // TODO: Integrate with toast notification service
      console.error('API Error:', apiError);

      return throwError(() => apiError);
    })
  );
};
