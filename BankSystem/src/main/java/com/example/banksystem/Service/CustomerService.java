package com.example.banksystem.Service;

import com.example.banksystem.ApiResponse.ApiException;
import com.example.banksystem.DTOout.AccountDTO;
import com.example.banksystem.DTOout.CustomerDTO;
import com.example.banksystem.Model.Account;
import com.example.banksystem.Model.Customer;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Repository.AuthRepository;
import com.example.banksystem.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;

    public List<CustomerDTO> getAllCustomers(Integer authId) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Admin or Employee with ID: " + authId + " was not found");

        if (auth.getRole().equals("ADMIN") || auth.getRole().equals("EMPLOYEE")) {

            List<Customer> customers = customerRepository.findAll();
            if (customers.isEmpty())
                throw new ApiException("There are no customers yet");

            List<CustomerDTO> customerDTOS = new ArrayList<>();
            for (Customer c : customers) {
                // Adding customer accounts to a list
                List<AccountDTO> accountDTOS = new ArrayList<>();
                for (Account a : c.getAccounts())
                    accountDTOS.add(new AccountDTO(a.getAccountNumber(), a.getBalance(), a.getIsActive()));

                customerDTOS.add(new CustomerDTO(
                        c.getUser().getUsername(),
                        c.getUser().getName(),
                        c.getUser().getEmail(),
                        c.getPhoneNumber(),
                        accountDTOS));
            }
            return customerDTOS;
        } else throw new ApiException("You don't have the permission to access this endpoint");
    }

    public void register(com.example.banksystem.DTOin.CustomerDTO customerDTO) {
        MyUser myUser = new MyUser();
        myUser.setUsername(customerDTO.getUsername());
        myUser.setPassword(new BCryptPasswordEncoder().encode(customerDTO.getPassword()));
        myUser.setName(customerDTO.getName());
        myUser.setEmail(customerDTO.getEmail());
        myUser.setRole("CUSTOMER");
        authRepository.save(myUser);

        Customer customer = new Customer();
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setUser(myUser);
        customerRepository.save(customer);
    }

    public void updateCustomer(Integer authId, Integer customerId, com.example.banksystem.DTOin.CustomerDTO customerDTO) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Admin with ID: " + authId + " was not found");

        MyUser oldCustomer = authRepository.findMyUserById(customerId);
        if (oldCustomer == null)
            throw new ApiException("Customer with ID: " + customerId + " was not found");

        if (authId.equals(customerId) || auth.getRole().equals("ADMIN") || auth.getRole().equals("EMPLOYEE")) {
            oldCustomer.setUsername(customerDTO.getUsername());
            oldCustomer.setPassword(new BCryptPasswordEncoder().encode(customerDTO.getPassword()));
            oldCustomer.setName(customerDTO.getName());
            oldCustomer.setEmail(customerDTO.getEmail());
            oldCustomer.getCustomer().setPhoneNumber(customerDTO.getPhoneNumber());
            authRepository.save(oldCustomer);
        } else throw new ApiException("You don't have access to update this customer");
    }

    public void deleteCustomer(Integer authId, Integer customerId) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Customer with ID: " + authId + " was not found");

        MyUser oldCustomer = authRepository.findMyUserById(customerId);
        if (oldCustomer == null)
            throw new ApiException("Customer with ID: " + customerId + " was not found");

        if (authId.equals(customerId) || auth.getRole().equals("ADMIN") || auth.getRole().equals("EMPLOYEE"))
            authRepository.delete(oldCustomer);

        else throw new ApiException("You don't have the permission to delete a customer");
    }
}
