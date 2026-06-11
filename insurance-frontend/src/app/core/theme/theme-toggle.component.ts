import { Component, inject } from '@angular/core';
import { ThemeService } from './theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  template: `
    <button
      type="button"
      class="theme-toggle glass rounded-full p-2 flex items-center gap-2 text-sm transition-colors"
      [attr.aria-label]="'Cambiar a modo ' + (themeService.theme() === 'dark' ? 'claro' : 'oscuro')"
      (click)="themeService.toggle()"
    >
      <span class="sr-only">Tema</span>
      <svg
        class="w-5 h-5 transition-colors"
        [class.text-primary]="themeService.theme() === 'light'"
        [class.text-on-surface-variant]="themeService.theme() !== 'light'"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"
        />
      </svg>
      <svg
        class="w-5 h-5 transition-colors"
        [class.text-primary]="themeService.theme() === 'dark'"
        [class.text-on-surface-variant]="themeService.theme() !== 'dark'"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"
        />
      </svg>
    </button>
  `,
  styles: `
    .theme-toggle:hover {
      background: color-mix(in oklab, var(--on-surface) 8%, transparent);
    }
  `,
})
export class ThemeToggleComponent {
  protected readonly themeService = inject(ThemeService);
}
