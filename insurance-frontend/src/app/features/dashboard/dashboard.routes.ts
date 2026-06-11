import { Routes } from '@angular/router';

export const dashboardRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./presentation/dashboard.page').then((m) => m.DashboardPage),
  },
];
