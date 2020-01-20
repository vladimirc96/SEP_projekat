package com.sep.paypalservice.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pptransactions")
public class PPTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String orderId;

    @Column
    private String payerId;

    @Column
    private String payerEmail;

    @Column
    private String currency;

    @Column
    private double amount;

    @Column
    private String status;

    @Column
    private String payee;

    @Column
    private String createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private PPClient client;

    public PPTransaction() {
    }

    public PPTransaction(String orderId, String payerId, String payerEmail, String currency, double amount, String status, String payee, String createdAt) {
        this.orderId = orderId;
        this.payerId = payerId;
        this.payerEmail = payerEmail;
        this.currency = currency;
        this.amount = amount;
        this.status = status;
        this.payee = payee;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getPayee() {
        return payee;
    }

    public String getCreatedAt() { return createdAt; }

    public void setId(long id) {
        this.id = id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStatus(String state) {
        this.status = state;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public PPClient getClient() {
        return client;
    }

    public void setClient(PPClient client) {
        this.client = client;
    }
}