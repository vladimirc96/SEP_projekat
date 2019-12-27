package com.sep.bankservice.dto;

public class PaymentDTO {

    private Long sellerId;
    private double amount;

    public PaymentDTO(Long merchantId, double amount) {
        this.sellerId = merchantId;
        this.amount = amount;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
