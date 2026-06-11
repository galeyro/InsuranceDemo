import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from './notification.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed top-24 right-6 z-[100] flex flex-col gap-3 pointer-events-none">
      <div
        *ngFor="let toast of notificationService.activeToasts()"
        class="pointer-events-auto glass-elevated rounded-xl px-5 py-4 flex items-center gap-3 min-w-[320px] max-w-md transition-all duration-300"
        [class.border-l-4]="true"
        [class.border-l-emerald-400]="toast.type === 'success'"
        [class.border-l-red-400]="toast.type === 'error'"
        [class.border-l-amber-400]="toast.type === 'warning'"
        [class.border-l-blue-400]="toast.type === 'info'"
      >
        <div
          class="w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0"
          [class.bg-emerald-400/20]="toast.type === 'success'"
          [class.bg-red-400/20]="toast.type === 'error'"
          [class.bg-amber-400/20]="toast.type === 'warning'"
          [class.bg-blue-400/20]="toast.type === 'info'"
        >
          <svg
            *ngIf="toast.type === 'success'"
            class="w-4 h-4 text-emerald-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
          </svg>
          <svg
            *ngIf="toast.type === 'error'"
            class="w-4 h-4 text-red-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
          <svg
            *ngIf="toast.type === 'warning'"
            class="w-4 h-4 text-amber-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
          </svg>
          <svg
            *ngIf="toast.type === 'info'"
            class="w-4 h-4 text-blue-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <p class="text-sm text-on-surface flex-1">{{ toast.message }}</p>
        <button
          (click)="notificationService.remove(toast.id)"
          class="text-on-surface-variant hover:text-on-surface transition-colors"
        >
          <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
    </div>
  `,
  styles: [],
})
export class ToastContainerComponent {
  protected readonly notificationService = inject(NotificationService);
}
