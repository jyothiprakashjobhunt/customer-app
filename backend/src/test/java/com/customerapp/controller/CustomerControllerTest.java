package com.customerapp.controller;

import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;
import com.customerapp.exception.CustomerNotFoundException;
import com.customerapp.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private static final String CUSTOMERS_URL      = "/api/v1/customers";
    private static final String CUSTOMER_BY_ID_URL = "/api/v1/customers/{id}";

    @Autowired private MockMvc mockMvc;
    @MockBean  private CustomerService customerService;

    private ObjectMapper        objectMapper;
    private CustomerRequestDto  validRequest;
    private CustomerResponseDto savedResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        validRequest  = new CustomerRequestDto("John", "Doe", LocalDate.of(1990, 5, 15));
        savedResponse = new CustomerResponseDto(1L, "John", "Doe",
                LocalDate.of(1990, 5, 15), OffsetDateTime.now(), OffsetDateTime.now());
    }

    @Nested
    @DisplayName("POST /api/v1/customers")
    class CreateCustomer {

        @Test
        void should_return201WithCreatedCustomer_when_requestIsValid() throws Exception {
            // Arrange
            when(customerService.createCustomer(any(CustomerRequestDto.class))).thenReturn(savedResponse);

            // Act & Assert
            mockMvc.perform(post(CUSTOMERS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Doe"));

            verify(customerService, times(1)).createCustomer(any(CustomerRequestDto.class));
        }

        @Test
        void should_return400WithValidationError_when_firstNameIsBlank() throws Exception {
            // Arrange
            CustomerRequestDto invalidRequest = new CustomerRequestDto("", "Doe", LocalDate.of(1990, 5, 15));

            // Act & Assert
            mockMvc.perform(post(CUSTOMERS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.firstName").exists());

            verifyNoInteractions(customerService);
        }

        @Test
        void should_return400WithValidationError_when_lastNameIsBlank() throws Exception {
            // Arrange
            CustomerRequestDto invalidRequest = new CustomerRequestDto("John", "", LocalDate.of(1990, 5, 15));

            // Act & Assert
            mockMvc.perform(post(CUSTOMERS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.lastName").exists());

            verifyNoInteractions(customerService);
        }

        @Test
        void should_return400WithValidationError_when_dateOfBirthIsNull() throws Exception {
            // Arrange
            CustomerRequestDto invalidRequest = new CustomerRequestDto("John", "Doe", null);

            // Act & Assert
            mockMvc.perform(post(CUSTOMERS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.dateOfBirth").exists());

            verifyNoInteractions(customerService);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/customers")
    class GetAllCustomers {

        @Test
        void should_return200WithCustomerList_when_customersExist() throws Exception {
            // Arrange
            List<CustomerResponseDto> customers = List.of(
                    new CustomerResponseDto(1L, "Jane", "Smith", LocalDate.of(1985, 3, 20),
                            OffsetDateTime.now(), OffsetDateTime.now()),
                    new CustomerResponseDto(2L, "Bob", "Jones", LocalDate.of(1978, 11, 5),
                            OffsetDateTime.now(), OffsetDateTime.now())
            );
            when(customerService.getAllCustomers()).thenReturn(customers);

            // Act & Assert
            mockMvc.perform(get(CUSTOMERS_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].firstName").value("Jane"));

            verify(customerService, times(1)).getAllCustomers();
        }

        @Test
        void should_return200WithEmptyList_when_noCustomersExist() throws Exception {
            // Arrange
            when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get(CUSTOMERS_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/customers/{id}")
    class GetCustomerById {

        @Test
        void should_return200WithCustomer_when_customerExistsForGivenId() throws Exception {
            // Arrange
            when(customerService.getCustomerById(1L)).thenReturn(savedResponse);

            // Act & Assert
            mockMvc.perform(get(CUSTOMER_BY_ID_URL, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.firstName").value("John"));

            verify(customerService, times(1)).getCustomerById(1L);
        }

        @Test
        void should_return404_when_customerDoesNotExist() throws Exception {
            // Arrange
            when(customerService.getCustomerById(999L)).thenThrow(new CustomerNotFoundException(999L));

            // Act & Assert
            mockMvc.perform(get(CUSTOMER_BY_ID_URL, 999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Customer not found with id: 999"));
        }
    }
}
