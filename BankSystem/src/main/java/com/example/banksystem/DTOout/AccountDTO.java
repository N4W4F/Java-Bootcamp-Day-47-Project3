package com.example.banksystem.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private Double balance;
    private Boolean isActive;
}
