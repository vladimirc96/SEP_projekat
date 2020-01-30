package com.sep.paypalservice.model;

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
    private String planId;

    @Column
    private String tokenn;

    @Column
    private long sellerId;

    @Column
    private long activeOrderId;

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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
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
}
