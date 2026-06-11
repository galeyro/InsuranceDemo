export const PolicyStatus = {
  QUOTED: 'QUOTED' as const,
  ISSUED: 'ISSUED' as const,
  ACTIVE: 'ACTIVE' as const,
  SUSPENDED: 'SUSPENDED' as const,
  CANCELLED: 'CANCELLED' as const,
};

export type PolicyStatus = typeof PolicyStatus[keyof typeof PolicyStatus];

export const PolicyStatusLabels: Record<PolicyStatus, string> = {
  QUOTED: 'Cotizada',
  ISSUED: 'Emitida',
  ACTIVE: 'Activa',
  SUSPENDED: 'Suspendida',
  CANCELLED: 'Cancelada',
};

export const PolicyStatusColors: Record<PolicyStatus, string> = {
  QUOTED: 'amber',
  ISSUED: 'blue',
  ACTIVE: 'emerald',
  SUSPENDED: 'orange',
  CANCELLED: 'red',
};
