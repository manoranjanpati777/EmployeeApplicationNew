package com.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "employee_id", unique = true, nullable = false)
    @Pattern(regexp = "E\\d{3}", message = "Employee ID should follow the format 'E123'.")
    private String employeeId;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is required.")
    private String email;

    @ElementCollection
    @NotEmpty(message = "Phone numbers are required.")
    private List<@Pattern(regexp = "\\d{10}", message = "Phone number should be 10 digits.") String> phoneNumbers;

    @NotNull(message = "Date of joining is required.")
    private LocalDate doj;

    @Min(value = 1, message = "Salary must be a positive number.")
    private double salary;

}
