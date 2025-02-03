package com.BunnaBank.BunnaBankBackend.Controller;
import com.BunnaBank.BunnaBankBackend.Model.Transaction;
import com.BunnaBank.BunnaBankBackend.Service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }

//    @GetMapping("/{transactionId}")
//    public Transaction getTransactionById(@PathVariable Long transactionId) {
//        return transactionService.getTransactionById(transactionId);
//    }

//    @GetMapping("/{accountNumber}")
//    public List<Transaction> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
//        return transactionService.getTransactionsByAccountNumber(accountNumber);
//    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Page<Transaction>> getTransactionsByAccountNumber(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getTransactions(@RequestParam(required = false)  Long transactionId ,
                                                             @RequestParam(required = false)  String accountNumber,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionService.getTransactions(transactionId, accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

}
