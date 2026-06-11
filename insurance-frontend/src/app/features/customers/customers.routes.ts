import { Routes } from '@angular/router';

export const customersRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./presentation/customer-list/customer-list.page').then((m) => m.CustomerListPage),
  },
  {
    path: 'new',
    loadComponent: () => import('./presentation/customer-form/customer-form.page').then((m) => m.CustomerFormPage),
  },
  {
    path: ':id',
    loadComponent: () => import('./presentation/customer-detail/customer-detail.page').then((m) => m.CustomerDetailPage),
  },
];
