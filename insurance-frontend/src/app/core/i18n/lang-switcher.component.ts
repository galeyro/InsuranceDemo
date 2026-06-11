import { Component, inject } from '@angular/core';
import { TranslocoService } from '@jsverse/transloco';

@Component({
  selector: 'app-lang-switcher',
  standalone: true,
  template: `
    <div class="flex items-center gap-2">
      <button
        type="button"
        class="px-3 py-1 rounded-default text-sm font-medium transition-colors"
        [class.bg-primary]="transloco.getActiveLang() === 'es'"
        [class.text-on-primary]="transloco.getActiveLang() === 'es'"
        [class.text-on-surface-variant]="transloco.getActiveLang() !== 'es'"
        [class.hover\:bg-surface-container-high]="transloco.getActiveLang() !== 'es'"
        (click)="setLang('es')"
      >
        ES
      </button>
      <button
        type="button"
        class="px-3 py-1 rounded-default text-sm font-medium transition-colors"
        [class.bg-primary]="transloco.getActiveLang() === 'en'"
        [class.text-on-primary]="transloco.getActiveLang() === 'en'"
        [class.text-on-surface-variant]="transloco.getActiveLang() !== 'en'"
        [class.hover\:bg-surface-container-high]="transloco.getActiveLang() !== 'en'"
        (click)="setLang('en')"
      >
        EN
      </button>
    </div>
  `,
  styles: [],
})
export class LangSwitcherComponent {
  protected readonly transloco = inject(TranslocoService);

  setLang(lang: string): void {
    this.transloco.setActiveLang(lang);
    localStorage.setItem('lumina-lang', lang);
  }
}
