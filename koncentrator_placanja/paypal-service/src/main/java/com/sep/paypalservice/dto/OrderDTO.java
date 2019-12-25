package com.sep.paypalservice.dto;

public class OrderDTO {

    private double price;
    private String currency;
    private String intent;
    private String description;

    public OrderDTO() {

    }

    public OrderDTO(double price, String currency, String intent, String description) {
        this.price = price;
        this.currency = currency;
        this.intent = intent;
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getIntent() {
        return intent;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
