import { PolicyStatus } from '../enums/policy-status.enum';

const transitions: Record<PolicyStatus, PolicyStatus[]> = {
  QUOTED: ['ISSUED', 'CANCELLED'],
  ISSUED: ['ACTIVE', 'CANCELLED'],
  ACTIVE: ['SUSPENDED', 'CANCELLED'],
  SUSPENDED: ['ACTIVE', 'CANCELLED'],
  CANCELLED: [],
};

export function canTransition(from: PolicyStatus, to: PolicyStatus): boolean {
  return transitions[from]?.includes(to) ?? false;
}

export function getAllowedTransitions(from: PolicyStatus): PolicyStatus[] {
  return transitions[from] ?? [];
}

export function isTerminal(status: PolicyStatus): boolean {
  return status === 'CANCELLED';
}
