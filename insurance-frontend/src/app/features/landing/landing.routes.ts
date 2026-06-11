import { Routes } from '@angular/router';

export const landingRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./landing-page.component').then((m) => m.LandingPageComponent),
  },
];
