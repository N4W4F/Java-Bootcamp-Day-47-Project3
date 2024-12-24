package com.example.banksystem.Controller;

import com.example.banksystem.ApiResponse.ApiResponse;
import com.example.banksystem.Model.Account;
import com.example.banksystem.Model.MyUser;
import com.example.banksystem.Service.AccountService;
import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/get")
    public ResponseEntity getAllAccount(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(accountService.getAllAccounts(myUser.getId()));
    }

    @GetMapping("/get-my")
    public ResponseEntity getMyAccounts(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(accountService.getMyAccounts(myUser.getId()));
    }

    @PostMapping("/create")
    public ResponseEntity createAccount(@AuthenticationPrincipal MyUser myUser,
                                        @RequestBody @Valid Account account) {
        accountService.createAccount(myUser.getId(), account);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Account has been created successfully"));
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity updateAccount(@AuthenticationPrincipal MyUser myUser,
                                        @PathVariable Integer accountId,
                                        @RequestBody @Valid Account account) {
        accountService.updateAccount(myUser.getId(), accountId, account);
        return ResponseEntity.status(200).body(new ApiResponse("Account has been updated successfully"));
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity deleteAccount(@AuthenticationPrincipal MyUser myUser,
                                        @PathVariable Integer accountId) {
        accountService.deleteAccount(myUser.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account has been deleted successfully"));
    }

    @GetMapping("/view-details/{accountId}")
    public ResponseEntity viewAccountDetail(@AuthenticationPrincipal MyUser myUser,
                                            @PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(accountService.viewAccountDetails(myUser.getId(), accountId));
    }

    @PutMapping("/deposit-to/{accountId}/amount/{amount}")
    public ResponseEntity depositAmount(@AuthenticationPrincipal MyUser myUser,
                                        @PathVariable Integer accountId,
                                        @PathVariable Double amount) {
        accountService.depositAmount(myUser.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Amount: " + amount + " has been deposited from account: "
                                                                + accountId + " successfully"));
    }

    @PutMapping("/withdraw-from/{accountId}/amount/{amount}")
    public ResponseEntity withdrawAmount(@AuthenticationPrincipal MyUser myUser,
                                         @PathVariable Integer accountId,
                                         @PathVariable Double amount) {
        accountService.withdrawAmount(myUser.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Amount: " + amount + " has been withdrawn from account: "
                                                                + accountId + " successfully"));
    }

    @PutMapping("/transfer-from/{senderAccountId}/to/{receiverAccountId}/amount/{amount}")
    public ResponseEntity transferAmount(@AuthenticationPrincipal MyUser myUser,
                                         @PathVariable Integer senderAccountId,
                                         @PathVariable Integer receiverAccountId,
                                         @PathVariable Double amount) {
        accountService.transferAmount(myUser.getId(), senderAccountId, receiverAccountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Amount: " + amount + " has been transferred from account: " + senderAccountId
                                                                + "to account: " + receiverAccountId + " successfully"));
    }

    @PutMapping("/activate/{accountId}")
    public ResponseEntity activateAccount(@AuthenticationPrincipal MyUser myUser,
                                          @PathVariable Integer accountId) {
        accountService.activateAccount(myUser.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account has been activated successfully"));
    }

    @PutMapping("/block/{accountId}")
    public ResponseEntity blockAccount(@AuthenticationPrincipal MyUser myUser,
                                       @PathVariable Integer accountId) {
        accountService.blockAccount(myUser.getId(), accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account has been blocked successfully"));
    }
}
