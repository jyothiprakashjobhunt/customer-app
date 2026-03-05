package com.customerapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request payload for creating a new customer")
public record CustomerRequestDto(

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        @Schema(description = "Customer's first name", example = "John")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        @Schema(description = "Customer's last name", example = "Doe")
        String lastName,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Date of birth (yyyy-MM-dd)", example = "1990-05-15",
                type = "string", format = "date")
        LocalDate dateOfBirth

) {}
