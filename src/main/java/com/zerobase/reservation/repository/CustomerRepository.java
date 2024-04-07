package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
   Optional<Customer> findByEmail(String email);

   Optional<Customer> findByName(String name);

}
