import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { PolicyHttpService } from '../../data/policy-http.service';
import { StateService } from '../../../../core/state/state.service';
import { canTransition, getAllowedTransitions } from '../../../../domain/services/policy-state-machine';

@Component({
  selector: 'app-policy-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslocoDirective],
  template: `
    <div class="space-y-6" *transloco="let t">
      <h2 class="font-outfit text-headline-md font-semibold text-on-surface">{{ t('policies.form.title') }}</h2>
      
      <div class="glass rounded-xl p-6 md:p-8 max-w-2xl">
        <form [formGroup]="form" (ngSubmit)="onSubmit()" class="space-y-6">
          <!-- Step 1: Customer -->
          <div>
            <label class="block text-label-sm uppercase tracking-wider text-on-surface-variant mb-2">
              {{ t('policies.form.customer') }}
            </label>
            <select
              formControlName="customerId"
              class="w-full px-4 py-3 rounded-default bg-surface-container-high border border-outline-variant text-on-surface focus:border-primary focus:outline-none transition-colors"
            >
              <option value="">{{ t('common.select') }}</option>
              <option *ngFor="let customer of state.customers()" [value]="customer.id.value">
                {{ customer.name }} ({{ customer.email.value }})
              </option>
            </select>
            <div *ngIf="form.get('customerId')!.invalid && form.get('customerId')!.touched" class="text-sm text-red-400 mt-1">
              Cliente requerido
            </div>
          </div>
          
          <!-- Step 2: Branch -->
          <div>
            <label class="block text-label-sm uppercase tracking-wider text-on-surface-variant mb-2">
              {{ t('policies.form.branch') }}
            </label>
            <div class="grid grid-cols-2 gap-3">
              <button
                type="button"
                *ngFor="let branch of branches"
                (click)="form.patchValue({ branch })"
                class="p-4 rounded-xl border transition-all text-center"
                [class.border-primary]="form.value.branch === branch"
                [class.bg-primary/10]="form.value.branch === branch"
                [class.border-outline-variant]="form.value.branch !== branch"
                [class.hover:border-primary/50]="form.value.branch !== branch"
              >
                <div class="font-medium text-on-surface">{{ branch }}</div>
              </button>
            </div>
          </div>
          
          <!-- Step 3: Rating Strategy -->
          <div>
            <label class="block text-label-sm uppercase tracking-wider text-on-surface-variant mb-2">
              {{ t('policies.form.ratingStrategy') }}
            </label>
            <div class="grid grid-cols-3 gap-3">
              <button
                type="button"
                *ngFor="let strategy of strategies"
                (click)="form.patchValue({ ratingStrategy: strategy })"
                class="p-4 rounded-xl border transition-all text-center"
                [class.border-primary]="form.value.ratingStrategy === strategy"
                [class.bg-primary/10]="form.value.ratingStrategy === strategy"
                [class.border-outline-variant]="form.value.ratingStrategy !== strategy"
                [class.hover:border-primary/50]="form.value.ratingStrategy !== strategy"
              >
                <div class="font-medium text-on-surface">{{ strategy }}</div>
              </button>
            </div>
          </div>
          
          <!-- Step 4: Risk Profile -->
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-label-sm uppercase tracking-wider text-on-surface-variant mb-2">
                {{ t('policies.form.riskScore') }}
              </label>
              <input
                type="number"
                formControlName="riskScore"
                min="0"
                max="100"
                class="w-full px-4 py-3 rounded-default bg-surface-container-high border border-outline-variant text-on-surface focus:border-primary focus:outline-none transition-colors"
              />
            </div>
            <div>
              <label class="block text-label-sm uppercase tracking-wider text-on-surface-variant mb-2">
                {{ t('policies.form.customerSinceYear') }}
              </label>
              <input
                type="number"
                formControlName="customerSinceYear"
                min="1900"
                [max]="currentYear"
                class="w-full px-4 py-3 rounded-default bg-surface-container-high border border-outline-variant text-on-surface focus:border-primary focus:outline-none transition-colors"
              />
            </div>
          </div>
          
          <div class="flex items-center gap-4 pt-4">
            <button
              type="submit"
              [disabled]="form.invalid || loading"
              class="px-8 py-3 rounded-default bg-primary text-on-primary font-medium text-body-md transition-all hover:opacity-90 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span *ngIf="!loading">{{ t('policies.form.submit') }}</span>
              <span *ngIf="loading">{{ t('common.loading') }}</span>
            </button>
            <button
              type="button"
              (click)="router.navigate(['/app/policies'])"
              class="px-8 py-3 rounded-default border border-outline-variant text-on-surface font-medium text-body-md transition-colors hover:bg-surface-container-high"
            >
              {{ t('policies.form.cancel') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [],
})
export class PolicyFormPage {
  private readonly fb = inject(FormBuilder);
  protected readonly router = inject(Router);
  private readonly policyService = inject(PolicyHttpService);
  protected readonly state = inject(StateService);

  loading = false;
  currentYear = new Date().getFullYear();
  branches = ['AUTO', 'LIFE', 'HOME', 'HEALTH'];
  strategies = ['STANDARD', 'RISK_BASED', 'LOYALTY'];

  form = this.fb.group({
    customerId: ['', Validators.required],
    branch: ['AUTO', Validators.required],
    ratingStrategy: ['STANDARD', Validators.required],
    riskScore: [50, [Validators.required, Validators.min(0), Validators.max(100)]],
    customerSinceYear: [2020, [Validators.required, Validators.min(1900)]],
  });

  onSubmit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    const { customerId, branch, ratingStrategy, riskScore, customerSinceYear } = this.form.value;

    this.policyService.create({
      customerId: customerId!,
      branch: branch as any,
      ratingStrategy: ratingStrategy as any,
      riskProfile: {
        riskScore: riskScore!,
        customerSinceYear: customerSinceYear!,
      },
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/app/policies']);
      },
      error: () => {
        this.loading = false;
      },
    });
  }
}
