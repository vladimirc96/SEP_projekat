package com.sep.paypalservice.dto;

public class OrderDTO {

    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;

    public OrderDTO() {

    }

    public OrderDTO(double price, String currency, String method, String intent, String description) {
        this.price = price;
        this.currency = currency;
        this.method = method;
        this.intent = intent;
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMethod() {
        return method;
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

    public void setMethod(String method) {
        this.method = method;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
