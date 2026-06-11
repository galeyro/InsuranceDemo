import { Injectable, signal } from '@angular/core';
import { Customer } from '../../domain/models/customer.model';
import { Policy } from '../../domain/models/policy.model';

export interface PolicyTransition {
  policyNumber: string;
  oldStatus: string;
  newStatus: string;
  timestamp: Date;
  success: boolean;
}

export interface AppNotification {
  id: string;
  title: string;
  message: string;
  type: 'customer_created' | 'policy_created' | 'policy_status_changed';
  timestamp: Date;
  read: boolean;
  payload?: {
    customerName?: string;
    policyNumber?: string;
    oldStatus?: string;
    newStatus?: string;
  };
}

@Injectable({ providedIn: 'root' })
export class StateService {
  readonly customers = signal<Customer[]>([]);
  readonly policies = signal<Policy[]>([]);
  readonly transitions = signal<PolicyTransition[]>([]);
  readonly notifications = signal<AppNotification[]>([]);

  addNotification(notification: Omit<AppNotification, 'id' | 'read' | 'timestamp'>): void {
    const newNotif: AppNotification = {
      ...notification,
      id: crypto.randomUUID ? crypto.randomUUID() : Math.random().toString(36).substring(2),
      read: false,
      timestamp: new Date()
    };
    this.notifications.update((list) => [newNotif, ...list]);
  }

  markAllAsRead(): void {
    this.notifications.update((list) => list.map((n) => ({ ...n, read: true })));
  }

  clearNotifications(): void {
    this.notifications.set([]);
  }

  addCustomer(customer: Customer): void {
    this.customers.update((list) => [...list, customer]);
    this.addNotification({
      title: 'Cliente Creado',
      message: `Se registró al cliente: ${customer.name}`,
      type: 'customer_created',
      payload: { customerName: customer.name }
    });
  }

  addPolicy(policy: Policy): void {
    this.policies.update((list) => [...list, policy]);
    // Evitamos notificaciones masivas en carga inicial de base de datos
    const isRecent = Math.abs(Date.now() - (policy.createdAt ? new Date(policy.createdAt).getTime() : Date.now())) < 5000;
    if (isRecent) {
      this.addNotification({
        title: 'Póliza Creada',
        message: `Nueva póliza cotizada: ${policy.policyNumber}`,
        type: 'policy_created',
        payload: { policyNumber: policy.policyNumber }
      });
    }
  }

  updatePolicy(updated: Policy): void {
    const oldPolicy = this.policies().find((p) => p.id.value === updated.id.value);
    this.policies.update((list) =>
      list.map((p) => (p.id.value === updated.id.value ? updated : p))
    );
    if (oldPolicy && oldPolicy.status !== updated.status) {
      this.addNotification({
        title: 'Estado de Póliza Actualizado',
        message: `Póliza ${updated.policyNumber} cambió de ${oldPolicy.status} a ${updated.status}`,
        type: 'policy_status_changed',
        payload: {
          policyNumber: updated.policyNumber,
          oldStatus: oldPolicy.status,
          newStatus: updated.status
        }
      });
    }
  }

  addTransition(transition: PolicyTransition): void {
    this.transitions.update((list) => [transition, ...list]);
  }

  getCustomerById(id: string): Customer | undefined {
    return this.customers().find((c) => c.id.value === id);
  }

  getPolicyById(id: string): Policy | undefined {
    return this.policies().find((p) => p.id.value === id);
  }

  getPoliciesByCustomer(customerId: string): Policy[] {
    return this.policies().filter((p) => p.customerId.value === customerId);
  }

  get activePoliciesCount(): number {
    return this.policies().filter((p) => p.status === 'ACTIVE').length;
  }

  get totalMonthlyPremium(): number {
    return this.policies().reduce((sum, p) => sum + (p.monthlyPremium?.amount ?? 0), 0);
  }
}
