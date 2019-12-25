package com.sep.bitcoinservice.dto;

import com.sep.bitcoinservice.enums.Enums;

public class TransactionStatusDTO {

    private long id;
    private String status;
    private double amountDifference;

    public TransactionStatusDTO() {
    }

    public TransactionStatusDTO(long id, String status, double amountDifference) {
        this.id = id;
        this.status = status;
        this.amountDifference = amountDifference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmountDifference() {
        return amountDifference;
    }

    public void setAmountDifference(double amountDifference) {
        this.amountDifference = amountDifference;
    }
}
