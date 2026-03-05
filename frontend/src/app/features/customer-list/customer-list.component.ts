import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../core/services/customer.service';
import { Customer } from '../../core/models/customer.model';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss']
})
export class CustomerListComponent implements OnInit {

  customers:       Customer[] = [];
  selectedCustomer: Customer | null = null;
  isLoading    = false;
  errorMessage = '';
  searchId:    number | null = null;
  searchError  = '';

  constructor(private readonly customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.isLoading    = true;
    this.errorMessage = '';

    this.customerService.getAllCustomers().subscribe({
      next: (customers) => {
        this.customers = customers;
        this.isLoading = false;
      },
      error: (err: Error) => {
        this.errorMessage = err.message;
        this.isLoading    = false;
      }
    });
  }

  viewCustomer(id: number): void {
    this.searchError      = '';
    this.selectedCustomer = null;

    this.customerService.getCustomerById(id).subscribe({
      next: (customer) => {
        this.selectedCustomer = customer;
      },
      error: (err: Error) => {
        this.searchError = err.message;
      }
    });
  }

  findById(): void {
    if (!this.searchId) return;
    this.viewCustomer(this.searchId);
  }

  clearSelection(): void {
    this.selectedCustomer = null;
  }

  clearSearch(): void {
    this.searchId         = null;
    this.searchError      = '';
    this.selectedCustomer = null;
  }
}
