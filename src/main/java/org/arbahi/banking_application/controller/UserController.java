package org.arbahi.banking_application.controller;

import lombok.RequiredArgsConstructor;
import org.arbahi.banking_application.dto.BankResponse;
import org.arbahi.banking_application.dto.UserRequest;
import org.arbahi.banking_application.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("user")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

}
