package com.sep.bitcoinservice.dto;

import com.sep.bitcoinservice.enums.Enums;
import com.sep.bitcoinservice.model.Seller;
import com.sep.bitcoinservice.model.Transaction;

import javax.persistence.*;
import java.util.Date;

public class TransactionDTO {

    private long id;
    private long orderId;
    private String status;
    private double amount;
    private double amountDifference;
    private Enums.Currency currency;
    private Date createdAt;
    private String paymentUrl;
    private String paymentAddress;
    private SellerDTO seller;

    public TransactionDTO() {
    }

    public static TransactionDTO formDto(Transaction t) {
        return new TransactionDTO(t.getId(), t.getOrderId(), t.getStatus(), t.getAmount(), t.getAmountDifference(),
                t.getCurrency(), t.getCreatedAt(), t.getPaymentUrl(), t.getPaymentAddress(),
                SellerDTO.formDto(t.getSeller()));
    }

    public TransactionDTO(long id, long orderId, String status, double amount, double amountDifference, Enums.Currency currency, Date createdAt, String paymentUrl, String paymentAddress, SellerDTO seller) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
        this.amountDifference = amountDifference;
        this.currency = currency;
        this.createdAt = createdAt;
        this.paymentUrl = paymentUrl;
        this.paymentAddress = paymentAddress;
        this.seller = seller;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentAddress() {
        return paymentAddress;
    }

    public void setPaymentAddress(String paymentAddress) {
        this.paymentAddress = paymentAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountDifference() {
        return amountDifference;
    }

    public void setAmountDifference(double amountDifference) {
        this.amountDifference = amountDifference;
    }

    public Enums.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Enums.Currency currency) {
        this.currency = currency;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public SellerDTO getSeller() {
        return seller;
    }

    public void setSeller(SellerDTO seller) {
        this.seller = seller;
    }
}
