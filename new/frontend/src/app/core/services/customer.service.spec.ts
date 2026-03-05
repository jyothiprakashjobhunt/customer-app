import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CustomerService } from './customer.service';
import { Customer, CustomerRequest } from '../models/customer.model';
import { environment } from '../../../environments/environment';

describe('CustomerService', () => {

  let service:  CustomerService;
  let httpMock: HttpTestingController;

  const BASE_URL = `${environment.apiBaseUrl}/api/v1/customers`;

  const mockCustomer: Customer = {
    id: 1, firstName: 'John', lastName: 'Doe', dateOfBirth: '1990-05-15',
    createdAt: '2026-03-04T10:30:00+00:00', updatedAt: '2026-03-04T10:30:00+00:00'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:   [HttpClientTestingModule],
      providers: [CustomerService]
    });
    service  = TestBed.inject(CustomerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  describe('createCustomer()', () => {

    it('should_returnCreatedCustomer_when_postRequestIsSuccessful', () => {
      // Arrange
      const request: CustomerRequest = { firstName: 'John', lastName: 'Doe', dateOfBirth: '1990-05-15' };

      // Act
      service.createCustomer(request).subscribe(result => {
        // Assert
        expect(result.id).toBe(1);
        expect(result.firstName).toBe('John');
        expect(result.createdAt).toBeTruthy();
      });

      const req = httpMock.expectOne(BASE_URL);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(request);
      req.flush(mockCustomer);
    });

    it('should_sendCompleteRequestBodyToServer_when_createCustomerIsCalled', () => {
      // Arrange
      const request: CustomerRequest = { firstName: 'Alice', lastName: 'Smith', dateOfBirth: '1995-08-22' };

      // Act
      service.createCustomer(request).subscribe();

      // Assert
      const req = httpMock.expectOne(BASE_URL);
      expect(req.request.body.firstName).toBe('Alice');
      expect(req.request.body.lastName).toBe('Smith');
      expect(req.request.body.dateOfBirth).toBe('1995-08-22');
      req.flush({ ...mockCustomer, ...request });
    });
  });

  describe('getAllCustomers()', () => {

    it('should_returnCustomerList_when_getRequestIsSuccessful', () => {
      // Arrange
      const customers: Customer[] = [
        { ...mockCustomer, id: 1, firstName: 'Jane' },
        { ...mockCustomer, id: 2, firstName: 'Bob'  }
      ];

      // Act
      service.getAllCustomers().subscribe(result => {
        // Assert
        expect(result.length).toBe(2);
        expect(result[0].firstName).toBe('Jane');
        expect(result[1].firstName).toBe('Bob');
      });

      const req = httpMock.expectOne(BASE_URL);
      expect(req.request.method).toBe('GET');
      req.flush(customers);
    });

    it('should_returnEmptyArray_when_serverReturnsNoCustomers', () => {
      // Act
      service.getAllCustomers().subscribe(result => {
        // Assert
        expect(result.length).toBe(0);
      });

      const req = httpMock.expectOne(BASE_URL);
      expect(req.request.method).toBe('GET');
      req.flush([]);
    });
  });

  describe('getCustomerById()', () => {

    it('should_returnCustomer_when_customerExistsForGivenId', () => {
      // Act
      service.getCustomerById(1).subscribe(result => {
        // Assert
        expect(result.id).toBe(1);
        expect(result.firstName).toBe('John');
      });

      const req = httpMock.expectOne(`${BASE_URL}/1`);
      expect(req.request.method).toBe('GET');
      req.flush(mockCustomer);
    });

    it('should_propagateError_when_serverReturns404ForNonExistentCustomer', () => {
      // Act
      service.getCustomerById(999).subscribe({
        next: () => fail('Expected error but got success'),
        error: (err: Error) => {
          // Assert
          expect(err).toBeTruthy();
        }
      });

      const req = httpMock.expectOne(`${BASE_URL}/999`);
      req.flush(
        { message: 'Customer not found with id: 999' },
        { status: 404, statusText: 'Not Found' }
      );
    });
  });
});
