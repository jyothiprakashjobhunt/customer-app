import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../../core/services/customer.service';
import { CustomerRequest } from '../../core/models/customer.model';

@Component({
  selector: 'app-customer-form',
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.scss']
})
export class CustomerFormComponent implements OnInit {

  @Output() customerCreated = new EventEmitter<void>();

  customerForm!: FormGroup;
  isLoading    = false;
  errorMessage = '';
  successMessage = '';

  // yyyy-MM-dd in local time — avoids UTC offset shifting the date to yesterday
  readonly maxDate = new Date().toLocaleDateString('en-CA');

  constructor(
    private readonly fb: FormBuilder,
    private readonly customerService: CustomerService
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  get firstName()   { return this.customerForm.get('firstName')!;   }
  get lastName()    { return this.customerForm.get('lastName')!;     }
  get dateOfBirth() { return this.customerForm.get('dateOfBirth')!;  }

  onSubmit(): void {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    this.isLoading     = true;
    this.errorMessage  = '';
    this.successMessage = '';

    const request: CustomerRequest = this.customerForm.value;

    this.customerService.createCustomer(request).subscribe({
      next: () => {
        this.isLoading      = false;
        this.successMessage = 'Customer created successfully!';
        this.customerForm.reset();
        this.customerCreated.emit();
      },
      error: (err: Error) => {
        this.isLoading    = false;
        this.errorMessage = err.message;
      }
    });
  }

  onReset(): void {
    this.customerForm.reset();
    this.errorMessage   = '';
    this.successMessage = '';
  }

  private initForm(): void {
    this.customerForm = this.fb.group({
      firstName:   ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      lastName:    ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      dateOfBirth: ['', [Validators.required]]
    });
  }
}
