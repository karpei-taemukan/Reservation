package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    Optional<Reserve> findByCustomerNameAndStoreName(String customerName, String storeName);
}
