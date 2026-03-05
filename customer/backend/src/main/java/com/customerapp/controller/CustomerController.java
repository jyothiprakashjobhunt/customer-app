package com.customerapp.controller;

import com.customerapp.controller.swagger.CustomerControllerSwagger;
import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;
import com.customerapp.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerSwagger {

	private final CustomerService customerService;

	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto requestDto) {

		log.info("Creating customer: {} {}", requestDto.firstName(), requestDto.lastName());
		CustomerResponseDto response = customerService.createCustomer(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	@GetMapping
	public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
		log.info("Fetching all customers");

		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
		log.info("Fetching customer id: {}", id);

		return ResponseEntity.ok(customerService.getCustomerById(id));
	}
}
