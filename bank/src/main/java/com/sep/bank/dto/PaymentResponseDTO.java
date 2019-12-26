package com.sep.bank.dto;

import com.sep.bank.model.PaymentStatus;

import java.util.Date;

public class PaymentResponseDTO {

    private Long merchantOrderId;
    private Long acquirerOrderId;
    private Long paymentId;
    private Date acquirerTimestamp;
    private PaymentStatus paymentStatus;

    public PaymentResponseDTO() { super(); }

    public PaymentResponseDTO(Long merchantOrderId, Long acquirerOrderId, Long paymentId, Date acquirerTimestamp, PaymentStatus paymentStatus) {
        this.merchantOrderId = merchantOrderId;
        this.acquirerOrderId = acquirerOrderId;
        this.paymentId = paymentId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.paymentStatus = paymentStatus;
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

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Date getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public void setAcquirerTimestamp(Date acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
