package com.example.banksystem.Controller;

import com.example.banksystem.ApiResponse.ApiResponse;
import com.example.banksystem.DTOin.CustomerDTO;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity getAllCustomers(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.getAllCustomers(myUser.getId()));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid CustomerDTO customerDTO) {
        customerService.register(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Customer has been registered successfully"));
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity updateCustomer(@AuthenticationPrincipal MyUser myUser,
                                         @PathVariable Integer customerId,
                                         @RequestBody @Valid CustomerDTO customerDTO) {
        customerService.updateCustomer(myUser.getId(), customerId, customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Customer with ID: " + customerId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity deleteCustomer(@AuthenticationPrincipal MyUser myUser,
                                         @PathVariable Integer customerId) {
        customerService.deleteCustomer(myUser.getId(), customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Customer with ID: " + customerId + " has been deleted successfully"));
    }
}
