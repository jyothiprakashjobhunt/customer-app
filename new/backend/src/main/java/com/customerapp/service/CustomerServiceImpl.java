package com.customerapp.service;

import com.customerapp.config.CacheConfig.CacheNames;
import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;
import com.customerapp.exception.CustomerNotFoundException;
import com.customerapp.mapper.CustomerMapper;
import com.customerapp.model.Customer;
import com.customerapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CUSTOMERS, allEntries = true)
	public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {
		log.info("Creating customer: {} {}", requestDto.firstName(), requestDto.lastName());
		Customer entity = customerMapper.toEntity(requestDto);
		Customer saved = customerRepository.save(entity);
		log.info("Customer created with id: {}", saved.getId());

		return customerMapper.toResponseDto(saved);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = CacheNames.CUSTOMERS)
	public List<CustomerResponseDto> getAllCustomers() {
		log.info("Fetching all customers");

		return customerMapper.toResponseDtoList(customerRepository.findAll());
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = CacheNames.CUSTOMER_BY_ID, key = "#id")
	public CustomerResponseDto getCustomerById(Long id) {
		log.info("Fetching customer id: {}", id);
		Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

		return customerMapper.toResponseDto(customer);
	}
}
