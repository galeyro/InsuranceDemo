import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { StateService } from '../../../../core/state/state.service';
import { CustomerHttpService } from '../../data/customer-http.service';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslocoDirective],
  template: `
    <div class="space-y-4 md:space-y-6 animate-fade-in" *transloco="let t">
      <!-- Header Area -->
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 border-b border-border-custom pb-4 md:pb-6">
        <div class="space-y-1">
          <h2 class="font-outfit text-headline-lg font-bold text-on-surface flex items-center gap-3">
            {{ t('customers.title') }}
            <span class="text-xs bg-primary/10 text-primary border border-primary/20 px-2.5 py-0.5 rounded-full font-sans font-semibold">
              {{ state.customers().length }}
            </span>
          </h2>
          <p class="text-body-sm text-on-surface-variant">{{ t('customers.listSubtitle') }}</p>
        </div>
        <a
          routerLink="/app/customers/new"
          class="inline-flex items-center justify-center gap-2 px-6 py-3.5 rounded-xl bg-primary text-on-primary font-bold text-body-md shadow-[0_10px_20px_-8px_rgba(255,126,10,0.4)] transition-all hover:scale-105 hover:shadow-[0_10px_25px_-5px_rgba(255,126,10,0.5)] active:scale-95"
        >
          <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
          </svg>
          {{ t('customers.new') }}
        </a>
      </div>
      
      <!-- Empty State -->
      <div *ngIf="state.customers().length === 0" class="glass rounded-2xl p-12 text-center max-w-xl mx-auto space-y-6">
        <div class="w-20 h-20 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center mx-auto transform rotate-6 hover:rotate-0 transition-transform duration-300">
          <svg class="w-10 h-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
          </svg>
        </div>
        <div class="space-y-2">
          <h4 class="font-outfit text-headline-sm font-semibold text-on-surface">{{ t('customers.listEmptyTitle') }}</h4>
          <p class="text-body-md text-on-surface-variant max-w-md mx-auto">{{ t('common.noData') }} {{ t('customers.listEmptyDescription') }}</p>
        </div>
        <a
          routerLink="/app/customers/new"
          class="inline-flex px-8 py-3.5 rounded-xl bg-primary text-on-primary font-bold text-body-md transition-all hover:scale-105 active:scale-95"
        >
          {{ t('customers.new') }}
        </a>
      </div>
      
      <!-- Customers Grid/Table -->
      <div *ngIf="state.customers().length > 0" class="glass rounded-2xl overflow-hidden border border-border-custom shadow-sm">
        <div class="overflow-x-auto">
          <table class="w-full border-collapse">
            <thead>
              <tr class="bg-surface-container-high/40 border-b border-border-custom text-left">
                <th class="px-6 py-4 text-label-sm uppercase tracking-wider text-on-surface-variant font-bold">{{ t('customers.form.name') }}</th>
                <th class="hidden sm:table-cell px-6 py-4 text-label-sm uppercase tracking-wider text-on-surface-variant font-bold">{{ t('customers.form.email') }}</th>
                <th class="px-6 py-4 text-label-sm uppercase tracking-wider text-on-surface-variant font-bold">Estado</th>
                <th class="px-6 py-4 text-label-sm uppercase tracking-wider text-on-surface-variant font-bold text-right">{{ t('common.actions') }}</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-border-custom">
              <tr
                *ngFor="let customer of state.customers()"
                class="hover:bg-hover-custom transition-colors group"
              >
                <td class="px-3 py-3 md:px-6 md:py-5">
                  <div class="flex items-center gap-4">
                    <div class="w-10 h-10 rounded-xl bg-primary/10 flex items-center justify-center text-primary font-bold text-sm group-hover:scale-110 transition-transform">
                      {{ customer.name[0] }}
                    </div>
                    <div>
                      <span class="font-bold text-on-surface block group-hover:text-primary transition-colors">{{ customer.name }}</span>
                      <span class="text-xs text-on-surface-variant font-mono">ID: {{ customer.id.value.substring(0,8) }}...</span>
                    </div>
                  </div>
                </td>
                <td class="hidden sm:table-cell px-3 py-3 md:px-6 md:py-5 text-on-surface-variant font-medium">{{ customer.email.value }}</td>
                <td class="px-3 py-3 md:px-6 md:py-5">
                  <span
                    class="status-badge"
                    [class.status-badge-active]="customer.active"
                    [class.status-badge-cancelled]="!customer.active"
                  >
                    <span 
                      class="status-dot" 
                      [class.status-dot-active]="customer.active" 
                      [class.status-dot-cancelled]="!customer.active"
                    ></span>
                    {{ customer.active ? 'Activo' : 'Inactivo' }}
                  </span>
                </td>
                <td class="px-3 py-3 md:px-6 md:py-5 text-right">
                  <a
                    [routerLink]="['/app/customers', customer.id.value]"
                    class="inline-flex items-center justify-center gap-1 px-4 py-2 rounded-lg bg-surface-container hover:bg-primary hover:text-on-primary font-bold text-xs text-on-surface-variant transition-all duration-200"
                  >
                    <span>{{ t('common.view') }}</span>
                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [],
})
export class CustomerListPage implements OnInit {
  protected readonly state = inject(StateService);
  private readonly customerService = inject(CustomerHttpService);

  ngOnInit(): void {
    this.customerService.getAll().subscribe();
  }
}
