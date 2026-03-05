export interface Customer {
  id:          number;
  firstName:   string;
  lastName:    string;
  dateOfBirth: string;
  createdAt:   string;
  updatedAt:   string;
}

export interface CustomerRequest {
  firstName:   string;
  lastName:    string;
  dateOfBirth: string;
}
