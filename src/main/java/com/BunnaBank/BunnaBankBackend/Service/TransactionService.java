package com.BunnaBank.BunnaBankBackend.Service;

import com.BunnaBank.BunnaBankBackend.Model.Customer;
import com.BunnaBank.BunnaBankBackend.Model.Transaction;
import com.BunnaBank.BunnaBankBackend.Repository.CustomerRepository;
import com.BunnaBank.BunnaBankBackend.Repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public Page<Transaction> getTransactions(Long transactionId, String accountNumber,Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().isSorted() ? pageable.getSort().descending() : Sort.by(Sort.Direction.DESC, "transactionId") // Default sorting by ID
        );

        if (transactionId != null) {
            return transactionRepository.findByTransactionId(transactionId, pageable);
        } else if (accountNumber != null) {
            return transactionRepository.findByAccountNumber(accountNumber, sortedPageable);
        }
        return transactionRepository.findAll(sortedPageable);
    }

//    @Transactional
    public Transaction addTransaction(Transaction transaction) {
//        Customer customer = customerRepository.findByAccountNumber(transaction.getAccountNumber());
//
//        if (customer == null) {
//            throw new RuntimeException("Customer not found with account number: " + transaction.getAccountNumber());
//        }
//
//        // Update Customer Balance
//        if ("DEPOSIT".equalsIgnoreCase(transaction.getTransactionType())) {
//            customer.setClearBalance(customer.getClearBalance().add(transaction.getAmount()));
//        } else if ("WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
//            if (customer.getClearBalance().compareTo(transaction.getAmount()) < 0) {
//                throw new RuntimeException("Insufficient funds");
//            }
//            customer.setClearBalance(customer.getClearBalance().subtract(transaction.getAmount()));
//        } else {
//            throw new RuntimeException("Invalid transaction type");
//        }
//
//        customerRepository.save(customer);
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() ->
                new RuntimeException("Transaction not found")
        );
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }
    public Page<Transaction> getTransactionsByAccountNumber(String accountNumber, Pageable pageable) {
        return transactionRepository.findByAccountNumber(accountNumber, pageable);
    }
}
