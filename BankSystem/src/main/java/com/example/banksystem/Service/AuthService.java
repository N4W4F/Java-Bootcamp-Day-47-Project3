package com.example.banksystem.Service;

import com.example.banksystem.ApiResponse.ApiException;
import com.example.banksystem.DTOin.CustomerDTO;
import com.example.banksystem.DTOin.EmployeeDTO;
import com.example.banksystem.Model.Customer;
import com.example.banksystem.Model.Employee;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Repository.AuthRepository;
import com.example.banksystem.Repository.CustomerRepository;
import com.example.banksystem.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public List<MyUser> getAllUsers(Integer adminId) {
        MyUser admin = authRepository.findMyUserById(adminId);
        if (admin == null)
            throw new ApiException("Admin with ID: " + adminId + " was not found");

        if (admin.getRole().equals("ADMIN"))
            return authRepository.findAll();

        throw new ApiException("You don't have the permission to access this endpoint");
    }
}
