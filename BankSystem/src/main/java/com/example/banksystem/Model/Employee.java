package com.example.banksystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Employee Position cannot be empty")
    private String position;

    @Column(columnDefinition = "DECIMAL NOT NULL")
    @PositiveOrZero(message = "Employee Salary must be a non-negative decimal number")
    private Double salary;

    // Relations
    @OneToOne
    @MapsId
    @JsonIgnore
    private MyUser user;
}
