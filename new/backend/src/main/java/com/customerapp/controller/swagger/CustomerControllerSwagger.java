package com.customerapp.controller.swagger;

import com.customerapp.dto.CustomerRequestDto;
import com.customerapp.dto.CustomerResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Customer Management", description = "Endpoints for creating and retrieving customer records")
public interface CustomerControllerSwagger {

	@Operation(summary = "Create a new customer")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(example = "{\"firstName\":\"First name is required\"}"))),
			@ApiResponse(responseCode = "500", description = "Unexpected server error") })
	ResponseEntity<CustomerResponseDto> createCustomer(
			@RequestBody(description = "Customer details", required = true, content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))) CustomerRequestDto requestDto);

	@Operation(summary = "Retrieve all customers")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Customers returned successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerResponseDto.class)))),
			@ApiResponse(responseCode = "500", description = "Unexpected server error") })
	ResponseEntity<List<CustomerResponseDto>> getAllCustomers();

	@Operation(summary = "Retrieve a customer by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(example = "{\"status\":404,\"message\":\"Customer not found with id: 1\"}"))),
			@ApiResponse(responseCode = "500", description = "Unexpected server error") })
	ResponseEntity<CustomerResponseDto> getCustomerById(
			@Parameter(description = "Customer ID", required = true, example = "1") Long id);
}
