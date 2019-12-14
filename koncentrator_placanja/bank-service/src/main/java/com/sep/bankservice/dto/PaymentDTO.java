package com.sep.bankservice.dto;

public class PaymentDTO {

    private String merchantId;
    private double amount;

    public PaymentDTO(String merchantId, double amount) {
        this.merchantId = merchantId;
        this.amount = amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
