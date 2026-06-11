export interface ValueObject<T> {
  value: T;
}

export interface CustomerId extends ValueObject<string> {}
export interface PolicyId extends ValueObject<string> {}
export interface Email extends ValueObject<string> {}

export interface Money {
  amount: number;
  currency: 'USD' | 'COP';
}

export interface RiskProfile {
  riskScore: number;
  customerSinceYear: number;
}

export interface Coverage {
  coverageAmount: Money;
  termMonths: number;
  attributes: Record<string, unknown>;
}
