package com.sep.sellers.model;

import com.sep.sellers.dto.ActiveBillingPlanDTO;

import javax.persistence.*;

@Entity
public class ActiveBillingPlan {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column
    private long sellerId;

    @Column
    private String name;

    @Column
    private String issn;

    @Column
    private String currency;

    @Column
    private double amount;

    public ActiveBillingPlan() {
    }

    public ActiveBillingPlan(ActiveBillingPlanDTO d) {
        this.sellerId = d.getSellerId();
        this.amount = d.getAmount();
        this.currency = d.getCurrency();
        this.issn = d.getIssn();
        this.name = d.getName();
    }

    public long getId() {
        return id;
    }

    public long getSellerId() {
        return sellerId;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setSellerId(long seller_id) {
        this.sellerId = seller_id;
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
}
