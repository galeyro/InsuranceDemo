import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { CustomerHttpService } from '../../data/customer-http.service';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslocoDirective],
  template: `
    <div class="space-y-6" *transloco="let t">
      <h2 class="font-outfit text-headline-md font-semibold text-on-surface">{{ t('customers.form.title') }}</h2>
      
      <div class="glass rounded-xl p-6 md:p-8 max-w-2xl">
        <form [formGroup]="form" (ngSubmit)="onSubmit()" class="space-y-6">
          <div>
            <label class="block text-label-sm uppercase tracking-wider text-on-surface-variant mb-2">
              {{ t('customers.form.name') }}
            </label>
            <input
              type="text"
              formControlName="name"
              class="w-full px-4 py-3 rounded-default bg-surface-container-high border border-outline-variant text-on-surface placeholder-on-surface-variant/50 focus:border-primary focus:outline-none transition-colors"
              [placeholder]="t('customers.form.name')"
            />
            <div *ngIf="form.get('name')?.invalid && form.get('name')?.touched" class="text-sm text-red-400 mt-1">
              El nombre es obligatorio
            </div>
          </div>
          
          <div>
            <label class="block text-label-sm uppercase tracking-wider text-on-surface-variant mb-2">
              {{ t('customers.form.email') }}
            </label>
            <input
              type="email"
              formControlName="email"
              class="w-full px-4 py-3 rounded-default bg-surface-container-high border border-outline-variant text-on-surface placeholder-on-surface-variant/50 focus:border-primary focus:outline-none transition-colors"
              [placeholder]="t('customers.form.email')"
            />
            <div *ngIf="form.get('email')?.invalid && form.get('email')?.touched" class="text-sm text-red-400 mt-1">
              Email inválido
            </div>
          </div>
          
          <div class="flex items-center gap-4 pt-4">
            <button
              type="submit"
              [disabled]="form.invalid || loading"
              class="px-8 py-3 rounded-default bg-primary text-on-primary font-medium text-body-md transition-all hover:opacity-90 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span *ngIf="!loading">{{ t('customers.form.submit') }}</span>
              <span *ngIf="loading">{{ t('common.loading') }}</span>
            </button>
            <button
              type="button"
              (click)="router.navigate(['/app/customers'])"
              class="px-8 py-3 rounded-default border border-outline-variant text-on-surface font-medium text-body-md transition-colors hover:bg-surface-container-high"
            >
              {{ t('customers.form.cancel') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [],
})
export class CustomerFormPage {
  private readonly fb = inject(FormBuilder);
  protected readonly router = inject(Router);
  private readonly customerService = inject(CustomerHttpService);

  loading = false;

  form = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
  });

  onSubmit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    const { name, email } = this.form.value;

    this.customerService.create({ name: name!, email: email! }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/app/customers']);
      },
      error: () => {
        this.loading = false;
      },
    });
  }
}
