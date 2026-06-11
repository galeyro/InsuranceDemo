import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { signal } from '@angular/core';

const loadingCount = signal(0);

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  loadingCount.update((c) => c + 1);

  return next(req).pipe(
    finalize(() => {
      loadingCount.update((c) => Math.max(0, c - 1));
    })
  );
};

export function isLoading() {
  return loadingCount() > 0;
}
