package com.sep.bank.dto;


import com.sep.bank.model.PaymentStatus;

public class PaymentStatusDTO {

    private Long id;
    private PaymentStatus paymentStatus;

    public PaymentStatusDTO(){super();}

    public PaymentStatusDTO(Long id, PaymentStatus paymentStatus) {
        this.id = id;
        this.paymentStatus = paymentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
