package com.sep.sellers.model;

import com.sep.sellers.enums.Enums;

import javax.persistence.*;

@Entity
public class ActiveOrder {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column
    private long nc_order_id;

    @Column
    private String title;

    @Column
    private String currency;

    @Column
    private long seller_id;

    @Column
    private double amount;

    @Column
    private String return_url;

    @Column
    @Enumerated(EnumType.STRING)
    private Enums.OrderType orderType;

    @Column
    @Enumerated(EnumType.STRING)
    private Enums.OrderStatus orderStatus;

    public ActiveOrder() {
    }

    public long getId() {
        return id;
    }

    public long getNc_order_id() {
        return nc_order_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCurrency() {
        return currency;
    }

    public long getSeller_id() {
        return seller_id;
    }

    public double getAmount() {
        return amount;
    }

    public String getReturn_url() {
        return return_url;
    }

    public Enums.OrderType getOrderType() {
        return orderType;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNc_order_id(long nc_order_id) {
        this.nc_order_id = nc_order_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSeller_id(long seller_id) {
        this.seller_id = seller_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public void setOrderType(Enums.OrderType orderType) {
        this.orderType = orderType;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
