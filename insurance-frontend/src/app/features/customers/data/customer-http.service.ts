import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, tap } from 'rxjs';
import { Customer, CreateCustomerDto } from '../../../domain/models/customer.model';
import { StateService } from '../../../core/state/state.service';
import { NotificationService } from '../../../core/notification/notification.service';

@Injectable({ providedIn: 'root' })
export class CustomerHttpService {
  private readonly http = inject(HttpClient);
  private readonly state = inject(StateService);
  private readonly notification = inject(NotificationService);
  private readonly apiUrl = '/api/customers';

  create(customer: CreateCustomerDto): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer).pipe(
      tap((created) => {
        this.state.addCustomer(created);
        this.notification.success(`Cliente ${created.name} creado exitosamente`);
      }),
      catchError((err) => {
        this.notification.error(err.message || 'Error al crear cliente');
        throw err;
      })
    );
  }

  getById(id: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`).pipe(
      tap((customer) => this.state.addCustomer(customer)),
      catchError((err) => {
        this.notification.error(err.message || 'Cliente no encontrado');
        throw err;
      })
    );
  }
}
