package com.example.banksystem.Service;

import com.example.banksystem.ApiResponse.ApiException;
import com.example.banksystem.DTOout.AccountDTO;
import com.example.banksystem.Model.Account;
import com.example.banksystem.Model.Customer;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Repository.AccountRepository;
import com.example.banksystem.Repository.AuthRepository;
import com.example.banksystem.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;

    public List<Account> getAllAccounts(Integer authId) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Admin or Employee with ID: " + authId + " was not found");

        if (auth.getRole().equals("ADMIN") || auth.getRole().equals("EMPLOYEE"))
            return accountRepository.findAll();
        else throw new ApiException("You don't have the permission to access this endpoint");
    }

    public List<AccountDTO> getMyAccounts(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        List<Account> accounts = accountRepository.findAccountsByCustomer(customer);
        if (accounts.isEmpty())
            throw new ApiException("You don't have any accounts yet");

        List<AccountDTO> accountDTOS = new ArrayList<>();
        for (Account a : accounts)
            accountDTOS.add(new AccountDTO(a.getAccountNumber(), a.getBalance(), a.getIsActive()));

        return accountDTOS;
    }

    public void createAccount(Integer customerId, Account account) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        account.setCustomer(customer);
        accountRepository.save(account);

        customer.getAccounts().add(account);
        customerRepository.save(customer);
    }

    public void updateAccount(Integer customerId, Integer accountId, Account account) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        Account oldAccount = accountRepository.findAccountsByIdAndCustomer(accountId, customer);
        if (oldAccount == null)
            throw new ApiException("Account was not found");

        oldAccount.setBalance(account.getBalance());
        accountRepository.save(oldAccount);
    }

    public void deleteAccount(Integer customerId, Integer accountId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        Account account = accountRepository.findAccountsByIdAndCustomer(accountId, customer);
        if (account == null)
            throw new ApiException("Account was not found");

        accountRepository.delete(account);
    }

    public Account viewAccountDetails(Integer customerId, Integer accountId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        Account account = accountRepository.findAccountsByIdAndCustomer(accountId, customer);
        if (account == null)
            throw new ApiException("Account was not found");

        return account;
    }

    public void depositAmount(Integer customerId, Integer accountId, Double amount) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        Account account = accountRepository.findAccountsByIdAndCustomer(accountId, customer);
        if (account == null)
            throw new ApiException("Account was not found");

        if (!account.getIsActive())
            throw new ApiException("Your account is not activated yet");

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    public void withdrawAmount(Integer customerId, Integer accountId, Double amount) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        Account account = accountRepository.findAccountsByIdAndCustomer(accountId, customer);
        if (account == null)
            throw new ApiException("Account was not found");

        if (!account.getIsActive())
            throw new ApiException("Your account is not activated yet");

        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
        } else throw new ApiException("You don't have enough balance to withdraw this amount");
    }

    public void transferAmount(Integer customerId, Integer senderAccountId, Integer receiverAccountId, Double amount) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null)
            throw new ApiException("Customer was not found");

        Account sender = accountRepository.findAccountsByIdAndCustomer(senderAccountId, customer);
        if (sender == null)
            throw new ApiException("Sender account was not found");

        Account receiver = accountRepository.findAccountById(receiverAccountId);
        if (receiver == null)
            throw new ApiException("Receiver account was not found");

        if (!sender.getIsActive())
            throw new ApiException("The sender account is not activated yet");

        if (!receiver.getIsActive())
            throw new ApiException("The receiver account is not activated yet");

        if (sender.getBalance() >= amount) {
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);
            accountRepository.save(sender);
            accountRepository.save(receiver);
        }
        else throw new ApiException("You don't have enough balance to transfer this amount");
    }

    public void activateAccount(Integer authId, Integer accountId) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Admin or Employee with ID: " + authId + " was not found");

        Account account = accountRepository.findAccountById(accountId);
        if (account == null)
            throw new ApiException("Account with ID: " + accountId + " was not found");

        if (auth.getRole().equals("ADMIN") || auth.getRole().equals("EMPLOYEE")) {
            account.setIsActive(true);
            accountRepository.save(account);
        } else throw new ApiException("You don't have the permission to activate an account");
    }

    public void blockAccount(Integer authId, Integer accountId) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Admin or Employee with ID: " + authId + " was not found");

        Account account = accountRepository.findAccountById(accountId);
        if (account == null)
            throw new ApiException("Account with ID: " + accountId + " was not found");

        if (auth.getRole().equals("ADMIN") || auth.getRole().equals("EMPLOYEE")) {
            account.setIsActive(false);
            accountRepository.save(account);
        } else throw new ApiException("You don't have the permission to block an account");
    }
}
