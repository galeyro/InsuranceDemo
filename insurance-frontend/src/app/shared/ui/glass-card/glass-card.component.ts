import { Component, input } from '@angular/core';

@Component({
  selector: 'app-glass-card',
  standalone: true,
  template: `
    <div class="glass rounded-xl p-6" [class.glass-elevated]="elevated()">
      <ng-content />
    </div>
  `,
  styles: [],
})
export class GlassCardComponent {
  elevated = input(false);
}
