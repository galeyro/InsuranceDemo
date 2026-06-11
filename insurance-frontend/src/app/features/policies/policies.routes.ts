import { Routes } from '@angular/router';

export const policiesRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./presentation/policy-list/policy-list.page').then((m) => m.PolicyListPage),
  },
  {
    path: 'new',
    loadComponent: () => import('./presentation/policy-form/policy-form.page').then((m) => m.PolicyFormPage),
  },
  {
    path: ':id',
    loadComponent: () => import('./presentation/policy-detail/policy-detail.page').then((m) => m.PolicyDetailPage),
  },
];
