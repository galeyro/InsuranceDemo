export const Branch = {
  AUTO: 'AUTO' as const,
  LIFE: 'LIFE' as const,
  HOME: 'HOME' as const,
  HEALTH: 'HEALTH' as const,
};

export type Branch = typeof Branch[keyof typeof Branch];

export const BranchLabels: Record<Branch, string> = {
  AUTO: 'Automóviles',
  LIFE: 'Vida',
  HOME: 'Hogar',
  HEALTH: 'Salud',
};
