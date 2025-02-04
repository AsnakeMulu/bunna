package com.BunnaBank.BunnaBankBackend.Service;

import com.BunnaBank.BunnaBankBackend.DTO.TransactionRequest;
import com.BunnaBank.BunnaBankBackend.DTO.TransactionResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public Page<TransactionResponse> getTransactions(Long transactionId, String accountNumber,Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().isSorted() ? pageable.getSort().descending() : Sort.by(Sort.Direction.DESC, "transactionId") // Default sorting by ID
        );

        Page<Transaction> transactions;
        if (transactionId != null) {
            transactions = transactionRepository.findByTransactionId(transactionId, pageable);
        } else if (accountNumber != null) {
            transactions = transactionRepository.findByCustomer_AccountNumber(accountNumber, sortedPageable);
        }else {
            transactions = transactionRepository.findAll(sortedPageable);
        }
        return transactions.map(TransactionResponse::new);
    }

//    @Transactional
    public TransactionResponse addTransaction(TransactionRequest request) {
        Customer customer = getValidatedCustomer(request.getAccountNumber());
        // Update Customer Balance
        if ("Credit".equalsIgnoreCase(request.getTransactionType())) {
            customer.setClearBalance(customer.getClearBalance().add(request.getAmount()));
        } else if ("Debit".equalsIgnoreCase(request.getTransactionType())) {
            if (customer.getClearBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds");
            }
            customer.setClearBalance(customer.getClearBalance().subtract(request.getAmount()));
        } else {
            throw new RuntimeException("Invalid transaction type");
        }

        customerRepository.save(customer);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setPostedBy(request.getPostedBy());
        transaction.setCustomer(customer);

        Transaction savedTransaction =  transactionRepository.save(transaction);
        return new TransactionResponse(savedTransaction);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() ->
                new RuntimeException("Transaction not found")
        );
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByCustomer_AccountNumber(accountNumber);
    }
    public Page<Transaction> getTransactionsByAccountNumber(String accountNumber, Pageable pageable) {
        return transactionRepository.findByCustomer_AccountNumber(accountNumber, pageable);
    }

    private Customer getValidatedCustomer(String accountNumber) {
        return customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Customer not found with account number: " + accountNumber));
    }
}
