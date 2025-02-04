package com.BunnaBank.BunnaBankBackend.Controller;

import com.BunnaBank.BunnaBankBackend.Model.Customer;
import com.BunnaBank.BunnaBankBackend.Repository.CustomerRepository;
import com.BunnaBank.BunnaBankBackend.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {
    @Autowired
    private final CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/add")
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @GetMapping
    public Page<Customer> getCustomers(@RequestParam(required = false) Long id,
                                       @RequestParam(required = false) String accountNumber,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerService.getCustomers(id, accountNumber, pageable);
    }

    @GetMapping("/validate-account/{accountNumber}")
    public ResponseEntity<?> validateAccount(@PathVariable String accountNumber) {
        Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);
        if (customer.isPresent()) {
            Customer customerData = customer.get();
            String customerName = customerData.getFirstName() + " "
                    + (customerData.getMiddleName() != null ? customerData.getMiddleName() + " " : "")
                    + customerData.getLastName();
            BigDecimal clearBalance = customerData.getClearBalance();
            Map<String, Object> response = new HashMap<>();
            response.put("customerName", customerName);
            response.put("clearBalance", clearBalance);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Account number not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            String message = customerService.deleteCustomer(id);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("message", message);
            }});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new HashMap<String, String>() {{
                        put("error", e.getMessage());
                    }});
        }
    }
}
