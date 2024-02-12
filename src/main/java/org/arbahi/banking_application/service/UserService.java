package org.arbahi.banking_application.service;

import org.arbahi.banking_application.dto.*;
import org.springframework.stereotype.Service;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse getBalanceEnquiry(EnquiryRequest enquiryRequest);

    String getNameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitDTO request);

    BankResponse debitAccount(CreditDebitDTO request);

    BankResponse transferOperation(TransferDTO transferDTO);
}
