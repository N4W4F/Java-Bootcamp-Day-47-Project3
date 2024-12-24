package com.example.banksystem.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeDTO {
    private String username;
    private String name;
    private String email;
    private String position;
    private Double salary;
}
