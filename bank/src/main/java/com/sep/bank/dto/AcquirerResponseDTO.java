package com.sep.bank.dto;

import com.sep.bank.model.PaymentStatus;

import java.util.Date;

public class AcquirerResponseDTO {

    private PaymentStatus paymentStatus;
    private Long acquirerOrderId;
    private Date acquirerTimestamp;
    private String message;
    private String confirmUrl;

    public AcquirerResponseDTO(){}

    public AcquirerResponseDTO(PaymentStatus paymentStatus, Long acquirerOrderId, Date acquirerTimestamp, String message) {
        this.paymentStatus = paymentStatus;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
