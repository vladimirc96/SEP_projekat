package com.sep.bitcoinservice.dto;

public class RateDTO {

    private double rate;

    public RateDTO() {
    }

    public RateDTO(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
