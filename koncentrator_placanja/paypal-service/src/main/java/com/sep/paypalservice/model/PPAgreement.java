package com.sep.paypalservice.model;

import com.paypal.api.payments.Billing;

import javax.persistence.*;

@Entity
@Table(name = "ppagreement")
public class PPAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String activeAgreementId;

    @Column
    private String startDate;

    @Column
    private String finalPaymentDate;

    @Column
    private String payerEmail;

    @Column
    private String payerId;

    @Column
    private String status;

    @Column
    private String tokenn;

    @Column
    private long sellerId;

    @Column
    private long activeOrderId;

    @Column
    private String username;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BillingPlan billingPlan;

    public PPAgreement() {
    }

    public String getTokenn() {
        return tokenn;
    }

    public void setTokenn(String tokenn) {
        this.tokenn = tokenn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getActiveAgreementId() {
        return activeAgreementId;
    }

    public void setActiveAgreementId(String activeAgreementId) {
        this.activeAgreementId = activeAgreementId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinalPaymentDate() {
        return finalPaymentDate;
    }

    public void setFinalPaymentDate(String finalPaymentDate) {
        this.finalPaymentDate = finalPaymentDate;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BillingPlan getBillingPlan() {
        return billingPlan;
    }

    public void setBillingPlan(BillingPlan billingPlan) {
        this.billingPlan = billingPlan;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public long getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(long activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
