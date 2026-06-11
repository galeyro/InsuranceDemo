import { Component, inject, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { StateService } from '../../../../core/state/state.service';
import { CustomerHttpService } from '../../data/customer-http.service';
import { PolicyHttpService } from '../../../policies/data/policy-http.service';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslocoDirective],
  template: `
    <div class="space-y-6" *transloco="let t">
      <div class="flex items-center gap-4">
        <button
          (click)="router.navigate(['/app/customers'])"
          class="p-2 rounded-default hover:bg-surface-container-high transition-colors"
        >
          <svg class="w-5 h-5 text-on-surface-variant" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
        </button>
        <h2 class="font-outfit text-headline-md font-semibold text-on-surface">{{ t('customers.detail.title') }}</h2>
      </div>
      
      <div *ngIf="currentCustomer() as c" class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div class="glass rounded-xl p-6">
          <div class="flex items-center gap-4 mb-6">
            <div class="w-16 h-16 rounded-full bg-primary/20 flex items-center justify-center">
              <span class="text-primary font-bold text-2xl">{{ c.name[0] }}</span>
            </div>
            <div>
              <h3 class="font-outfit text-xl font-semibold text-on-surface">{{ c.name }}</h3>
              <p class="text-on-surface-variant">{{ c.email.value }}</p>
            </div>
          </div>
          
          <div class="space-y-3">
            <div class="flex justify-between items-center py-2 border-b border-border-custom gap-2 min-w-0">
              <span class="text-on-surface-variant flex-shrink-0">ID</span>
              <span class="text-on-surface font-mono text-xs xs:text-sm truncate max-w-[150px] xs:max-w-none" [title]="c.id.value">{{ c.id.value }}</span>
            </div>
            <div class="flex justify-between py-2 border-b border-border-custom">
              <span class="text-on-surface-variant">{{ t('common.actions') }}</span>
              <span class="text-on-surface font-medium">{{ c.active ? 'Activo' : 'Inactivo' }}</span>
            </div>
            <div class="flex justify-between py-2">
              <span class="text-on-surface-variant">Registro</span>
              <span class="text-on-surface">{{ c.createdAt | date:'medium' }}</span>
            </div>
          </div>
        </div>
        
        <div class="glass rounded-xl p-6">
          <h3 class="font-outfit text-lg font-semibold text-on-surface mb-4">{{ t('policies.title') }}</h3>
          
          <div *ngIf="currentPolicies().length === 0" class="text-center py-8">
            <p class="text-on-surface-variant mb-4">{{ t('common.noData') }}</p>
            <a
              routerLink="/app/policies/new"
              class="inline-flex px-6 py-3 rounded-default bg-primary text-on-primary font-medium text-body-md transition-colors hover:opacity-90"
            >
              {{ t('policies.new') }}
            </a>
          </div>
          
          <div *ngIf="currentPolicies().length > 0" class="space-y-3">
            <div
              *ngFor="let policy of currentPolicies()"
              class="flex items-center justify-between p-4 rounded-lg bg-surface-container-high/40 border border-border-custom gap-2 min-w-0"
            >
              <div class="min-w-0">
                <div class="font-medium text-on-surface truncate" [title]="policy.policyNumber">{{ policy.policyNumber }}</div>
                <div class="text-sm text-on-surface-variant">{{ policy.branch }}</div>
              </div>
              <div class="flex items-center gap-3 flex-shrink-0">
                <span
                  class="status-badge"
                  [class.status-badge-active]="policy.status === 'ACTIVE'"
                  [class.status-badge-quoted]="policy.status === 'QUOTED'"
                  [class.status-badge-issued]="policy.status === 'ISSUED'"
                  [class.status-badge-cancelled]="policy.status === 'CANCELLED'"
                  [class.status-badge-suspended]="policy.status === 'SUSPENDED'"
                >
                  {{ policy.status }}
                </span>
                <a
                  [routerLink]="['/app/policies', policy.id.value]"
                  class="text-primary hover:text-primary-container font-medium transition-colors"
                >
                  {{ t('common.view') }}
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div *ngIf="!currentCustomer()" class="glass rounded-xl p-12 text-center">
        <p class="text-body-md text-on-surface-variant">{{ t('common.loading') }}</p>
      </div>
    </div>
  `,
  styles: [],
})
export class CustomerDetailPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  protected readonly router = inject(Router);
  private readonly state = inject(StateService);
  private readonly customerService = inject(CustomerHttpService);
  private readonly policyService = inject(PolicyHttpService);

  currentCustomer = computed(() => {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return undefined;
    return this.state.getCustomerById(id);
  });

  currentPolicies = computed(() => {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return [];
    return this.state.getPoliciesByCustomer(id);
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return;

    const existing = this.state.getCustomerById(id);
    if (!existing) {
      this.customerService.getById(id).subscribe();
    }
    this.policyService.getByCustomer(id).subscribe();
  }
}
