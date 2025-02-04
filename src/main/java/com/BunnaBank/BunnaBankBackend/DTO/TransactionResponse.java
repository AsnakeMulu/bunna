package com.BunnaBank.BunnaBankBackend.DTO;

import com.BunnaBank.BunnaBankBackend.Model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class TransactionResponse {
    private Long transactionId;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime createdDate;
    private String postedBy;
//    private Long customerId;
    private Long accountNumber;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postBy) {
        this.postedBy = postBy;
    }

//    public Long getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(Long customerId) {
//        this.customerId = customerId;
//    }


    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.amount = transaction.getAmount();
        this.transactionType = transaction.getTransactionType();
        this.createdDate = transaction.getCreatedDate();
        this.postedBy = transaction.getPostedBy();
//        this.customerId = transaction.getCustomer() != null ? transaction.getCustomer().getCustomerId() : null;
        this.accountNumber = transaction.getCustomer() != null ? Long.valueOf(transaction.getCustomer().getAccountNumber()) : null;
    }
}
