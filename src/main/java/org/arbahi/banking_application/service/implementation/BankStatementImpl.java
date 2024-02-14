package org.arbahi.banking_application.service.implementation;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arbahi.banking_application.dto.EmailDetails;
import org.arbahi.banking_application.entity.Transaction;
import org.arbahi.banking_application.entity.User;
import org.arbahi.banking_application.exceptions.ListEmptyException;
import org.arbahi.banking_application.repository.TransactionRepository;
import org.arbahi.banking_application.repository.UserRepository;
import org.arbahi.banking_application.service.BankStatement;
import org.arbahi.banking_application.service.EmailService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankStatementImpl implements BankStatement {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final static String FILE="src/main/resources/files/file.pdf";
    @Override
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws ListEmptyException, FileNotFoundException, DocumentException {
        // Convert to LocalDate
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isAfter(start.atStartOfDay()) && transaction.getCreatedAt().isBefore(end.plusDays(1).atStartOfDay()))
                .toList();
        if (transactionList.isEmpty()){
            throw new ListEmptyException("You Don't have transaction in the selected Date");
        }
        User user=userRepository.findByAccountNumber(accountNumber);
        Rectangle statementSize=new Rectangle(PageSize.A4);
        Document document=new Document(statementSize);
        log.info("Setting the size of a document");
        OutputStream outputStream=new FileOutputStream(FILE);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        PdfPTable bankInfoTable=new PdfPTable(1);
        PdfPCell bankName=new PdfPCell(new Phrase("Banking Company"));
        bankName.setBorder(1);
        bankName.setBackgroundColor(BaseColor.DARK_GRAY);
        bankName.setPadding(20f);

        PdfPCell bankAddress=new PdfPCell(new Phrase("22250 , AL-Hoceima"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo=new PdfPTable(2);

        PdfPCell customerInfo=new PdfPCell(new Phrase("Start Date :"+startDate));
        customerInfo.setBorder(0);

        PdfPCell statement=new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);

        PdfPCell stopDate=new PdfPCell(new Phrase("End Date :"+endDate));
        stopDate.setBorder(0);

        PdfPCell customerName=new PdfPCell(new Phrase("Customer Name :"+user.getFirstName()+" "+user.getLastName()));
        customerName.setBorder(0);
        PdfPCell space=new PdfPCell();
        PdfPCell space1=new PdfPCell();
        PdfPCell space2=new PdfPCell();
        PdfPCell customerAddress=new PdfPCell(new Phrase("Customer Address :"+user.getStateOfOrigin()));
        customerAddress.setBorder(0);

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(customerName);
        statementInfo.addCell(space);
        statementInfo.addCell(space1);
        statementInfo.addCell(space2);
        statementInfo.addCell(customerAddress);

        PdfPTable transactionsTable=new PdfPTable(4);
        PdfPCell date=new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        bankName.setBackgroundColor(BaseColor.DARK_GRAY);
        PdfPCell transactionType=new PdfPCell(new Phrase("TRANSACTION TYPE"));
        date.setBorder(0);
        bankName.setBackgroundColor(BaseColor.DARK_GRAY);
        PdfPCell transactionAmount=new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        date.setBorder(0);
        bankName.setBackgroundColor(BaseColor.DARK_GRAY);
        PdfPCell status=new PdfPCell(new Phrase("STATUS"));
        date.setBorder(0);
        bankName.setBackgroundColor(BaseColor.DARK_GRAY);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);

        transactionList.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus()));

        });

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);


        document.close();

        /**
         * Send Email with attachement
         */
        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Bank Statement Informations")
                .messageBody("This is thw Attachment for the selected Date From: " +startDate+" To : "+endDate)
                .attachment(FILE)
                .build();
        emailService.sendEmailAlertWithAttachment(emailDetails);



        return transactionList;
    }



}
