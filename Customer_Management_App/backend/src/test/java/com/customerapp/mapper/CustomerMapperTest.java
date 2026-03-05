package com.customerapp.mapper;

import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;
import com.customerapp.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Nested
    @DisplayName("toEntity()")
    class ToEntity {

        @Test
        void should_mapAllFieldsCorrectly_when_requestDtoIsValid() {
            // Arrange
            CustomerRequestDto request = new CustomerRequestDto("John", "Doe", LocalDate.of(1990, 5, 15));

            // Act
            Customer result = customerMapper.toEntity(request);

            // Assert
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");
            assertThat(result.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 5, 15));
        }

        @Test
        void should_notSetIdOrAuditFields_when_mappingRequestDtoToEntity() {
            // Arrange
            CustomerRequestDto request = new CustomerRequestDto("John", "Doe", LocalDate.of(1990, 5, 15));

            // Act
            Customer result = customerMapper.toEntity(request);

            // Assert
            assertThat(result.getId()).isNull();
            assertThat(result.getCreatedAt()).isNull();
            assertThat(result.getUpdatedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("toResponseDto()")
    class ToResponseDto {

        @Test
        void should_mapAllFieldsCorrectly_when_customerEntityIsValid() {
            // Arrange
            OffsetDateTime now = OffsetDateTime.now();
            Customer customer = Customer.builder()
                    .id(1L).firstName("Jane").lastName("Smith")
                    .dateOfBirth(LocalDate.of(1985, 3, 20))
                    .createdAt(now).updatedAt(now)
                    .build();

            // Act
            CustomerResponseDto result = customerMapper.toResponseDto(customer);

            // Assert
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.firstName()).isEqualTo("Jane");
            assertThat(result.lastName()).isEqualTo("Smith");
            assertThat(result.createdAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("toResponseDtoList()")
    class ToResponseDtoList {

        @Test
        void should_returnMappedList_when_customerListIsNotEmpty() {
            // Arrange
            OffsetDateTime now = OffsetDateTime.now();
            List<Customer> customers = List.of(
                    Customer.builder().id(1L).firstName("Alice").lastName("Brown")
                            .dateOfBirth(LocalDate.of(1992, 7, 10)).createdAt(now).updatedAt(now).build(),
                    Customer.builder().id(2L).firstName("Tom").lastName("Jones")
                            .dateOfBirth(LocalDate.of(1988, 1, 1)).createdAt(now).updatedAt(now).build()
            );

            // Act
            List<CustomerResponseDto> result = customerMapper.toResponseDtoList(customers);

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result.get(0).firstName()).isEqualTo("Alice");
            assertThat(result.get(1).firstName()).isEqualTo("Tom");
        }

        @Test
        void should_returnEmptyList_when_customerListIsEmpty() {
            // Act & Assert
            assertThat(customerMapper.toResponseDtoList(List.of())).isNotNull().isEmpty();
        }
    }
}
