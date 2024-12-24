package com.example.banksystem.DTOin;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
public class EmployeeDTO {
    private Integer employeeId;

    @NotEmpty(message = "Employee Username cannot be empty")
    @Size(min = 4, max = 10, message = "Username must be between 4 and 10 characters")
    private String username;

    @NotEmpty(message = "Employee Password cannot be empty")
    @Size(min = 6, max = 255, message = "User Password must be at least 6 characters")
    private String password;

    @NotEmpty(message = "Employee Name cannot be empty")
    @Size(min = 2, max = 20, message = "User Name must be between 2 and 20 characters")
    private String name;

    @NotEmpty(message = "Employee Email cannot be empty")
    @Email(message = "Employee Email must be a valid email format")
    private String email;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Employee Position cannot be empty")
    private String position;

    @Column(columnDefinition = "DECIMAL NOT NULL")
    @PositiveOrZero(message = "Employee Salary must be a non-negative decimal number")
    private Double salary;
}
