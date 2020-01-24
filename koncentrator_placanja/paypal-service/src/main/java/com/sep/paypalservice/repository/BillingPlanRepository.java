package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.BillingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingPlanRepository extends JpaRepository<BillingPlan, Long> {

    BillingPlan findOneById(Long id);
}
