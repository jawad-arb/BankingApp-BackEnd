package org.arbahi.banking_application.service.implementation;

import lombok.RequiredArgsConstructor;
import org.arbahi.banking_application.dto.TransactionDTO;
import org.arbahi.banking_application.entity.Transaction;
import org.arbahi.banking_application.repository.TransactionRepository;
import org.arbahi.banking_application.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction=Transaction.builder()
                .accountNumber(transactionDTO.getAccountNumber())
                .transactionType(transactionDTO.getTransactionType())
                .amount(transactionDTO.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
    }
}
