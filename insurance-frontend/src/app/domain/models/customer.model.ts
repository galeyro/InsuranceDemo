import { Email, CustomerId } from './value-objects.model';

export interface Customer {
  id: CustomerId;
  name: string;
  email: Email;
  createdAt: string;
  updatedAt: string;
  active: boolean;
}

export interface CreateCustomerDto {
  name: string;
  email: string;
}
