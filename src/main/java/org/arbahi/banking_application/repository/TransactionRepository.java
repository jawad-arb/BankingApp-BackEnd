package org.arbahi.banking_application.repository;

import org.arbahi.banking_application.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String>{
}
