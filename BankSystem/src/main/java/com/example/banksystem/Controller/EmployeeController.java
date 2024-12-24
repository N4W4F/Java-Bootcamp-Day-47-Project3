package com.example.banksystem.Controller;

import com.example.banksystem.ApiResponse.ApiResponse;
import com.example.banksystem.DTOin.EmployeeDTO;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/get")
    public ResponseEntity getAllEmployee(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(employeeService.getAllEmployees(myUser.getId()));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.register(employeeDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Employee has been registered successfully"));
    }

    @PutMapping("/update/{employeeId}")
    public ResponseEntity updateEmployee(@AuthenticationPrincipal MyUser myUser,
                                         @PathVariable Integer employeeId,
                                         @RequestBody @Valid EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(myUser.getId(), employeeId, employeeDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Employee with ID: " + employeeId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity deleteEmployee(@AuthenticationPrincipal MyUser myUser,
                                         @PathVariable Integer employeeId) {
        employeeService.deleteEmployee(myUser.getId(), employeeId);
        return ResponseEntity.status(200).body(new ApiResponse("Employee with ID: " + employeeId + " has been deleted successfully"));
    }
}
