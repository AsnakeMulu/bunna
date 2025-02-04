package com.BunnaBank.BunnaBankBackend.Controller;
import com.BunnaBank.BunnaBankBackend.DTO.TransactionRequest;
import com.BunnaBank.BunnaBankBackend.DTO.TransactionResponse;
import com.BunnaBank.BunnaBankBackend.Model.Transaction;
import com.BunnaBank.BunnaBankBackend.Service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/add")
    public ResponseEntity<TransactionResponse> addTransaction(@RequestBody TransactionRequest transaction) {
//        try {
            TransactionResponse  savedTransaction = transactionService.addTransaction(transaction);
            return ResponseEntity.ok(savedTransaction);
//        }catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
//        }
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
    public ResponseEntity<Page<TransactionResponse>> getTransactions(@RequestParam(required = false)  Long transactionId ,
                                                             @RequestParam(required = false)  String accountNumber,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionResponse> transactions = transactionService.getTransactions(transactionId, accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

}
