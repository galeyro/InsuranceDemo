import { Component, inject, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { StateService } from '../../../../core/state/state.service';
import { PolicyHttpService } from '../../data/policy-http.service';
import { getAllowedTransitions } from '../../../../domain/services/policy-state-machine';
import { PolicyStatus } from '../../../../domain/enums/policy-status.enum';

@Component({
  selector: 'app-policy-detail',
  standalone: true,
  imports: [CommonModule, TranslocoDirective],
  template: `
    <div class="space-y-4 md:space-y-6" *transloco="let t">
      <div class="flex items-center gap-4">
        <button
          (click)="router.navigate(['/app/policies'])"
          class="p-2 rounded-default hover:bg-surface-container-high transition-colors"
        >
          <svg class="w-5 h-5 text-on-surface-variant" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
        </button>
        <h2 class="font-outfit text-headline-md font-semibold text-on-surface">{{ t('policies.detail.title') }}</h2>
      </div>
      
      <div *ngIf="currentPolicy() as p" class="grid grid-cols-1 lg:grid-cols-2 gap-4 lg:gap-6">
        <div class="space-y-4 md:space-y-6">
          <div class="glass rounded-xl p-4 md:p-6">
            <div class="flex flex-col xs:flex-row xs:items-center justify-between gap-4 mb-4 md:mb-6">
              <div>
                <div class="text-label-sm uppercase tracking-wider text-on-surface-variant mb-1">{{ t('policies.detail.policyNumber') }}</div>
                <div class="font-outfit text-xl xs:text-2xl font-bold text-on-surface truncate max-w-[220px] xs:max-w-none">{{ p.policyNumber }}</div>
              </div>
              <span
                class="status-badge block text-center flex-shrink-0"
                [class.status-badge-active]="p.status === 'ACTIVE'"
                [class.status-badge-quoted]="p.status === 'QUOTED'"
                [class.status-badge-issued]="p.status === 'ISSUED'"
                [class.status-badge-cancelled]="p.status === 'CANCELLED'"
                [class.status-badge-suspended]="p.status === 'SUSPENDED'"
              >
                {{ t('policies.status.' + p.status) }}
              </span>
            </div>
            
            <div class="space-y-3">
              <div class="flex justify-between py-2 border-b border-border-custom">
                <span class="text-on-surface-variant">{{ t('policies.form.branch') }}</span>
                <span class="text-on-surface font-medium">{{ t('policies.branch.' + p.branch) }}</span>
              </div>
              <div class="flex justify-between py-2 border-b border-border-custom">
                <span class="text-on-surface-variant">{{ t('policies.detail.monthlyPremium') }}</span>
                <span class="text-on-surface font-medium">$ {{ p.monthlyPremium.amount | number:'1.0-2' }}</span>
              </div>
              <div class="flex justify-between py-2 border-b border-border-custom">
                <span class="text-on-surface-variant">{{ t('policies.detail.coverage') }}</span>
                <span class="text-on-surface font-medium">$ {{ p.coverage.coverageAmount.amount | number:'1.0-2' }}</span>
              </div>
              <div class="flex justify-between py-2 border-b border-border-custom">
                <span class="text-on-surface-variant">{{ t('policies.detail.termMonths') }}</span>
                <span class="text-on-surface font-medium">
                  {{ p.coverage.termMonths ? p.coverage.termMonths + ' ' + (p.coverage.termMonths === 1 ? t('common.month') : t('common.months')) : t('policies.detail.indefinite') }}
                </span>
              </div>
              <div class="flex justify-between py-2">
                <span class="text-on-surface-variant">{{ t('policies.form.ratingStrategy') }}</span>
                <span class="text-on-surface font-medium">{{ t('policies.ratingStrategy.' + p.ratingStrategy) }}</span>
              </div>
            </div>
          </div>
          
          <!-- State Transitions -->
          <div class="glass rounded-xl p-4 md:p-6" *ngIf="currentTransitions().length > 0">
            <h3 class="font-outfit text-lg font-semibold text-on-surface mb-4">{{ t('policies.detail.transition') }}</h3>
            <div class="flex flex-wrap gap-3">
              <button
                *ngFor="let transition of currentTransitions()"
                (click)="transitionTo(transition)"
                [disabled]="transitionLoading"
                class="status-badge py-3 px-6 hover:scale-105 active:scale-95 transition-all cursor-pointer disabled:opacity-50"
                [class.status-badge-active]="transition === 'ACTIVE'"
                [class.status-badge-issued]="transition === 'ISSUED'"
                [class.status-badge-suspended]="transition === 'SUSPENDED'"
                [class.status-badge-cancelled]="transition === 'CANCELLED'"
              >
                {{ t('policies.status.' + transition) }}
              </button>
            </div>
          </div>
        </div>
        
        <div class="space-y-4 md:space-y-6">
          <div class="glass rounded-xl p-4 md:p-6">
            <h3 class="font-outfit text-lg font-semibold text-on-surface mb-4">{{ t('policies.form.ratingStrategy') }}</h3>
            <div class="space-y-3">
              <div class="flex justify-between py-2 border-b border-border-custom">
                <span class="text-on-surface-variant">{{ t('policies.form.riskScore') }}</span>
                <span class="text-on-surface font-medium">{{ p.riskProfile.riskScore }}/100</span>
              </div>
              <div class="flex justify-between py-2">
                <span class="text-on-surface-variant">{{ t('policies.form.customerSinceYear') }}</span>
                <span class="text-on-surface font-medium">{{ p.riskProfile.customerSinceYear }}</span>
              </div>
            </div>
            
            <div class="mt-4 h-2 bg-progress-bg-custom rounded-full overflow-hidden">
              <div
                class="h-full rounded-full transition-all"
                [class.bg-emerald-500]="p.riskProfile.riskScore < 40"
                [class.bg-amber-500]="p.riskProfile.riskScore >= 40 && p.riskProfile.riskScore < 70"
                [class.bg-red-500]="p.riskProfile.riskScore >= 70"
                [style.width.%]="p.riskProfile.riskScore"
              ></div>
            </div>
          </div>
          
          <div class="glass rounded-xl p-4 md:p-6">
            <h3 class="font-outfit text-lg font-semibold text-on-surface mb-4">{{ t('policies.detail.timeline') }}</h3>
            <div class="space-y-4">
              <div class="flex items-center gap-3">
                <div class="w-3 h-3 rounded-full bg-emerald-500"></div>
                <div>
                  <div class="text-sm text-on-surface">{{ t('policies.detail.created') }}</div>
                  <div class="text-xs text-on-surface-variant">{{ p.createdAt | date:'medium' }}</div>
                </div>
              </div>
              <div class="flex items-center gap-3">
                <div class="w-3 h-3 rounded-full bg-blue-500"></div>
                <div>
                  <div class="text-sm text-on-surface">{{ t('policies.detail.lastUpdated') }}</div>
                  <div class="text-xs text-on-surface-variant">{{ p.updatedAt | date:'medium' }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div *ngIf="!currentPolicy()" class="glass rounded-xl p-12 text-center">
        <p class="text-body-md text-on-surface-variant">{{ t('common.loading') }}</p>
      </div>
    </div>
  `,
  styles: [],
})
export class PolicyDetailPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  protected readonly router = inject(Router);
  private readonly state = inject(StateService);
  private readonly policyService = inject(PolicyHttpService);

  transitionLoading = false;

  currentPolicy = computed(() => {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return undefined;
    return this.state.getPolicyById(id);
  });

  currentTransitions = computed(() => {
    const p = this.currentPolicy();
    if (!p) return [];
    return getAllowedTransitions(p.status);
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return;

    const existing = this.state.getPolicyById(id);
    if (!existing) {
      this.policyService.getById(id).subscribe();
    }
  }

  transitionTo(status: PolicyStatus): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return;

    const current = this.currentPolicy();
    const policyNumber = current ? current.policyNumber : 'POL-UNKNOWN';
    const oldStatus = current ? current.status : 'UNKNOWN';

    this.transitionLoading = true;
    this.policyService.updateStatus(id, { targetStatus: status }).subscribe({
      next: (updated) => {
        this.transitionLoading = false;
        this.state.addTransition({
          policyNumber: updated.policyNumber || policyNumber,
          oldStatus: oldStatus,
          newStatus: updated.status,
          timestamp: new Date(),
          success: true
        });
      },
      error: () => {
        this.transitionLoading = false;
        this.state.addTransition({
          policyNumber: policyNumber,
          oldStatus: oldStatus,
          newStatus: status,
          timestamp: new Date(),
          success: false
        });
      },
    });
  }
}
