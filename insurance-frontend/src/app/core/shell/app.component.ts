import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslocoService } from '@jsverse/transloco';
import { ToastContainerComponent } from '../notification/toast-container.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastContainerComponent],
  template: `
    <app-toast-container />
    <router-outlet />
  `,
  styles: [],
})
export class AppComponent {
  protected readonly transloco = inject(TranslocoService);

  constructor() {
    const stored = localStorage.getItem('lumina-lang');
    if (stored === 'es' || stored === 'en') {
      this.transloco.setActiveLang(stored);
    }
  }
}
