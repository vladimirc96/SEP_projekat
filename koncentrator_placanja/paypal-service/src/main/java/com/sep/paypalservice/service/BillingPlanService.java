package com.sep.paypalservice.service;

import com.sep.paypalservice.model.BillingPlan;
import com.sep.paypalservice.repository.BillingPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingPlanService {

    @Autowired
    BillingPlanRepository billingPlanRepository;

    public List<BillingPlan> findAll() {
        return billingPlanRepository.findAll();
    }

    public BillingPlan findOneById(Long id) {
        return billingPlanRepository.findOneById(id);
    }

    public BillingPlan save(BillingPlan plan) {
        return billingPlanRepository.save(plan);
    }

    public void remove(Long id) {
        billingPlanRepository.deleteById(id);
    }

    public List<BillingPlan> findBySeller(long id) {
        return billingPlanRepository.findBySeller(id);
    }

}
