package com.bms.repository;

import com.bms.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    List<Transaction> findBySourceAccountNumber(String accountNumber);
    
    List<Transaction> findByDestinationAccountNumber(String accountNumber);
    
    Transaction findByTransactionId(String transactionId);
    
    List<Transaction> findBySourceAccountNumberOrDestinationAccountNumberOrderByTimestampDesc(
            String sourceAccountNumber, String destinationAccountNumber);
            
    List<Transaction> findBySourceAccountNumberOrderByTimestampDesc(String accountNumber);
    
    List<Transaction> findByDestinationAccountNumberOrderByTimestampDesc(String accountNumber);
} 