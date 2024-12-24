package com.example.banksystem.Repository;

import com.example.banksystem.Model.Account;
import com.example.banksystem.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountById(Integer id);
    Account findAccountsByIdAndCustomer(Integer id, Customer customer);
    List<Account> findAccountsByCustomer(Customer customer);
}
