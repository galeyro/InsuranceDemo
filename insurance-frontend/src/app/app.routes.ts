import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./features/landing/landing.routes').then((m) => m.landingRoutes),
  },
  {
    path: 'app',
    loadComponent: () => import('./core/shell/shell.component').then((m) => m.ShellComponent),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.routes').then((m) => m.dashboardRoutes),
      },
      {
        path: 'customers',
        loadChildren: () => import('./features/customers/customers.routes').then((m) => m.customersRoutes),
      },
      {
        path: 'policies',
        loadChildren: () => import('./features/policies/policies.routes').then((m) => m.policiesRoutes),
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
