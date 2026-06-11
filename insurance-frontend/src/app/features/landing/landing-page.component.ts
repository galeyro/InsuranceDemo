import { Component, inject, HostListener, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslocoDirective } from '@jsverse/transloco';
import { ThemeService } from '../../core/theme/theme.service';
import { LangSwitcherComponent } from '../../core/i18n/lang-switcher.component';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule, TranslocoDirective, LangSwitcherComponent],
  template: `
    <div *transloco="let t">
      <!-- Top Navigation Bar -->
      <header
        [class.h-16]="isScrolled"
        [class.bg-surface-dim]="isScrolled"
        [class.bg-surface-dim/80]="!isScrolled"
        [class.h-20]="!isScrolled"
        class="backdrop-blur-xl border-b border-white/10 fixed top-0 left-0 right-0 z-50 transition-all duration-300"
      >
        <div class="flex justify-between items-center w-full px-gutter h-full max-w-container-max mx-auto">
          <!-- Logo -->
          <div class="flex items-center gap-3">
            <span class="font-space-grotesk text-headline-md text-primary tracking-tight font-semibold">Sofka Insurance</span>
            <span class="text-on-surface-variant text-label-sm px-2 py-0.5 border border-primary/20 rounded">PREMIUM</span>
          </div>


          <!-- Actions: Lang, Theme, Enter Platform -->
          <div class="flex items-center gap-4">
            <app-lang-switcher />
            <button
              class="p-2 rounded-full hover:bg-white/10 transition-colors text-on-surface-variant cursor-pointer flex items-center justify-center"
              (click)="themeService.toggle()"
            >
              <span class="material-symbols-outlined text-[20px]">
                {{ themeService.theme() === 'dark' ? 'light_mode' : 'dark_mode' }}
              </span>
            </button>
            <button
              (click)="enterPlatform()"
              class="bg-primary text-on-primary px-6 py-2 rounded-lg font-bold transition-all hover:brightness-110 active:scale-95 text-label-sm font-sans"
            >
              {{ t('landing.cta.enter') }}
            </button>
          </div>
        </div>
      </header>

      <main class="relative pt-20">
        <!-- Hero Section -->
        <section class="relative min-h-[90vh] flex items-center justify-center pt-16 pb-36 overflow-hidden">
          <div class="max-w-container-max mx-auto px-gutter relative z-10 text-center">
            <!-- Badge -->
            <div class="inline-flex items-center gap-2 bg-primary/10 border border-primary/20 px-4 py-1.5 rounded-full mb-8">
              <span class="material-symbols-outlined text-primary text-[16px]">auto_awesome</span>
              <span class="text-primary text-label-sm uppercase tracking-widest font-semibold">{{ t('landing.hero.badge') }}</span>
            </div>

            <!-- Title -->
            <h1
              class="font-space-grotesk text-display-lg-mobile md:text-display-lg mb-6 leading-tight max-w-4xl mx-auto font-bold"
              [innerHTML]="t('landing.hero.title')"
            ></h1>

            <!-- Description -->
            <p class="font-body-lg text-body-lg text-on-surface-variant max-w-2xl mx-auto mb-10 opacity-80">
              {{ t('landing.hero.description') }}
            </p>

            <!-- Buttons -->
            <div class="flex flex-col sm:flex-row items-center justify-center gap-6">
              <button
                (click)="enterPlatform()"
                class="bg-primary text-on-primary-container h-14 px-10 rounded-xl font-bold flex items-center justify-center gap-2 shadow-[0_0_30px_rgba(255,126,10,0.3)] transition-all hover:scale-105 active:scale-95 cursor-pointer text-on-primary"
              >
                {{ t('landing.hero.cta.primary') }}
                <span class="material-symbols-outlined">arrow_forward</span>
              </button>
              <button class="border border-white/15 hover:bg-white/5 h-14 px-10 rounded-xl font-medium transition-all cursor-pointer text-on-surface">
                {{ t('landing.hero.cta.secondary') }}
              </button>
            </div>
          </div>

          <!-- Dashboard Peek (Lower Hero) -->
          <div class="absolute -bottom-64 left-1/2 -translate-x-1/2 w-full max-w-5xl opacity-40 hover:opacity-100 transition-opacity duration-700 pointer-events-none md:pointer-events-auto z-10">
            <div class="glass-card rounded-t-3xl p-4 pb-0 overflow-hidden shadow-2xl transition-all duration-700 opacity-100 translate-y-0">
              <img alt="Sofka Insure Executive Dashboard Interface" class="w-full rounded-t-2xl border border-white/10" src="https://lh3.googleusercontent.com/aida-public/AB6AXuAR_doasf7seGnmN4K3mPt6IDnl-Jxm2nYPIFkFQgDXTqI1WvLIcHDVYXFYubQFGNNHLs8GHDS-UwbLJQOWucGGPoU-z6tFzesBehlumiQJ1nVoct1cDq5o9SWG1YMPUeeSFL6hOSE4Oe479R3AUbwxLLlz-hDD8aEiDfU9kRCjQotCuAEoFtQipQAqIcOmxcioNxdZXSVIbYLlAWX6He0UbwCzBoET4hDyKAL7tdNrHNZjj382kbTU_qNUNUQI8hIlbVpqdWEAXho">
            </div>
          </div>
        </section>

        <!-- Value Propositions -->
        <section class="py-32 bg-surface mt-32 relative">
          <div class="max-w-container-max mx-auto px-gutter">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
              <!-- Prop 1 -->
              <div class="glass-card p-8 rounded-2xl group hover:border-primary/50 transition-all duration-300">
                <div class="w-14 h-14 bg-primary/10 border border-primary/20 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
                  <span class="material-symbols-outlined text-primary text-3xl">speed</span>
                </div>
                <h3 class="font-space-grotesk text-headline-sm mb-4 font-semibold">{{ t('landing.features.quoting.title') }}</h3>
                <p class="font-body-md text-body-md text-on-surface-variant">{{ t('landing.features.quoting.description') }}</p>
              </div>

              <!-- Prop 2 -->
              <div class="glass-card p-8 rounded-2xl group hover:border-primary/50 transition-all duration-300">
                <div class="w-14 h-14 bg-primary/10 border border-primary/20 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
                  <span class="material-symbols-outlined text-primary text-3xl">account_tree</span>
                </div>
                <h3 class="font-space-grotesk text-headline-sm mb-4 font-semibold">{{ t('landing.features.lifecycle.title') }}</h3>
                <p class="font-body-md text-body-md text-on-surface-variant">{{ t('landing.features.lifecycle.description') }}</p>
              </div>

              <!-- Prop 3 -->
              <div class="glass-card p-8 rounded-2xl group hover:border-primary/50 transition-all duration-300">
                <div class="w-14 h-14 bg-primary/10 border border-primary/20 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
                  <span class="material-symbols-outlined text-primary text-3xl">psychology</span>
                </div>
                <h3 class="font-space-grotesk text-headline-sm mb-4 font-semibold">{{ t('landing.features.intelligence.title') }}</h3>
                <p class="font-body-md text-body-md text-on-surface-variant">{{ t('landing.features.intelligence.description') }}</p>
              </div>
            </div>
          </div>
        </section>

        <!-- Feature Showcase -->
        <section class="py-32 overflow-hidden">
          <div class="max-w-container-max mx-auto px-gutter">
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-20 items-center">
              <div>
                <h2 class="font-space-grotesk text-display-lg-mobile md:text-display-lg mb-8 font-bold leading-tight" [innerHTML]="t('landing.clarity.title')"></h2>
                <div class="space-y-8">
                  <!-- Bullet 1 -->
                  <div class="flex gap-4">
                    <div class="mt-1">
                      <span class="material-symbols-outlined text-primary">check_circle</span>
                    </div>
                    <div>
                      <h4 class="font-space-grotesk text-headline-sm mb-2 font-semibold">{{ t('landing.clarity.riskProfiling.title') }}</h4>
                      <p class="text-on-surface-variant font-body-md text-body-md">{{ t('landing.clarity.riskProfiling.description') }}</p>
                    </div>
                  </div>

                  <!-- Bullet 2 -->
                  <div class="flex gap-4">
                    <div class="mt-1">
                      <span class="material-symbols-outlined text-primary">check_circle</span>
                    </div>
                    <div>
                      <h4 class="font-space-grotesk text-headline-sm mb-2 font-semibold">{{ t('landing.clarity.compliance.title') }}</h4>
                      <p class="text-on-surface-variant font-body-md text-body-md">{{ t('landing.clarity.compliance.description') }}</p>
                    </div>
                  </div>

                  <!-- Bullet 3 -->
                  <div class="flex gap-4">
                    <div class="mt-1">
                      <span class="material-symbols-outlined text-primary">check_circle</span>
                    </div>
                    <div>
                      <h4 class="font-space-grotesk text-headline-sm mb-2 font-semibold">{{ t('landing.clarity.omnichannel.title') }}</h4>
                      <p class="text-on-surface-variant font-body-md text-body-md">{{ t('landing.clarity.omnichannel.description') }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Stepper Card -->
              <div class="relative group">
                <!-- Background Glow -->
                <div class="absolute -inset-10 bg-primary/20 rounded-full blur-[100px] opacity-20 group-hover:opacity-40 transition-opacity"></div>
                <!-- Visual Peek -->
                <div class="relative glass-card p-6 rounded-[2rem] border-white/20 transform hover:-rotate-2 transition-all duration-500">
                  <div class="bg-background rounded-xl overflow-hidden shadow-2xl">
                    <img alt="Sofka Insure Visual Stepper Analytics" class="w-full h-auto" src="https://lh3.googleusercontent.com/aida-public/AB6AXuBLhxWqsufjR4tY6LcPSjFsLj7wt2B23wD7vpq9gOEEOxkbtRTWC8FfN4gIz1q09h9hklp-8FPh-6iSJN4Xwxny1z1pSAac0Pkmp3tCVaTYm55JNUp571s-AHBtTgGnqqFdI1_3iSd1UHv3y6NmvmQmYzb8AW11AwlC3TNSs73mQRjJ4tvb_NVXFVsKFIx-viUt5EsiCvHHgZ2OoeuLm3kpiZtQpYnlh55pHD-UySHXg5-DXFs_B_3COXMi1veqe2SMIn1yLrYxJYg">
                    <div class="p-6 border-t border-white/10 flex justify-between items-center bg-surface-container-low/20">
                      <div>
                        <p class="text-on-surface-variant text-label-sm">{{ t('landing.portfolio.title') }}</p>
                        <p class="text-primary font-space-grotesk text-headline-md font-semibold">$142.8M</p>
                      </div>
                      <div class="flex -space-x-3">
                        <div class="w-10 h-10 rounded-full border-2 border-background bg-secondary flex items-center justify-center text-xs text-on-secondary font-bold">JD</div>
                        <div class="w-10 h-10 rounded-full border-2 border-background bg-primary flex items-center justify-center text-xs text-on-primary font-bold">SA</div>
                        <div class="w-10 h-10 rounded-full border-2 border-background bg-surface-container-highest flex items-center justify-center text-xs text-white">+4</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Direct Access Section -->
        <section class="py-32 relative bg-surface-container-lowest overflow-hidden">
          <!-- Asymmetric Glow -->
          <div class="absolute top-0 right-0 w-1/3 h-full bg-primary/5 blur-[120px] rounded-full"></div>
          <div class="max-w-container-max mx-auto px-gutter text-center relative z-10">
            <div class="max-w-3xl mx-auto border border-white/15 p-12 md:p-20 rounded-[3rem] surface-gradient glass-card transition-all duration-700 opacity-100 translate-y-0">
              <h2 class="font-space-grotesk text-display-lg-mobile md:text-display-lg mb-6 font-bold leading-tight">{{ t('landing.ctaSection.title') }}</h2>
              <p class="font-body-lg text-body-lg text-on-surface-variant opacity-80 mb-10">{{ t('landing.ctaSection.description') }}</p>
              <button
                (click)="enterPlatform()"
                class="bg-primary text-on-primary h-16 px-12 rounded-2xl font-bold text-lg transition-all hover:scale-105 active:scale-95 shadow-[0_20px_40px_rgba(255,126,10,0.2)] cursor-pointer text-on-primary"
              >
                {{ t('landing.ctaSection.button') }}
              </button>
              <div class="mt-8 flex items-center justify-center gap-6 text-on-surface-variant text-label-sm font-semibold">
                <span class="flex items-center gap-1">
                  <span class="material-symbols-outlined text-[14px]">lock_open</span>
                  {{ t('landing.ctaSection.noLogin') }}
                </span>
                <span class="w-1 h-1 rounded-full bg-white/30"></span>
                <span class="flex items-center gap-1">
                  <span class="material-symbols-outlined text-[14px]">verified_user</span>
                  {{ t('landing.ctaSection.certified') }}
                </span>
              </div>
            </div>
          </div>
        </section>

        <!-- Footer -->
        <footer class="py-16 border-t border-white/10">
          <div class="max-w-container-max mx-auto px-gutter flex flex-col md:flex-row justify-between items-center gap-8">
            <div class="flex flex-col items-center md:items-start gap-2">
              <span class="font-space-grotesk text-headline-sm text-primary tracking-tight font-semibold">Sofka Insurance</span>
              <p class="text-on-surface-variant text-label-sm">{{ t('landing.footer.copyright') }}</p>
            </div>
            <div class="flex gap-8">
              <a class="text-on-surface-variant hover:text-primary transition-colors text-label-sm font-semibold" href="#">{{ t('landing.footer.privacy') }}</a>
              <a class="text-on-surface-variant hover:text-primary transition-colors text-label-sm font-semibold" href="#">{{ t('landing.footer.terms') }}</a>
              <a class="text-on-surface-variant hover:text-primary transition-colors text-label-sm font-semibold" href="#">{{ t('landing.footer.security') }}</a>
            </div>
            <div class="flex gap-4">
              <button class="w-10 h-10 rounded-full border border-white/10 flex items-center justify-center text-on-surface-variant hover:border-primary hover:text-primary transition-all">
                <span class="material-symbols-outlined text-[20px]">terminal</span>
              </button>
              <button class="w-10 h-10 rounded-full border border-white/10 flex items-center justify-center text-on-surface-variant hover:border-primary hover:text-primary transition-all">
                <span class="material-symbols-outlined text-[20px]">hub</span>
              </button>
            </div>
          </div>
        </footer>
      </main>
    </div>
  `,
  styles: [],
})
export class LandingPageComponent implements AfterViewInit {
  protected readonly themeService = inject(ThemeService);
  private readonly router = inject(Router);
  protected isScrolled = false;

  @HostListener('window:scroll', [])
  onWindowScroll() {
    if (typeof window !== 'undefined') {
      this.isScrolled = window.scrollY > 20;
    }
  }

  ngAfterViewInit() {
    if (typeof window !== 'undefined' && 'IntersectionObserver' in window) {
      const observer = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              entry.target.classList.add('opacity-100', 'translate-y-0');
              entry.target.classList.remove('opacity-0', 'translate-y-10');
              observer.unobserve(entry.target); // Animates once
            }
          });
        },
        { threshold: 0.1 }
      );

      const cards = document.querySelectorAll('.glass-card');
      cards.forEach((card) => {
        card.classList.add('transition-all', 'duration-700', 'opacity-0', 'translate-y-10');
        observer.observe(card);
      });
    }
  }

  enterPlatform(): void {
    this.router.navigate(['/app/dashboard']);
  }
}
