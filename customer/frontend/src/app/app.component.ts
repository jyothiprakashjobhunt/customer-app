import { Component, ViewChild } from '@angular/core';
import { CustomerListComponent } from './features/customer-list/customer-list.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Customer Management';

  @ViewChild(CustomerListComponent)
  customerListComponent!: CustomerListComponent;

  onCustomerCreated(): void {
    this.customerListComponent.loadCustomers();
  }
}
