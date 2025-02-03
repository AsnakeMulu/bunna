package com.BunnaBank.BunnaBankBackend.Repository;

import com.BunnaBank.BunnaBankBackend.Model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @NonNull
    Page<Customer> findAll(@NonNull Pageable pageable);
    Page<Customer> findById(Long id, Pageable pageable);
    Customer findByAccountNumber(String accountNumber);
    Page<Customer> findByAccountNumber(String accountNumber, Pageable pageable);
    boolean existsByAccountNumber(String accountNumber);
}
