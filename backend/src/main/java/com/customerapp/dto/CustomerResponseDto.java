package com.customerapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Schema(description = "Customer record returned by the API")
public record CustomerResponseDto(

        @Schema(description = "Customer ID", example = "1")
        Long id,

        @Schema(description = "First name", example = "John")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Date of birth", example = "1990-05-15", type = "string", format = "date")
        LocalDate dateOfBirth,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @Schema(description = "Created timestamp (UTC)", example = "2026-03-04T10:30:00+00:00",
                type = "string", format = "date-time")
        OffsetDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @Schema(description = "Last updated timestamp (UTC)", example = "2026-03-04T10:30:00+00:00",
                type = "string", format = "date-time")
        OffsetDateTime updatedAt

) {}
