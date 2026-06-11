import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { StateService } from '../../../core/state/state.service';
import { CustomerHttpService } from '../../customers/data/customer-http.service';
import { PolicyHttpService } from '../../policies/data/policy-http.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslocoDirective],
  template: `
    <div class="space-y-8 animate-fade-in" *transloco="let t">
      
      <!-- Welcome/Greeting Banner -->
      <div class="relative overflow-hidden rounded-2xl bg-gradient-to-r from-primary-container/20 via-primary-container/5 to-transparent border border-primary/20 p-6 md:p-8">
        <div class="absolute right-0 top-0 w-1/3 h-full bg-radial-gradient from-primary/10 to-transparent pointer-events-none"></div>
        <div class="relative z-10 space-y-2">
          <span class="text-xs font-bold uppercase tracking-widest text-primary flex items-center gap-1.5">
            <span class="material-symbols-outlined text-sm">auto_awesome</span> 
            {{ t('landing.hero.badge') }}
          </span>
          <h2 class="font-space-grotesk text-headline-lg font-bold text-on-surface">
            {{ t('dashboard.title') }}
          </h2>
          <p class="text-body-md text-on-surface-variant max-w-xl">
            {{ t('dashboard.subtitle') }}
          </p>
        </div>
      </div>

      <!-- KPI Summary Cards -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <!-- Customers KPI -->
        <div class="glass-card rounded-2xl p-6 hover:scale-[1.02] hover:border-primary/45 hover:shadow-[0_8px_32px_-16px_rgba(255,126,10,0.3)] transition-all duration-300 flex justify-between items-center group">
          <div class="space-y-2">
            <h3 class="text-label-sm uppercase tracking-wider text-on-surface-variant font-semibold">{{ t('dashboard.customers') }}</h3>
            <p class="font-space-grotesk text-display-md font-bold text-on-surface group-hover:text-primary transition-colors">{{ state.customers().length }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-primary/10 flex items-center justify-center text-primary group-hover:bg-primary group-hover:text-on-primary transition-all duration-300">
            <span class="material-symbols-outlined text-2xl">group</span>
          </div>
        </div>

        <!-- Active Policies KPI -->
        <div class="glass-card rounded-2xl p-6 hover:scale-[1.02] hover:border-primary/45 hover:shadow-[0_8px_32px_-16px_rgba(255,126,10,0.3)] transition-all duration-300 flex justify-between items-center group">
          <div class="space-y-2">
            <h3 class="text-label-sm uppercase tracking-wider text-on-surface-variant font-semibold">{{ t('dashboard.activePolicies') }}</h3>
            <p class="font-space-grotesk text-display-md font-bold text-on-surface group-hover:text-primary transition-colors">{{ state.activePoliciesCount }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-primary/10 flex items-center justify-center text-primary group-hover:bg-primary group-hover:text-on-primary transition-all duration-300">
            <span class="material-symbols-outlined text-2xl">shield_with_heart</span>
          </div>
        </div>

        <!-- Monthly Premium KPI -->
        <div class="glass-card rounded-2xl p-6 hover:scale-[1.02] hover:border-primary/45 hover:shadow-[0_8px_32px_-16px_rgba(255,126,10,0.3)] transition-all duration-300 flex justify-between items-center group">
          <div class="space-y-2">
            <h3 class="text-label-sm uppercase tracking-wider text-on-surface-variant font-semibold">{{ t('dashboard.monthlyPremium') }}</h3>
            <p class="font-space-grotesk text-display-md font-bold text-on-surface group-hover:text-primary transition-colors">$ {{ state.totalMonthlyPremium | number:'1.2-2' }}</p>
          </div>
          <div class="w-12 h-12 rounded-xl bg-primary/10 flex items-center justify-center text-primary group-hover:bg-primary group-hover:text-on-primary transition-all duration-300">
            <span class="material-symbols-outlined text-2xl">payments</span>
          </div>
        </div>
      </div>

      <!-- Bento Layout Grid -->
      <div class="grid grid-cols-1 xl:grid-cols-3 gap-6">
        
        <!-- Left Side: Payment History & Risk Distribution (Col span 2) -->
        <div class="xl:col-span-2 space-y-6">
          
          <!-- Historial de Transiciones Card -->
          <div class="glass-card rounded-2xl p-6 md:p-8">
            <div class="flex justify-between items-center mb-6">
              <h3 class="font-space-grotesk text-headline-sm font-semibold text-on-surface">{{ t('dashboard.kafkaHistory') }}</h3>
              <span class="text-xs text-on-surface-variant bg-surface-container-high px-3 py-1 rounded-full font-semibold">{{ t('dashboard.hotEvents') }}</span>
            </div>
            
            <div class="divide-y divide-border-custom">
              <!-- Empty State -->
              <div *ngIf="state.transitions().length === 0" class="py-8 text-center space-y-3">
                <div class="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center text-primary mx-auto">
                  <span class="material-symbols-outlined text-2xl">sync_alt</span>
                </div>
                <div class="space-y-1">
                  <p class="font-semibold text-on-surface text-body-md">{{ t('dashboard.emptyHistoryTitle') }}</p>
                  <p class="text-xs text-on-surface-variant max-w-md mx-auto">
                    {{ t('dashboard.emptyHistoryDescription') }}
                  </p>
                </div>
              </div>

              <!-- List of transitions -->
              <div 
                *ngFor="let trans of state.transitions()" 
                class="flex items-center justify-between py-4 first:pt-0 hover:bg-hover-custom px-3 rounded-xl transition-colors cursor-pointer group gap-2 min-w-0"
              >
                <div class="flex items-center gap-4 min-w-0">
                  <div 
                    class="w-10 h-10 rounded-xl flex-shrink-0 flex items-center justify-center group-hover:scale-105 transition-transform"
                    [style.background-color]="trans.success ? 'var(--status-active-bg)' : 'var(--status-cancelled-bg)'"
                    [style.color]="trans.success ? 'var(--status-active-text)' : 'var(--status-cancelled-text)'"
                  >
                    <span class="material-symbols-outlined">{{ trans.success ? 'sync' : 'report' }}</span>
                  </div>
                  <div class="min-w-0">
                    <p class="font-semibold text-on-surface group-hover:text-primary transition-colors text-sm truncate">
                      Transición Póliza {{ trans.policyNumber }}
                    </p>
                    <p class="text-xs text-on-surface-variant truncate">
                      {{ trans.oldStatus }} ➔ {{ trans.newStatus }} • Encolado en Kafka
                    </p>
                  </div>
                </div>
                <div class="text-right flex-shrink-0">
                  <p class="font-mono text-xs text-on-surface-variant font-medium">{{ trans.timestamp | date:'HH:mm:ss' }}</p>
                  <span 
                    class="text-[10px] px-2 py-0.5 rounded font-bold tracking-wider"
                    [style.background-color]="trans.success ? 'var(--status-active-bg)' : 'var(--status-cancelled-bg)'"
                    [style.color]="trans.success ? 'var(--status-active-text)' : 'var(--status-cancelled-text)'"
                  >
                    {{ trans.success ? 'ENCOLADO' : 'FALLIDO' }}
                  </span>
                </div>
              </div>
            </div>
          </div>

          <!-- Risk Distribution Card -->
          <div class="glass-card rounded-2xl p-6 md:p-8">
            <h3 class="font-space-grotesk text-headline-sm font-semibold text-on-surface mb-6">{{ t('dashboard.riskDistribution') }}</h3>
            <div class="space-y-6">
              <!-- AUTO -->
              <div>
                <div class="flex justify-between mb-2 text-sm font-medium">
                  <span class="text-on-surface-variant">{{ t('policies.form.branch') }} {{ t('policies.branch.AUTO') }} (AUTO)</span>
                  <span class="text-primary font-semibold">{{ getPolicyPercent('AUTO') }}% ({{ getPolicyCount('AUTO') }})</span>
                </div>
                <div class="w-full bg-progress-bg-custom h-2.5 rounded-full overflow-hidden">
                  <div class="bg-primary h-full transition-all duration-500" [style.width.%]="getPolicyPercent('AUTO')"></div>
                </div>
              </div>
              <!-- LIFE -->
              <div>
                <div class="flex justify-between mb-2 text-sm font-medium">
                  <span class="text-on-surface-variant">{{ t('policies.form.branch') }} {{ t('policies.branch.LIFE') }} (LIFE)</span>
                  <span class="text-primary font-semibold">{{ getPolicyPercent('LIFE') }}% ({{ getPolicyCount('LIFE') }})</span>
                </div>
                <div class="w-full bg-progress-bg-custom h-2.5 rounded-full overflow-hidden">
                  <div class="bg-primary h-full transition-all duration-500" [style.width.%]="getPolicyPercent('LIFE')"></div>
                </div>
              </div>
              <!-- HOME -->
              <div>
                <div class="flex justify-between mb-2 text-sm font-medium">
                  <span class="text-on-surface-variant">{{ t('policies.form.branch') }} {{ t('policies.branch.HOME') }} (HOME)</span>
                  <span class="text-primary font-semibold">{{ getPolicyPercent('HOME') }}% ({{ getPolicyCount('HOME') }})</span>
                </div>
                <div class="w-full bg-progress-bg-custom h-2.5 rounded-full overflow-hidden">
                  <div class="bg-primary h-full transition-all duration-500" [style.width.%]="getPolicyPercent('HOME')"></div>
                </div>
              </div>
              <!-- HEALTH -->
              <div>
                <div class="flex justify-between mb-2 text-sm font-medium">
                  <span class="text-on-surface-variant">{{ t('policies.form.branch') }} {{ t('policies.branch.HEALTH') }} (HEALTH)</span>
                  <span class="text-primary font-semibold">{{ getPolicyPercent('HEALTH') }}% ({{ getPolicyCount('HEALTH') }})</span>
                </div>
                <div class="w-full bg-progress-bg-custom h-2.5 rounded-full overflow-hidden">
                  <div class="bg-primary h-full transition-all duration-500" [style.width.%]="getPolicyPercent('HEALTH')"></div>
                </div>
              </div>
            </div>
          </div>

        </div>

        <!-- Right Side: Recent Policies list (Col span 1) -->
        <div class="glass-card rounded-2xl p-6 md:p-8 flex flex-col justify-between">
          <div>
            <div class="flex justify-between items-center mb-6">
              <h3 class="font-space-grotesk text-headline-sm font-semibold text-on-surface">{{ t('dashboard.recentTitle') }}</h3>
              <span class="text-xs text-on-surface-variant bg-surface-container-high px-3 py-1 rounded-full font-semibold">Top 5</span>
            </div>

            <!-- Empty state -->
            <div *ngIf="state.policies().length === 0" class="py-12 text-center text-on-surface-variant">
              <div class="w-12 h-12 bg-surface-container-high rounded-xl flex items-center justify-center mx-auto mb-3">
                <span class="material-symbols-outlined text-2xl text-on-surface-variant/40">description</span>
              </div>
              <p class="text-sm">{{ t('common.noData') }}</p>
            </div>

            <!-- List of Policies -->
            <div *ngIf="state.policies().length > 0" class="divide-y divide-border-custom">
              <div
                *ngFor="let policy of state.policies().slice(0, 5)"
                class="flex items-center justify-between py-4 first:pt-0 last:pb-0 hover:bg-hover-custom px-2 rounded-xl transition-colors group gap-2 min-w-0"
              >
                <div class="flex items-center gap-3 min-w-0">
                  <div class="w-9 h-9 rounded-xl bg-primary/10 flex-shrink-0 flex items-center justify-center text-primary font-bold text-xs group-hover:scale-105 transition-transform">
                    {{ policy.branch[0] }}
                  </div>
                  <div class="min-w-0">
                    <div class="font-bold text-on-surface group-hover:text-primary transition-colors text-sm truncate">{{ policy.policyNumber }}</div>
                    <div class="text-[10px] text-on-surface-variant flex items-center gap-1.5 truncate">
                      <span>{{ t('policies.branch.' + policy.branch) }}</span>
                      <span class="w-1.5 h-1.5 rounded-full bg-border-custom flex-shrink-0"></span>
                      <span class="truncate">{{ t('policies.ratingStrategy.' + policy.ratingStrategy) }}</span>
                    </div>
                  </div>
                </div>
                <div class="text-right flex-shrink-0">
                  <span
                    class="status-badge block mb-1 text-center justify-center"
                    [class.status-badge-active]="policy.status === 'ACTIVE'"
                    [class.status-badge-quoted]="policy.status === 'QUOTED'"
                    [class.status-badge-issued]="policy.status === 'ISSUED'"
                    [class.status-badge-cancelled]="policy.status === 'CANCELLED'"
                    [class.status-badge-suspended]="policy.status === 'SUSPENDED'"
                  >
                    {{ t('policies.status.' + policy.status) }}
                  </span>
                  <span class="font-mono text-xs font-semibold text-on-surface">
                    $ {{ policy.monthlyPremium.amount | number:'1.2-2' }}
                  </span>
                </div>
              </div>
            </div>
          </div>

          <!-- Bottom Action button to policies page -->
          <div class="mt-8 pt-6 border-t border-border-custom">
            <button routerLink="/app/policies" class="w-full h-12 border border-border-custom hover:bg-hover-custom rounded-xl font-medium transition-all text-body-sm flex items-center justify-center gap-2 cursor-pointer text-on-surface">
              {{ t('dashboard.viewAll') }}
              <span class="material-symbols-outlined text-sm">arrow_forward</span>
            </button>
          </div>
        </div>

      </div>

    </div>
  `,
  styles: [],
})
export class DashboardPage implements OnInit {
  protected readonly state = inject(StateService);
  private readonly customerService = inject(CustomerHttpService);
  private readonly policyService = inject(PolicyHttpService);

  ngOnInit(): void {
    this.customerService.getAll().subscribe();
    this.policyService.getAll().subscribe({
      next: (policies) => {
        // Generar historial de eventos simulados de Kafka basados en pólizas reales de Postgres
        if (this.state.transitions().length === 0 && policies.length > 0) {
          const mockTransitions = policies.slice(0, 3).map((policy, index) => {
            const minutesAgo = (index + 1) * 12 + 5;
            const timestamp = new Date(Date.now() - minutesAgo * 60 * 1000);
            
            let oldStatus = 'QUOTED';
            let newStatus = policy.status;
            
            if (policy.status === 'ACTIVE') {
              oldStatus = 'ISSUED';
            } else if (policy.status === 'ISSUED') {
              oldStatus = 'QUOTED';
            } else if (policy.status === 'SUSPENDED' || policy.status === 'CANCELLED') {
              oldStatus = 'ACTIVE';
            }

            return {
              policyNumber: policy.policyNumber,
              oldStatus: oldStatus,
              newStatus: newStatus,
              timestamp: timestamp,
              success: index !== 2 // Simular una fallida para variedad visual
            };
          });
          
          // Llenar el signal
          mockTransitions.forEach((t) => this.state.addTransition(t));
        }
      }
    });
  }

  getPolicyCount(branch: string): number {
    return this.state.policies().filter((p) => p.branch === branch).length;
  }

  getPolicyPercent(branch: string): number {
    const total = this.state.policies().length;
    if (total === 0) return 0;
    const count = this.getPolicyCount(branch);
    return Math.round((count / total) * 100);
  }
}
