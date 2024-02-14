package org.arbahi.banking_application.service;

import com.itextpdf.text.DocumentException;
import org.arbahi.banking_application.entity.Transaction;
import org.arbahi.banking_application.exceptions.ListEmptyException;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankStatement {
    List<Transaction> generateStatement(String accountNumber,String startDate,String endDate) throws ListEmptyException, FileNotFoundException, DocumentException;
}
