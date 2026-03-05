package com.customerapp.service;

import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;
import com.customerapp.exception.CustomerNotFoundException;
import com.customerapp.mapper.CustomerMapper;
import com.customerapp.model.Customer;
import com.customerapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerMapper      customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequestDto  validRequest;
    private Customer            persistedCustomer;
    private CustomerResponseDto expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = new CustomerRequestDto("Alice", "Brown", LocalDate.of(1992, 7, 10));

        persistedCustomer = Customer.builder()
                .id(1L).firstName("Alice").lastName("Brown")
                .dateOfBirth(LocalDate.of(1992, 7, 10))
                .build();

        expectedResponse = new CustomerResponseDto(1L, "Alice", "Brown",
                LocalDate.of(1992, 7, 10), OffsetDateTime.now(), OffsetDateTime.now());
    }

    @Nested
    @DisplayName("createCustomer()")
    class CreateCustomer {

        @Test
        void should_returnMappedResponseDto_when_customerIsSavedSuccessfully() {
            // Arrange
            when(customerMapper.toEntity(validRequest)).thenReturn(persistedCustomer);
            when(customerRepository.save(persistedCustomer)).thenReturn(persistedCustomer);
            when(customerMapper.toResponseDto(persistedCustomer)).thenReturn(expectedResponse);

            // Act
            CustomerResponseDto result = customerService.createCustomer(validRequest);

            // Assert
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.firstName()).isEqualTo("Alice");
        }

        @Test
        void should_delegateMappingToCustomerMapper_when_requestIsValid() {
            // Arrange
            when(customerMapper.toEntity(any())).thenReturn(persistedCustomer);
            when(customerRepository.save(any())).thenReturn(persistedCustomer);
            when(customerMapper.toResponseDto(any())).thenReturn(expectedResponse);

            // Act
            customerService.createCustomer(validRequest);

            // Assert
            verify(customerMapper, times(1)).toEntity(validRequest);
            verify(customerMapper, times(1)).toResponseDto(persistedCustomer);
        }

        @Test
        void should_invokeRepositorySaveExactlyOnce_when_requestIsValid() {
            // Arrange
            when(customerMapper.toEntity(any())).thenReturn(persistedCustomer);
            when(customerRepository.save(any())).thenReturn(persistedCustomer);
            when(customerMapper.toResponseDto(any())).thenReturn(expectedResponse);

            // Act
            customerService.createCustomer(validRequest);

            // Assert
            verify(customerRepository, times(1)).save(any(Customer.class));
            verifyNoMoreInteractions(customerRepository);
        }
    }

    @Nested
    @DisplayName("getAllCustomers()")
    class GetAllCustomers {

        @Test
        void should_returnMappedResponseDtoList_when_customersExistInRepository() {
            // Arrange
            List<Customer>            stored   = List.of(persistedCustomer);
            List<CustomerResponseDto> expected = List.of(expectedResponse);
            when(customerRepository.findAll()).thenReturn(stored);
            when(customerMapper.toResponseDtoList(stored)).thenReturn(expected);

            // Act
            List<CustomerResponseDto> result = customerService.getAllCustomers();

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).firstName()).isEqualTo("Alice");
        }

        @Test
        void should_returnEmptyList_when_noCustomersExistInRepository() {
            // Arrange
            when(customerRepository.findAll()).thenReturn(Collections.emptyList());
            when(customerMapper.toResponseDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

            // Act & Assert
            assertThat(customerService.getAllCustomers()).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("getCustomerById()")
    class GetCustomerById {

        @Test
        void should_returnMappedResponseDto_when_customerExistsForGivenId() {
            // Arrange
            when(customerRepository.findById(1L)).thenReturn(Optional.of(persistedCustomer));
            when(customerMapper.toResponseDto(persistedCustomer)).thenReturn(expectedResponse);

            // Act
            CustomerResponseDto result = customerService.getCustomerById(1L);

            // Assert
            assertThat(result.id()).isEqualTo(1L);
            verify(customerMapper, times(1)).toResponseDto(persistedCustomer);
        }

        @Test
        void should_returnNotFound_when_customerDoesNotExistForGivenId() {
            // Arrange
            when(customerRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> customerService.getCustomerById(999L))
                    .isInstanceOf(CustomerNotFoundException.class)
                    .hasMessageContaining("999");

            verifyNoInteractions(customerMapper);
        }

        @Test
        void should_includeCustomerIdInErrorMessage_when_customerDoesNotExist() {
            // Arrange
            when(customerRepository.findById(42L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> customerService.getCustomerById(42L))
                    .isInstanceOf(CustomerNotFoundException.class)
                    .hasMessageContaining("42");
        }
    }
}
