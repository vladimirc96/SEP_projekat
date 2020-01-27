package com.sep.paypalservice.model;

import javax.persistence.*;

@Entity
public class BillingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_time", nullable = false)
    private String createdAt;

    @Column(name = "updated_time", nullable = false)
    private String updatedAt;

    @Column(name = "description")
    private String description;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "freqInterval")
    private String freqInterval;

    @Column(name = "cycles")
    private String cycles;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amountStart")
    private double amountStart;

    @Column(name = "seller_id")
    private long seller_id;


    public BillingPlan() {
    }

    public BillingPlan(String planId, String name, String state, String createdAt, String updatedAt, String description, String frequency, String freqInterval, String cycles, double amount, String currency, double amountStart, long seller_id) {
        this.planId = planId;
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.frequency = frequency;
        this.freqInterval = freqInterval;
        this.cycles = cycles;
        this.amount = amount;
        this.currency = currency;
        this.amountStart = amountStart;
        this.seller_id = seller_id;
    }

    public long getId() {
        return id;
    }

    public String getPlanId() {
        return planId;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getFreqInterval() {
        return freqInterval;
    }

    public String getCycles() {
        return cycles;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmountStart() {
        return amountStart;
    }

    public long getSeller_id() {
        return seller_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setFreqInterval(String freqInterval) {
        this.freqInterval = freqInterval;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmountStart(double amountStart) {
        this.amountStart = amountStart;
    }

    public void setSeller_id(long seller_id) {
        this.seller_id = seller_id;
    }
}
