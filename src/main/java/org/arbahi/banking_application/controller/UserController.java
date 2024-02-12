package org.arbahi.banking_application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.arbahi.banking_application.dto.*;
import org.arbahi.banking_application.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@Tag(name = "User Account Management System")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "create an account",
            description = "create an account for the user and generate an account number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "account Created Successfully"
    )
    @PostMapping("user")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }
    @Operation(
            summary = "Account Balance",
            description = "get the balance for user account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "account Created Successfully"
    )
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.getBalanceEnquiry(enquiryRequest);
    }
    @Operation(
            summary = "Account name",
            description = "get the name of the user account "
    )
    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.getNameEnquiry(enquiryRequest);
    }
    @Operation(
            summary = "credit an account",
            description = "credit an amount in an account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "account credited Successfully"
    )
    @PostMapping("credit")
    public BankResponse creditOperation(@RequestBody CreditDebitDTO request){
        return userService.creditAccount(request);
    }
    @Operation(
            summary = "debit an account",
            description = "debit an amount in an account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "account debited Successfully"
    )
    @PostMapping("debit")
    public BankResponse debitOperation(@RequestBody CreditDebitDTO request){
        return userService.debitAccount(request);
    }
    @Operation(
            summary = "transfer an amount",
            description = "transfer an amount from an account to another "
    )
    @ApiResponse(
            responseCode = "200",
            description = "amount transfered successfully"
    )
    @PostMapping("transfer")
    public BankResponse transferOperation(@RequestBody TransferDTO transferDTO){
        return userService.transferOperation(transferDTO);
    }

}
