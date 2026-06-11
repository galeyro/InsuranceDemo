import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-glass-button',
  standalone: true,
  template: `
    <button
      type="button"
      class="px-6 py-3 rounded-default font-medium text-body-md transition-all active:scale-95"
      [class]="variantClass()"
      [disabled]="disabled()"
      (click)="clicked.emit()"
    >
      <ng-content />
    </button>
  `,
  styles: [],
})
export class GlassButtonComponent {
  variant = input<'primary' | 'secondary' | 'ghost'>('primary');
  disabled = input(false);
  clicked = output<void>();

  variantClass() {
    const map = {
      primary: 'bg-primary text-on-primary hover:opacity-90',
      secondary: 'border border-outline-variant text-on-surface hover:bg-surface-container-high',
      ghost: 'text-primary hover:bg-surface-container-high',
    };
    return map[this.variant()];
  }
}
