import { PolicyId, CustomerId, Money, RiskProfile, Coverage } from './value-objects.model';

export type Branch = 'AUTO' | 'LIFE' | 'HOME' | 'HEALTH';
export type RatingStrategy = 'STANDARD' | 'RISK_BASED' | 'LOYALTY';
export type PolicyStatus = 'QUOTED' | 'ISSUED' | 'ACTIVE' | 'SUSPENDED' | 'CANCELLED';

export interface Policy {
  id: PolicyId;
  policyNumber: string;
  customerId: CustomerId;
  branch: Branch;
  ratingStrategy: RatingStrategy;
  coverage: Coverage;
  monthlyPremium: Money;
  riskProfile: RiskProfile;
  createdAt: string;
  updatedAt: string;
  status: PolicyStatus;
}

export interface CreatePolicyDto {
  customerId: string;
  branch: Branch;
  ratingStrategy: RatingStrategy;
  riskProfile: RiskProfile;
}

export interface UpdatePolicyStatusDto {
  targetStatus: PolicyStatus;
}
