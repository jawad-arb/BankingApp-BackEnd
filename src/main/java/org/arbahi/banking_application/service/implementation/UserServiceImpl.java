package org.arbahi.banking_application.service.implementation;

import lombok.RequiredArgsConstructor;
import org.arbahi.banking_application.dto.*;
import org.arbahi.banking_application.entity.User;
import org.arbahi.banking_application.repository.UserRepository;
import org.arbahi.banking_application.service.EmailService;
import org.arbahi.banking_application.service.TransactionService;
import org.arbahi.banking_application.service.UserService;
import org.arbahi.banking_application.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;
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
        /**
         * Send the Email to the user
         */

        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATED SUCCESSFULLY")
                .messageBody("YOUR ACCOUNT WITH ACCOUNT Name :"+savedUser.getFirstName()+savedUser.getLastName()+" "+savedUser.getOtherName()
                +"\n AND CLIENT Name "+savedUser.getFirstName()+" \n HAS CREATED SUCCESSFULLY")
                                    .build();
        emailService.sendEmailAlert(emailDetails);
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

    @Override
    public BankResponse getBalanceEnquiry(EnquiryRequest enquiryRequest) {
        if (!userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode("404")
                    .responseMessage("No Account exists with this Account Number : "+enquiryRequest.getAccountNumber())
                    .accountInfo(null)
                    .build();
        }
        User foundUser=userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("Account Found with this Account Number")
                .accountInfo(AccountInfo.builder()
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String getNameEnquiry(EnquiryRequest enquiryRequest) {
        if (!userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber()))
            return "No Account exists with this Account Number : "+enquiryRequest.getAccountNumber();
        User foundUser=userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitDTO request) {
        if (!userRepository.existsByAccountNumber(request.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode("404")
                    .responseMessage("No Account exists with this Account Number : "+request.getAccountNumber())
                    .accountInfo(null)
                    .build();
        }
        User userToCredit=userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);
        TransactionDTO transactionDTO=TransactionDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO);
        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("Credit Operation")
                .messageBody("your account : "+userToCredit.getAccountNumber()+" has Debited successfully the amount :"+request.getAmount()
                        +"\n Your Balance now is : "+userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("the account has Credited successfully the amount :"+request.getAmount())
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitDTO request) {
        if (!userRepository.existsByAccountNumber(request.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode("404")
                    .responseMessage("No Account exists with this Account Number : "+request.getAccountNumber())
                    .accountInfo(null)
                    .build();
        }
        User userToDebit=userRepository.findByAccountNumber(request.getAccountNumber());
        if (request.getAmount().compareTo(userToDebit.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode("402")
                    .responseMessage("Insufficient Balance . impossible to make this Debit ")
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(userToDebit);
        TransactionDTO transactionDTO=TransactionDTO.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO);
        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("Credit Operation")
                .messageBody("your account : "+userToDebit.getAccountNumber()+" has Debited successfully the amount :"+request.getAmount()
                        +"\n Your Balance now is : "+userToDebit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("the account has Debited successfully the amount :"+request.getAmount())
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse transferOperation(TransferDTO transferDTO) {
        if (!userRepository.existsByAccountNumber(transferDTO.getCreditedAccountNumber()) && !userRepository.existsByAccountNumber(transferDTO.getDebitedAccountNumber())){
            return BankResponse.builder()
                    .responseCode("404")
                    .responseMessage("No Account exists with this Account Number : "+transferDTO.getCreditedAccountNumber()+" Or "+transferDTO.getDebitedAccountNumber())
                    .accountInfo(null)
                    .build();
        }
        User userToDebit=userRepository.findByAccountNumber(transferDTO.getDebitedAccountNumber());
        User userToCredit=userRepository.findByAccountNumber(transferDTO.getCreditedAccountNumber());
        TransactionDTO transactionDTO=TransactionDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("TRANSFER CREDIT")
                .amount(transferDTO.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO);
        TransactionDTO transactionDTO1=TransactionDTO.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("TRANSFER DEBIT")
                .amount(transferDTO.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO1);
        if (transferDTO.getAmount().compareTo(userToDebit.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode("402")
                    .responseMessage("Insufficient Balance . impossible to make this Debit ")
                    .accountInfo(null)
                    .build();
        }
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(transferDTO.getAmount()));
        userRepository.save(userToDebit);
        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("Transfer Operation")
                .messageBody("your account : "+userToDebit.getAccountNumber()+" has maked a transfer successfully the amount :"+transferDTO.getAmount()+" to the account Number : "+transferDTO.getCreditedAccountNumber()
                        +"\n Your Balance now is : "+userToDebit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails);
        CreditFromUser(userToCredit, transferDTO.getAmount(), transferDTO);
        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("The amount "+transferDTO.getAmount()+"has been transfered Successfully")
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }

    private void CreditFromUser(User userToCredit, BigDecimal amount, TransferDTO transferDTO) {
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(amount));
        userRepository.save(userToCredit);
        EmailDetails emailDetails1= EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("transfer Operation")
                .messageBody(" the user with account number : "+transferDTO.getDebitedAccountNumber()+"\n has maked a transfer to your account : "+userToCredit.getAccountNumber()+" With the amount :"+ amount
                        +"\n Your Balance now is : "+userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetails1);
    }
}
