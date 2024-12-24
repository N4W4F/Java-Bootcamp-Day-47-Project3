package com.example.banksystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(19) NOT NULL UNIQUE")
    @NotEmpty(message = "Account Number cannot be empty")
    @Pattern(regexp = "\\b\\d{4}-\\d{4}-\\d{4}-\\d{4}\\b",
            message = "Account Number must follow the format XXXX-XXXX-XXXX-XXXX")
    private String accountNumber;

    @Column(columnDefinition = "DECIMAL NOT NULL")
    @NotNull(message = "Account Balance cannot be empty")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be a non-negative decimal number")
    private Double balance;

    @Column(columnDefinition = "BOOLEAN NOT NULL")
    private Boolean isActive = false;

    // Relations
    @ManyToOne
    @JsonIgnore
    private Customer customer;
}
