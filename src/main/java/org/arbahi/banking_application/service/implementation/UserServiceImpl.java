package org.arbahi.banking_application.service.implementation;

import lombok.RequiredArgsConstructor;
import org.arbahi.banking_application.dto.AccountInfo;
import org.arbahi.banking_application.dto.BankResponse;
import org.arbahi.banking_application.dto.UserRequest;
import org.arbahi.banking_application.entity.User;
import org.arbahi.banking_application.repository.UserRepository;
import org.arbahi.banking_application.service.UserService;
import org.arbahi.banking_application.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode("404")
                    .responseMessage("User Already Has Account in this Bank")
                    .accountInfo(null)
                    .build();
        }
        User newUser= User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser=userRepository.save(newUser);
        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("User Added account Successfully ")
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountName(savedUser.getFirstName()+" " + savedUser.getLastName()+" "+savedUser.getOtherName())
                        .accountNumber(savedUser.getAccountNumber())
                        .build())
                .build();
    }
}
