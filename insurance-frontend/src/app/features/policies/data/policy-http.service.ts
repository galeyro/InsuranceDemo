import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, tap } from 'rxjs';
import { Policy, CreatePolicyDto, UpdatePolicyStatusDto } from '../../../domain/models/policy.model';
import { StateService } from '../../../core/state/state.service';
import { NotificationService } from '../../../core/notification/notification.service';

@Injectable({ providedIn: 'root' })
export class PolicyHttpService {
  private readonly http = inject(HttpClient);
  private readonly state = inject(StateService);
  private readonly notification = inject(NotificationService);
  private readonly apiUrl = '/api/policies';

  create(policy: CreatePolicyDto): Observable<Policy> {
    return this.http.post<Policy>(this.apiUrl, policy).pipe(
      tap((created) => {
        this.state.addPolicy(created);
        this.notification.success(`Póliza ${created.policyNumber} creada exitosamente`);
      }),
      catchError((err) => {
        this.notification.error(err.message || 'Error al crear póliza');
        throw err;
      })
    );
  }

  getById(id: string): Observable<Policy> {
    return this.http.get<Policy>(`${this.apiUrl}/${id}`).pipe(
      tap((policy) => this.state.addPolicy(policy)),
      catchError((err) => {
        this.notification.error(err.message || 'Póliza no encontrada');
        throw err;
      })
    );
  }

  getByCustomer(customerId: string): Observable<Policy[]> {
    return this.http.get<Policy[]>(`${this.apiUrl}/customer/${customerId}`).pipe(
      tap((policies) => {
        policies.forEach((p) => this.state.addPolicy(p));
      }),
      catchError((err) => {
        this.notification.error(err.message || 'Error al cargar pólizas');
        throw err;
      })
    );
  }

  updateStatus(id: string, status: UpdatePolicyStatusDto): Observable<Policy> {
    return this.http.patch<Policy>(`${this.apiUrl}/${id}/status`, status).pipe(
      tap((updated) => {
        this.state.updatePolicy(updated);
        this.notification.success(`Estado actualizado a ${updated.status}`);
      }),
      catchError((err) => {
        this.notification.error(err.message || 'Transición no permitida');
        throw err;
      })
    );
  }
}
