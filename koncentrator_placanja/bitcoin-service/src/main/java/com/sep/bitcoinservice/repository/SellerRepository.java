package com.sep.bitcoinservice.repository;

import com.sep.bitcoinservice.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
