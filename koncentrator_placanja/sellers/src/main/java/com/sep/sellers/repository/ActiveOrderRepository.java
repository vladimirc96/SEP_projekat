package com.sep.sellers.repository;

import com.sep.sellers.model.ActiveOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveOrderRepository extends JpaRepository<ActiveOrder, Long> {
}
