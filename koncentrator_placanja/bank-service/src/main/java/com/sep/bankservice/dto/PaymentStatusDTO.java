package com.sep.bankservice.dto;


import com.sep.bankservice.model.PaymentStatus;

public class PaymentStatusDTO {

    private PaymentStatus paymentStatus;

    public PaymentStatusDTO(){super();}

    public PaymentStatusDTO(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
