package com.sep.paymentcardcenter.dto;


import com.sep.paymentcardcenter.model.PaymentStatus;

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
