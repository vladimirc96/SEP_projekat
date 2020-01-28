package com.sep.sellers.dto;

import com.sep.sellers.enums.Enums;

public class ActiveOrderDTO {

    private Long id;
    private Long ncOrderId;
    private String title;
    private String currency;
    private Long sellerId;
    private double amount;
    private String returnUrl;
    private Enums.OrderType orderType;
    private Enums.OrderStatus orderStatus;

    public ActiveOrderDTO(Long id, Long ncOrderId, String title, String currency, Long sellerId, double amount, String returnUrl, Enums.OrderType orderType, Enums.OrderStatus orderStatus) {
        this.id = id;
        this.ncOrderId = ncOrderId;
        this.title = title;
        this.currency = currency;
        this.sellerId = sellerId;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNcOrderId() {
        return ncOrderId;
    }

    public void setNcOrderId(Long ncOrderId) {
        this.ncOrderId = ncOrderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Enums.OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(Enums.OrderType orderType) {
        this.orderType = orderType;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
