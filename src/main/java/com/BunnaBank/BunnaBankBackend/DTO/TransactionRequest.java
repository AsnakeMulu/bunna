package com.BunnaBank.BunnaBankBackend.DTO;

import java.math.BigDecimal;

public class TransactionRequest {
    private BigDecimal amount;
    private String transactionType;
    private String postedBy;
    private String accountNumber; // The customer's account number

    // Getters and Setters
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

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


}
