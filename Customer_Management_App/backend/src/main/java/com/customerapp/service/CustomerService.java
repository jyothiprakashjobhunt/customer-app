package com.customerapp.service;

import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto requestDto);

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto getCustomerById(Long id);
}
