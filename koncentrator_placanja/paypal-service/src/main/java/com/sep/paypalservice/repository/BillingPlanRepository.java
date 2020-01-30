package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.BillingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillingPlanRepository extends JpaRepository<BillingPlan, Long> {

    BillingPlan findOneById(Long id);

    @Query("select b from BillingPlan b where b.sellerId = :id")
    List<BillingPlan> findBySeller(@Param("id") Long id);

    @Query("select b from BillingPlan b where b.planId = :id")
    BillingPlan findByPlanId(String id);
}
