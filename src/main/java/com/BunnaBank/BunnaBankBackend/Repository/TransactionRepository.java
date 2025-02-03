package com.BunnaBank.BunnaBankBackend.Repository;

import com.BunnaBank.BunnaBankBackend.Model.Customer;
import com.BunnaBank.BunnaBankBackend.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(String accountNumber);
    Page<Transaction> findByAccountNumber(String accountNumber, Pageable pageable);
    Page<Transaction> findByTransactionId(Long transactionId, Pageable pageable);
    Transaction findByTransactionId(Long transactionId);
}
