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

    @Column
    private long activeOrderId;

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

    @Column
    private String paymentToken;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PPClient client;


    public PPTransaction() {
    }

    public PPTransaction(long activeOrderId, String orderId, String payerId, String payerEmail, String currency, double amount, String status, String payee, String createdAt, String token, PPClient client) {
        this.activeOrderId = activeOrderId;
        this.orderId = orderId;
        this.payerId = payerId;
        this.payerEmail = payerEmail;
        this.currency = currency;
        this.amount = amount;
        this.status = status;
        this.payee = payee;
        this.createdAt = createdAt;
        this.paymentToken = token;
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(long activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public PPClient getClient() {
        return client;
    }

    public void setClient(PPClient client) {
        this.client = client;
    }
}