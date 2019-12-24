package com.sep.bitcoinservice.dto;

import com.sep.bitcoinservice.enums.Enums;

public class OrderDTO {

    private long sellerId;
    private Enums.Currency currency;
    private double amount;
    private String title;
    private String description;


    public OrderDTO() {
    }

    public OrderDTO(long sellerId, Enums.Currency currency, double amount, String title, String description) {
        this.sellerId = sellerId;
        this.currency = currency;
        this.amount = amount;
        this.title = title;
        this.description = description;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public Enums.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Enums.Currency currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
