package com.BunnaBank.BunnaBankBackend.Service;

import com.BunnaBank.BunnaBankBackend.Model.Customer;
import com.BunnaBank.BunnaBankBackend.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer) {
        customer.setAccountNumber(generateUniqueAccountNumber());
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(long id, Customer updatedCustomer) {

        Optional<Customer> existingCustomerOptional = customerRepository.findById(id);

        if (existingCustomerOptional.isPresent()) {
            Customer existingCustomer = existingCustomerOptional.get();

            // Update fields
            existingCustomer.setFirstName(updatedCustomer.getFirstName());
            existingCustomer.setMiddleName(updatedCustomer.getMiddleName());
            existingCustomer.setLastName(updatedCustomer.getLastName());
            existingCustomer.setGender(updatedCustomer.getGender());
            existingCustomer.setAge(updatedCustomer.getAge());
            existingCustomer.setAccountNumber(updatedCustomer.getAccountNumber());
            existingCustomer.setClearBalance(updatedCustomer.getClearBalance());

            return customerRepository.save(existingCustomer);
        } else {
            throw new RuntimeException("Customer with ID " + id + " not found.");
        }
    }

    public String deleteCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            customerRepository.delete(customer.get());
            return "Customer deleted successfully";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
    }

    public Page<Customer> getCustomers(Long id, String accountNumber,Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().isSorted() ? pageable.getSort().descending() : Sort.by(Sort.Direction.DESC, "id") // Default sorting by ID
        );

        if (id != null) {
            return customerRepository.findById(id, pageable);
        } else if (accountNumber != null) {
            return customerRepository.findByAccountNumber(accountNumber, sortedPageable);
        }
        return customerRepository.findAll(sortedPageable);
    }

    public Optional<Customer> findCustomer(String accountNumber) {
        return customerRepository.findByAccountNumber(accountNumber);
    }

    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            long number  = 1 + (long) (random.nextDouble() * 9999999999999L);
            DecimalFormat df = new DecimalFormat("0000000000000");
            accountNumber = df.format(number);
        } while (customerRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
