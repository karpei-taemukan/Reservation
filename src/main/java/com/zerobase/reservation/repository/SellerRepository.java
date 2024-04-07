package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByEmail(String email);

    Optional<Seller> findByName(String username);
}
