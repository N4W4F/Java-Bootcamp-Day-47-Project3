package com.example.banksystem.Service;

import com.example.banksystem.ApiResponse.ApiException;
import com.example.banksystem.DTOout.EmployeeDTO;
import com.example.banksystem.Model.Employee;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Repository.AuthRepository;
import com.example.banksystem.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AuthRepository authRepository;

    public List<EmployeeDTO> getAllEmployees(Integer adminId) {
        MyUser admin = authRepository.findMyUserById(adminId);
        if (admin == null)
            throw new ApiException("Admin with ID: " + adminId + " was not found");

        if (admin.getRole().equals("ADMIN")) {
            List<Employee> employees = employeeRepository.findAll();
            List<EmployeeDTO> employeeDTOS = new ArrayList<>();

            for (Employee e : employees) {
                employeeDTOS.add(new EmployeeDTO(
                        e.getUser().getUsername(),
                        e.getUser().getName(),
                        e.getUser().getEmail(),
                        e.getPosition(),
                        e.getSalary()));
            }
            return employeeDTOS;
        }
        else throw new ApiException("You don't have the permission to access this endpoint");
    }

    public void register(com.example.banksystem.DTOin.EmployeeDTO employeeDTO) {
        MyUser myUser = new MyUser();
        myUser.setUsername(employeeDTO.getUsername());
        myUser.setPassword(new BCryptPasswordEncoder().encode(employeeDTO.getPassword()));
        myUser.setName(employeeDTO.getName());
        myUser.setEmail(employeeDTO.getEmail());
        myUser.setRole("EMPLOYEE");
        authRepository.save(myUser);

        Employee employee = new Employee();
        employee.setPosition(employeeDTO.getPosition());
        employee.setSalary(employeeDTO.getSalary());
        employee.setUser(myUser);
        employeeRepository.save(employee);
    }

    public void updateEmployee(Integer authId, Integer employeeId, com.example.banksystem.DTOin.EmployeeDTO employeeDTO) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Admin with ID: " + authId + " was not found");

        MyUser oldEmployee = authRepository.findMyUserById(employeeId);
        if (oldEmployee == null)
            throw new ApiException("Employee with ID: " + employeeId + " was not found");

        if (authId.equals(employeeId) || auth.getRole().equals("ADMIN")) {
            oldEmployee.setUsername(employeeDTO.getUsername());
            oldEmployee.setPassword(new BCryptPasswordEncoder().encode(employeeDTO.getPassword()));
            oldEmployee.setName(employeeDTO.getName());
            oldEmployee.setEmail(employeeDTO.getEmail());
            oldEmployee.getEmployee().setPosition(employeeDTO.getPosition());
            oldEmployee.getEmployee().setSalary(employeeDTO.getSalary());
            authRepository.save(oldEmployee);
        } else throw new ApiException("You don't have the access to update this employee");
    }

    public void deleteEmployee(Integer authId, Integer employeeId) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null)
            throw new ApiException("Admin with ID: " + authId + " was not found");

        MyUser oldEmployee = authRepository.findMyUserById(employeeId);
        if (oldEmployee == null)
            throw new ApiException("Employee with ID: " + employeeId + " was not found");

        if (authId.equals(employeeId) || auth.getRole().equals("ADMIN"))
            authRepository.delete(oldEmployee);
        else throw new ApiException("You don't have the access to delete this employee");
    }
}
