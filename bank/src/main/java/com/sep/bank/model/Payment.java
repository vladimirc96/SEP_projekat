package com.sep.bank.model;

import javax.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "merchant_order_id")
    private Long merchantOrderId;

    @Column(name = "acquirer_order_id")
    private Long acquirerOrderId;

    @Column(name = "issuer_order_id")
    private Long issuerOrderId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "acquirer_url")
    private String acquirerUrl;

    @Column(name = "issuer_url")
    private String issuerUrl;

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAcquirerOrderId() {
        return acquirerOrderId;
    }

    public void setAcquirerOrderId(Long acquirerOrderId) {
        this.acquirerOrderId = acquirerOrderId;
    }

    public Long getIssuerOrderId() {
        return issuerOrderId;
    }

    public void setIssuerOrderId(Long issuerOrderId) {
        this.issuerOrderId = issuerOrderId;
    }

    public String getAcquirerUrl() {
        return acquirerUrl;
    }

    public void setAcquirerUrl(String acquirerUrl) {
        this.acquirerUrl = acquirerUrl;
    }

    public String getIssuerUrl() {
        return issuerUrl;
    }

    public void setIssuerUrl(String issuerUrl) {
        this.issuerUrl = issuerUrl;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(Long merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }
}
