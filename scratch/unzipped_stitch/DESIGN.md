---
name: Sofka Insure Premium
colors:
  surface: '#131313'
  surface-dim: '#131313'
  surface-bright: '#3a3939'
  surface-container-lowest: '#0e0e0e'
  surface-container-low: '#1c1b1b'
  surface-container: '#201f1f'
  surface-container-high: '#2a2a2a'
  surface-container-highest: '#353534'
  on-surface: '#e5e2e1'
  on-surface-variant: '#dfc0af'
  inverse-surface: '#e5e2e1'
  inverse-on-surface: '#313030'
  outline: '#a68b7c'
  outline-variant: '#584235'
  surface-tint: '#ffb689'
  primary: '#ffb689'
  on-primary: '#512300'
  primary-container: '#ff7e0a'
  on-primary-container: '#5e2a00'
  inverse-primary: '#984800'
  secondary: '#dac2b4'
  on-secondary: '#3c2d24'
  secondary-container: '#57463b'
  on-secondary-container: '#cbb4a6'
  tertiary: '#8fcdff'
  on-tertiary: '#003450'
  tertiary-container: '#00abfb'
  on-tertiary-container: '#003c5c'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffdbc8'
  primary-fixed-dim: '#ffb689'
  on-primary-fixed: '#311300'
  on-primary-fixed-variant: '#743500'
  secondary-fixed: '#f7decf'
  secondary-fixed-dim: '#dac2b4'
  on-secondary-fixed: '#261910'
  on-secondary-fixed-variant: '#544339'
  tertiary-fixed: '#cbe6ff'
  tertiary-fixed-dim: '#8fcdff'
  on-tertiary-fixed: '#001e30'
  on-tertiary-fixed-variant: '#004b71'
  background: '#131313'
  on-background: '#e5e2e1'
  surface-variant: '#353534'
typography:
  display-lg:
    fontFamily: Space Grotesk
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Space Grotesk
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Space Grotesk
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  container-max: 1280px
  gutter: 24px
  margin-desktop: 64px
  margin-mobile: 20px
---

## Brand & Style
The design system for Sofka Insure is built upon a foundation of high-performance precision and premium reliability. It targets a sophisticated audience that expects transparency, speed, and modern technical capability. 

The aesthetic is **Modern Corporate with Glassmorphic accents**, utilizing a high-contrast dark mode to create a sense of focus and depth. The design language emphasizes "Tech-Forward Protection," using the contrast between the deep Carbon Black backgrounds and the vibrant Sofka Orange to guide user action. Layouts should feel expansive and organized, utilizing translucent layers to maintain a sense of lightness despite the dark color palette.

## Colors
The palette is dominated by **Carbon Black (#0d0d0d)** to provide a premium, low-glare environment. **Sofka Orange (#FF7E0A)** is used exclusively for primary actions and brand emphasis.

Surface areas utilize a sophisticated **three-stop linear gradient** to simulate subtle light hitting a technical surface. Status colors are highly saturated to ensure immediate recognition against the dark backdrop. Border treatments must remain subtle at **15% white opacity** to define structure without creating visual noise.

## Typography
This design system uses a dual-type approach to balance personality and utility. 

**Space Grotesk** is used for all headings and display elements. Its geometric and slightly technical character reinforces the "Sofka Technologies" lineage. **Inter** is the workhorse for all functional content, forms, and data tables, ensuring maximum legibility and a neutral, professional tone. 

For data-heavy insurance tables, use `body-sm`. For field labels, always use `label-md` with its uppercase styling to differentiate from input values.

## Layout & Spacing
The layout follows an **8px grid system**. This design system relies on a **12-column fluid grid** for desktop, collapsing to a **4-column grid** for mobile devices.

Horizontal rhythm is maintained by a 24px gutter. For insurance dashboards, prioritize a "Center-Focused" layout with a max-width of 1280px to prevent data from stretching too far on ultra-wide monitors. Use generous vertical padding (64px+) between major sections to emphasize the premium, "airy" feel of the brand.

## Elevation & Depth
Depth is created through **Tonal Layering and Translucency** rather than heavy shadows.

- **Level 0 (Background):** Pure Carbon Black (#0d0d0d).
- **Level 1 (Cards/Sections):** The signature Surface Gradient with a 1px white (15% opacity) border.
- **Level 2 (Modals/Popovers):** Surface Gradient with an added 20px Backdrop Blur and a subtle outer glow using the primary orange at 5% opacity.

Avoid drop shadows on flat surfaces; instead, use the 1px border to "cut" the element out of the background.

## Shapes
The shape language is controlled and precise. A base **8px (0.5rem) radius** is applied to buttons, input fields, and standard cards to provide a modern, approachable feel that remains professional. 

Larger containers or sections may use `rounded-xl` (24px) to create a distinct "floating" appearance when layered over the Carbon Black background. Iconography should follow a linear, 2px stroke weight with slight rounding to match the UI components.

## Components
- **Buttons:** Primary buttons use a solid Sofka Orange fill with black text. Hover state shifts to `#e06c00`. Secondary buttons use the 1px white border with transparent backgrounds.
- **Cards:** Apply the `surface_gradient` and `border_color`. For interactive cards, the border opacity should increase to 30% on hover.
- **Input Fields:** Backgrounds should be a solid `#1a1a1a` (slightly lighter than the background) with an 8px radius. The bottom border or focus ring should use Sofka Orange.
- **Status Chips:** Small, pill-shaped indicators with a 10% opacity background of the status color and 100% opacity text for contrast (e.g., Active uses Emerald text on a dark green tint).
- **Lists/Tables:** Use "Ghost Rows" where the background is transparent, separated only by thin 10% white lines. On hover, the row should take on a 5% white overlay.
- **Insurance Specifics:** Data visualizations (charts/graphs) should utilize the status colors for consistency across the policy lifecycle.