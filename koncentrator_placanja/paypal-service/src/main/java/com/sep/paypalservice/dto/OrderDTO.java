package com.sep.paypalservice.dto;

public class OrderDTO {

    private double price;
    private String currency;
    private String description;
    private Long id;
    private String name;
    private long activeOrderId;

    public OrderDTO() {

    }

    public OrderDTO(double price, String currency, String description, Long id, String name, long activeOrderId) {
        this.price = price;
        this.currency = currency;
        this.description = description;
        this.id = id;
        this.name = name;
        this.activeOrderId = activeOrderId;
    }

    public long getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(long activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
