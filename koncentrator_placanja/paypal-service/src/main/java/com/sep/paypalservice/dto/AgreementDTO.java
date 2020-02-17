package com.sep.paypalservice.dto;

public class AgreementDTO {

    private long id;
    private String startDate;
    private String endDate;
    private String status;
    private String frequency;
    private String freqInterval;
    private String cycles;
    private String amount;
    private String currency;
    private String name;
    private long sellerId;

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        if(frequency.equals("YEAR")) {
            this.frequency = "Godinu/a";
        } else if (frequency.equals("MONTH")) {
            this.frequency = "Mesec/i";
        } else if (frequency.equals("WEEK")) {
            this.frequency = "Nedelju/e";
        } else {
            this.frequency = "Dan/a";
        }
    }

    public String getFreqInterval() {
        return freqInterval;
    }

    public void setFreqInterval(String freqInterval) {
        this.freqInterval = freqInterval;
    }

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
