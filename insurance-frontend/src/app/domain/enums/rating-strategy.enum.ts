export const RatingStrategy = {
  STANDARD: 'STANDARD' as const,
  RISK_BASED: 'RISK_BASED' as const,
  LOYALTY: 'LOYALTY' as const,
};

export type RatingStrategy = typeof RatingStrategy[keyof typeof RatingStrategy];

export const RatingStrategyLabels: Record<RatingStrategy, string> = {
  STANDARD: 'Tarifa estándar',
  RISK_BASED: 'Basado en riesgo',
  LOYALTY: 'Fidelidad',
};
