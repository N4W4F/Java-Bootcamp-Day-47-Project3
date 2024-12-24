package com.example.banksystem.DTOout;

import com.example.banksystem.Model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomerDTO {
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private List<AccountDTO> accounts;
}
