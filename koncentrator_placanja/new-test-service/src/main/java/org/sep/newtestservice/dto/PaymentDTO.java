package org.sep.newtestservice.dto;

import org.sep.newtestservice.model.Transaction;

import java.util.Date;

public class PaymentDTO {

    private long id;
    private long sellerId;
    private long activeOrderId;
    private double amount;
    private String currency;
    private String status;
    private Date createdAt;

    public PaymentDTO(long id, long sellerId, long activeOrderId, double amount, String currency, String status,
                      Date createdAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.activeOrderId = activeOrderId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static PaymentDTO formDto(Transaction t) {
        if (t != null) {
            return new PaymentDTO(t.getId(), t.getSeller().getId(), t.getActiveOrderId(), t.getAmount(),
                    t.getCurrency(),
                    t.getStatus(),
                    t.getCreatedAt());
        } else {
            return null;
        }


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(long activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }
}
