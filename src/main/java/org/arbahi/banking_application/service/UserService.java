package org.arbahi.banking_application.service;

import org.arbahi.banking_application.dto.BankResponse;
import org.arbahi.banking_application.dto.UserRequest;
import org.springframework.stereotype.Service;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}
