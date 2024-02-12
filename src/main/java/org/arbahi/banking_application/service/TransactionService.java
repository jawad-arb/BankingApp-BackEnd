package org.arbahi.banking_application.service;

import org.arbahi.banking_application.dto.TransactionDTO;

public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
}
