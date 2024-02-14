package org.arbahi.banking_application.controller;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.arbahi.banking_application.entity.Transaction;
import org.arbahi.banking_application.exceptions.ListEmptyException;
import org.arbahi.banking_application.service.BankStatement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bankStatement")
public class TransactionController {
    private final BankStatement bankStatement;
    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) throws ListEmptyException, DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }
}
