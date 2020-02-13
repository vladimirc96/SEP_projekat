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

    @Column(name = "pcc_order_id")
    private Long pccOrderId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "return_url")
    private String returnUrl;

    @Column(name = "pcc_update_url")
    private String pccUrlUpdate;

    @Column(name = "issuer_update_url")
    private String issuerUpdateUrl;


    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(Long merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
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

    public Long getPccOrderId() {
        return pccOrderId;
    }

    public void setPccOrderId(Long pccOrderId) {
        this.pccOrderId = pccOrderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getPccUrlUpdate() {
        return pccUrlUpdate;
    }

    public void setPccUrlUpdate(String pccUrlUpdate) {
        this.pccUrlUpdate = pccUrlUpdate;
    }

    public String getIssuerUpdateUrl() {
        return issuerUpdateUrl;
    }

    public void setIssuerUpdateUrl(String issuerUpdateUrl) {
        this.issuerUpdateUrl = issuerUpdateUrl;
    }
}
