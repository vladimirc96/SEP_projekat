package com.sep.sellers.repository;

import com.sep.sellers.model.ActiveBillingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveBillingPlanRepository extends JpaRepository<ActiveBillingPlan, Long> {

    ActiveBillingPlan findOneById(long id);
}

