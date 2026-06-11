import { Component, inject, computed } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslocoDirective, TranslocoService } from '@jsverse/transloco';
import { ThemeService } from '../theme/theme.service';
import { LangSwitcherComponent } from '../i18n/lang-switcher.component';
import { StateService } from '../state/state.service';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    LangSwitcherComponent,
    TranslocoDirective,
  ],
  template: `
    <div class="min-h-screen bg-background text-on-surface" *transloco="let t">
      
      <!-- Mobile Sidebar Backdrop Overlay -->
      <div
        *ngIf="isMobileMenuOpen"
        class="fixed inset-0 z-40 bg-black/60 backdrop-blur-sm lg:hidden transition-opacity duration-300"
        (click)="toggleMobileMenu()"
      ></div>

      <!-- Left Sidebar Shell -->
      <aside
        [class.translate-x-0]="isMobileMenuOpen"
        [class.-translate-x-full]="!isMobileMenuOpen"
        class="fixed inset-y-0 left-0 z-50 w-64 lg:translate-x-0 lg:flex lg:flex-col py-8 px-4 transition-all duration-300 app-sidebar"
      >
        <!-- Brand Header -->
        <a
          routerLink="/"
          class="block px-6 mb-10 group cursor-pointer active:scale-95 transition-transform"
          title="Volver al inicio"
        >
          <h1 class="font-space-grotesk text-headline-md text-primary tracking-tight font-bold group-hover:opacity-80 transition-opacity">Sofka Insure</h1>
          <p class="font-body-sm text-on-surface-variant opacity-75 text-xs">{{ t('landing.subtitle') }} Protection</p>
        </a>

        <!-- Sidebar Navigation Menu -->
        <nav class="flex-1 space-y-2">
          <!-- Dashboard -->
          <a
            routerLink="/app/dashboard"
            routerLinkActive="bg-secondary-container/20 text-primary border-r-4 border-primary"
            class="flex items-center gap-3 px-6 py-3.5 rounded-lg text-body-md font-medium text-on-surface-variant hover:bg-white/5 transition-all cursor-pointer active:scale-[0.98] group"
            [class.text-on-surface-variant]="!isActive('/app/dashboard')"
            (click)="closeMobileMenu()"
          >
            <span class="material-symbols-outlined transition-transform group-hover:translate-x-0.5">dashboard</span>
            {{ t('nav.dashboard') }}
          </a>

          <!-- Customers -->
          <a
            routerLink="/app/customers"
            routerLinkActive="bg-secondary-container/20 text-primary border-r-4 border-primary"
            class="flex items-center gap-3 px-6 py-3.5 rounded-lg text-body-md font-medium text-on-surface-variant hover:bg-white/5 transition-all cursor-pointer active:scale-[0.98] group"
            [class.text-on-surface-variant]="!isActive('/app/customers')"
            (click)="closeMobileMenu()"
          >
            <span class="material-symbols-outlined transition-transform group-hover:translate-x-0.5">group</span>
            {{ t('nav.customers') }}
          </a>

          <!-- Policies -->
          <a
            routerLink="/app/policies"
            routerLinkActive="bg-secondary-container/20 text-primary border-r-4 border-primary"
            class="flex items-center gap-3 px-6 py-3.5 rounded-lg text-body-md font-medium text-on-surface-variant hover:bg-white/5 transition-all cursor-pointer active:scale-[0.98] group"
            [class.text-on-surface-variant]="!isActive('/app/policies')"
            (click)="closeMobileMenu()"
          >
            <span class="material-symbols-outlined transition-transform group-hover:translate-x-0.5">description</span>
            {{ t('nav.policies') }}
          </a>
        </nav>

        <!-- Bottom Actions -->
        <div class="border-t border-white/10 dark:border-white/10 pt-6 space-y-3">
          <button
            routerLink="/app/policies/new"
            (click)="closeMobileMenu()"
            class="w-full bg-primary text-on-primary font-bold py-3.5 rounded-xl hover:brightness-110 active:scale-95 transition-all cursor-pointer shadow-lg shadow-primary/10"
          >
            {{ t('policies.new') }}
          </button>
          
          <a
            (click)="logout()"
            class="flex items-center gap-3 px-6 py-3 rounded-lg text-body-sm font-medium text-on-surface-variant hover:bg-white/5 transition-all cursor-pointer group"
          >
            <span class="material-symbols-outlined transition-transform group-hover:translate-x-0.5">logout</span>
            {{ t('nav.logout') }}
          </a>
        </div>
      </aside>

      <!-- Main Contents Area -->
      <div class="lg:pl-64 flex flex-col min-h-screen">
        <!-- Top Navigation Bar -->
        <header class="bg-surface/80 backdrop-blur-xl border-b border-outline/10 dark:border-white/10 sticky top-0 z-40 h-20">
          <div class="flex justify-between items-center w-full px-gutter h-full max-w-container-max mx-auto">
            <!-- Left Side: Title and Mobile Toggle -->
            <div class="flex items-center gap-4">
              <button
                class="lg:hidden p-2 rounded-full hover:bg-white/5 text-on-surface-variant flex items-center justify-center cursor-pointer"
                (click)="toggleMobileMenu()"
              >
                <span class="material-symbols-outlined">menu</span>
              </button>
              <h2 class="font-space-grotesk text-headline-sm text-primary font-semibold">
                {{ t(getPageTitle()) }}
              </h2>
            </div>

            <!-- Right Side Actions -->
            <div class="flex items-center gap-4 md:gap-6">
              <!-- Language Switcher -->
              <app-lang-switcher />

              <!-- Theme Toggle -->
              <button
                class="p-2 rounded-full hover:bg-white/10 transition-colors text-on-surface-variant cursor-pointer flex items-center justify-center"
                (click)="themeService.toggle()"
                title="Cambiar tema"
              >
                <span class="material-symbols-outlined text-[20px]">
                  {{ themeService.theme() === 'dark' ? 'light_mode' : 'dark_mode' }}
                </span>
              </button>

              <!-- Notifications Badge & Dropdown -->
              <div class="relative flex items-center justify-center">
                <button
                  (click)="toggleNotifDropdown()"
                  class="p-2 rounded-full hover:bg-white/5 text-on-surface-variant hover:text-primary transition-all cursor-pointer flex items-center justify-center relative active:scale-95"
                  title="Notificaciones"
                >
                  <span class="material-symbols-outlined text-[22px]">notifications</span>
                  <span *ngIf="unreadCount() > 0" class="absolute top-1.5 right-1.5 w-2.5 h-2.5 bg-primary rounded-full animate-pulse"></span>
                </button>

                <!-- Dropdown Menu -->
                <div
                  *ngIf="isNotifDropdownOpen"
                  class="absolute right-0 top-12 w-80 bg-surface border border-outline/10 dark:border-white/10 rounded-2xl shadow-xl z-50 p-4 space-y-3 max-h-96 overflow-y-auto animate-fade-in glass-card text-left"
                >
                  <div class="flex justify-between items-center pb-2 border-b border-outline-variant/20">
                    <div class="flex items-center gap-2">
                      <h3 class="font-space-grotesk text-title-sm font-bold text-on-surface">{{ t('notifications.title') }}</h3>
                      <span class="text-[9px] bg-primary/10 text-primary px-2 py-0.5 rounded-full font-bold tracking-wider uppercase">{{ t('notifications.badge') }}</span>
                    </div>
                    <button
                      *ngIf="state.notifications().length > 0"
                      (click)="clearNotifications()"
                      class="text-[11px] text-primary hover:text-primary-hover font-semibold cursor-pointer transition-colors px-2 py-1 rounded hover:bg-primary/5 active:scale-95 flex items-center gap-1"
                    >
                      <span class="material-symbols-outlined text-sm">clear_all</span>
                      {{ t('notifications.clear') }}
                    </button>
                  </div>

                  <!-- Empty state -->
                  <div *ngIf="state.notifications().length === 0" class="py-6 text-center text-on-surface-variant text-xs space-y-2">
                    <span class="material-symbols-outlined text-3xl opacity-50 block mx-auto">notifications_off</span>
                    <p>{{ t('notifications.empty') }}</p>
                  </div>

                  <!-- Notifications List -->
                  <div class="space-y-3 divide-y divide-outline-variant/10">
                    <div
                      *ngFor="let notif of state.notifications()"
                      class="pt-3 first:pt-0 flex gap-3 text-left group"
                    >
                      <!-- Icon based on type -->
                      <div
                        class="w-8 h-8 rounded-lg flex items-center justify-center flex-shrink-0"
                        [class.bg-emerald-500/10]="notif.type === 'customer_created'"
                        [class.text-emerald-400]="notif.type === 'customer_created'"
                        [class.bg-primary/10]="notif.type === 'policy_created'"
                        [class.text-primary]="notif.type === 'policy_created'"
                        [class.bg-amber-500/10]="notif.type === 'policy_status_changed'"
                        [class.text-amber-400]="notif.type === 'policy_status_changed'"
                      >
                        <span class="material-symbols-outlined text-lg">
                          {{ notif.type === 'customer_created' ? 'person_add' : (notif.type === 'policy_created' ? 'description' : 'sync_alt') }}
                        </span>
                      </div>
                      
                      <div class="space-y-0.5 flex-1 min-w-0">
                        <div class="flex justify-between items-start gap-2">
                          <p class="font-semibold text-xs text-on-surface truncate group-hover:text-primary transition-colors">
                            {{ t('notifications.events.' + notif.type + '.title') }}
                          </p>
                          <span class="text-[9px] text-on-surface-variant font-mono whitespace-nowrap">
                            {{ notif.timestamp | date:'HH:mm' }}
                          </span>
                        </div>
                        <p class="text-[10px] text-on-surface-variant leading-relaxed break-words">
                          <ng-container *ngIf="notif.type === 'customer_created'">
                            {{ t('notifications.events.customer_created.message', { name: notif.payload?.customerName }) }}
                          </ng-container>
                          <ng-container *ngIf="notif.type === 'policy_created'">
                            {{ t('notifications.events.policy_created.message', { policyNumber: notif.payload?.policyNumber }) }}
                          </ng-container>
                          <ng-container *ngIf="notif.type === 'policy_status_changed'">
                            {{ t('notifications.events.policy_status_changed.message', { 
                                 policyNumber: notif.payload?.policyNumber, 
                                 oldStatus: t('policies.status.' + notif.payload?.oldStatus), 
                                 newStatus: t('policies.status.' + notif.payload?.newStatus) 
                               }) 
                            }}
                          </ng-container>
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Divider -->
              <span class="h-6 w-px bg-outline/20 dark:bg-white/10"></span>

              <!-- User Avatar Profile -->
              <div class="flex items-center gap-3 cursor-pointer active:scale-95 transition-transform">
                <img
                  alt="User Avatar Profile"
                  class="w-10 h-10 rounded-full border border-primary/20 object-cover"
                  src="https://lh3.googleusercontent.com/aida-public/AB6AXuBQ8nXySh2Q_hlm5WlPoji_gWpdODZR5t9T1-iGJ7NJ2aoqdCUt5-2ZRri9G3QfzWXFmEfmp4NmFAVGLWSxCo-_Me35o6XxuQO2oJpqkv7vceWrLHxdJqGj6QJMMMeWfGXqOafZsaECeQX9jdjNAGDZxCwUAk_gtz4TI_ke0D1kxSXAoYnJx567taLPK7qtAOPkSaxEeo01fWnLAs9RqGc5ssrJON12BO6G8ZQCKNrdcOsrMWjn_E3f0LHG2Rs22Eub2Y6F1oFiVG0"
                />
              </div>
            </div>
          </div>
        </header>

        <!-- Main Routing Container -->
        <main class="flex-1 p-6 md:p-10 max-w-container-max w-full mx-auto">
          <router-outlet />
        </main>
      </div>

    </div>
  `,
  styles: [],
})
export class ShellComponent {
  protected readonly themeService = inject(ThemeService);
  private readonly router = inject(Router);
  protected readonly state = inject(StateService);

  protected isMobileMenuOpen = false;
  protected isNotifDropdownOpen = false;

  protected readonly unreadCount = computed(() => 
    this.state.notifications().filter((n) => !n.read).length
  );

  toggleNotifDropdown(): void {
    this.isNotifDropdownOpen = !this.isNotifDropdownOpen;
    if (this.isNotifDropdownOpen) {
      this.state.markAllAsRead();
    }
  }

  clearNotifications(): void {
    this.state.clearNotifications();
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
  }

  isActive(route: string): boolean {
    return window.location.pathname.startsWith(route);
  }

  getPageTitle(): string {
    const path = window.location.pathname;
    if (path.includes('dashboard')) return 'nav.dashboard';
    if (path.includes('customers')) return 'customers.title';
    if (path.includes('policies')) return 'policies.title';
    return '';
  }

  logout(): void {
    this.closeMobileMenu();
    this.router.navigate(['/']);
  }
}
