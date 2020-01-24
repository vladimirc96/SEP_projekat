package com.sep.paypalservice.dto;

public class PlanDTO {

    private String name;
    private String description;
    private String frequency;
    private String freqInterval;
    private String cycles;
    private String amount;
    private String currency;
    private String amountStart;
    private Long merchantId;

    public PlanDTO() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getFreqInterval() {
        return freqInterval;
    }

    public String getCycles() {
        return cycles;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAmountStart() {
        return amountStart;
    }

    public Long getMerchantId() { return merchantId; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setFreqInterval(String freqInterval) {
        this.freqInterval = freqInterval;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmountStart(String amountStart) {
        this.amountStart = amountStart;
    }

    public void setMerchantId(Long id) { this.merchantId = id; }
}
