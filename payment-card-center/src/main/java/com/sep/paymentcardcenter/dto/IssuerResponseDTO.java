package com.sep.paymentcardcenter.dto;

import com.sep.paymentcardcenter.model.PaymentStatus;

import java.util.Date;

public class IssuerResponseDTO {

    private PaymentStatus paymentStatus;
    private Long acquirerOrderId;
    private Date acquirerTimestamp;
    private Long issuerOrderId;
    private Date issuerTimestamp;

    public IssuerResponseDTO(){}


    public IssuerResponseDTO(PaymentStatus paymentStatus, Long acquirerOrderId, Date acquirerTimestamp, Long issuerOrderId, Date issuerTimestamp) {
        this.paymentStatus = paymentStatus;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.issuerOrderId = issuerOrderId;
        this.issuerTimestamp = issuerTimestamp;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getAcquirerOrderId() {
        return acquirerOrderId;
    }

    public void setAcquirerOrderId(Long acquirerOrderId) {
        this.acquirerOrderId = acquirerOrderId;
    }

    public Date getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public void setAcquirerTimestamp(Date acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public Long getIssuerOrderId() {
        return issuerOrderId;
    }

    public void setIssuerOrderId(Long issuerOrderId) {
        this.issuerOrderId = issuerOrderId;
    }

    public Date getIssuerTimestamp() {
        return issuerTimestamp;
    }

    public void setIssuerTimestamp(Date issuerTimestamp) {
        this.issuerTimestamp = issuerTimestamp;
    }
}
