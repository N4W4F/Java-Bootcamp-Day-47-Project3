package com.example.banksystem.Controller;

import com.example.banksystem.ApiResponse.ApiResponse;
import com.example.banksystem.DTOin.CustomerDTO;
import com.example.banksystem.DTOin.EmployeeDTO;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/get")
    public ResponseEntity getAllUsers(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(authService.getAllUsers(myUser.getId()));
    }
}
