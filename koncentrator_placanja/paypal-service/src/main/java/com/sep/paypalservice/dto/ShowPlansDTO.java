package com.sep.paypalservice.dto;

public class ShowPlansDTO {

    private long id;
    private String name;
    private String frequency;
    private String freqInterval;
    private String cycles;
    private double amount;
    private String currency;
    private double amountStart;

    public ShowPlansDTO(long id, String name, String frequency, String freqInterval, String cycles, double amount, String currency, double amountStart) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.freqInterval = freqInterval;
        this.cycles = cycles;
        this.amount = amount;
        this.currency = currency;
        this.amountStart = amountStart;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmountStart(double amountStart) {
        this.amountStart = amountStart;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmountStart() {
        return amountStart;
    }
}
