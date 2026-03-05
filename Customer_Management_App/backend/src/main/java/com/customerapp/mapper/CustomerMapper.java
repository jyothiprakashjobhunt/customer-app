package com.customerapp.mapper;

import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;
import com.customerapp.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

	public Customer toEntity(CustomerRequestDto requestDto) {
		return Customer.builder().firstName(requestDto.firstName()).lastName(requestDto.lastName())
				.dateOfBirth(requestDto.dateOfBirth()).build();
	}

	public CustomerResponseDto toResponseDto(Customer customer) {
		return new CustomerResponseDto(customer.getId(), customer.getFirstName(), customer.getLastName(),
				customer.getDateOfBirth(), customer.getCreatedAt(), customer.getUpdatedAt());
	}

	public List<CustomerResponseDto> toResponseDtoList(List<Customer> customers) {
		return customers.stream().map(this::toResponseDto).toList();
	}
}
