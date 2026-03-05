package com.customerapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "customers", indexes = { @Index(name = "idx_customers_first_name", columnList = "first_name"),
		@Index(name = "idx_customers_last_name", columnList = "last_name"),
		@Index(name = "idx_customers_date_of_birth", columnList = "date_of_birth") })
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@NotBlank(message = "First name is required")
	@Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;

	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	@Column(name = "date_of_birth", nullable = false, updatable = false)
	private LocalDate dateOfBirth;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Customer other))
			return false;
		
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Customer{id=%d, firstName='%s', lastName='%s', dateOfBirth=%s}".formatted(id, firstName, lastName,
				dateOfBirth);
	}
}
