package com.sep.bank.dto;


import com.sep.bank.model.PaymentStatus;

import java.util.Date;

public class IssuerResponseDTO {

    private PaymentStatus paymentStatus;
    private Long acquirerOrderId;
    private Date acquirerTimestamp;
    private Long issuerOrderId;
    private Date issuerTimestamp;
    private String message;
    private String issuerUpdateUrl;
    private String pccUpdateUrl;
    private Long pccOrderId;

    public IssuerResponseDTO(){}

    public IssuerResponseDTO(PaymentStatus paymentStatus, Long acquirerOrderId, Date acquirerTimestamp, Long issuerOrderId, Date issuerTimestamp, String message, String issuerUpdateUrl, String pccUpdateUrl, Long pccOrderId) {
        this.paymentStatus = paymentStatus;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.issuerOrderId = issuerOrderId;
        this.issuerTimestamp = issuerTimestamp;
        this.message = message;
        this.issuerUpdateUrl = issuerUpdateUrl;
        this.pccUpdateUrl = pccUpdateUrl;
        this.pccOrderId = pccOrderId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIssuerUpdateUrl() {
        return issuerUpdateUrl;
    }

    public void setIssuerUpdateUrl(String issuerUpdateUrl) {
        this.issuerUpdateUrl = issuerUpdateUrl;
    }

    public String getPccUpdateUrl() {
        return pccUpdateUrl;
    }

    public void setPccUpdateUrl(String pccUpdateUrl) {
        this.pccUpdateUrl = pccUpdateUrl;
    }

    public Long getPccOrderId() {
        return pccOrderId;
    }

    public void setPccOrderId(Long pccOrderId) {
        this.pccOrderId = pccOrderId;
    }
}
