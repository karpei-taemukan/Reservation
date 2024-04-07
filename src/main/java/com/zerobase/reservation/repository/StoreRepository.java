package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findBySellerId(Long id);
    Optional<Store> findByName(String name);
}
