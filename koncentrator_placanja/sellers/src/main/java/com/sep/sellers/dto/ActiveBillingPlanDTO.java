package com.sep.sellers.dto;

import com.sep.sellers.model.ActiveBillingPlan;

public class ActiveBillingPlanDTO {

    private Long id;
    private String name;
    private String issn;
    private String currency;
    private double amount;

    public ActiveBillingPlanDTO(Long id, String name, String issn, String currency, double amount) {
        this.id = id;
        this.name = name;
        this.issn = issn;
        this.currency = currency;
        this.amount = amount;
    }

    public ActiveBillingPlanDTO(ActiveBillingPlan a) {
        this(a.getId(), a.getName(), a.getIssn(), a.getCurrency(), a.getAmount());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIssn() {
        return issn;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }
}
