package com.sep.bitcoinservice.dto;


import com.sep.bitcoinservice.enums.Enums;

public class FinalizeOrderDTO {

    private long activeOrderId;
    private Enums.OrderStatus orderStatus;
    private long ncOrderId;
    private long agreementId;

    public FinalizeOrderDTO() {
    }

    public FinalizeOrderDTO(long activeOrderId, Enums.OrderStatus orderStatus, long ncOrderId, long agreementId) {
        this.activeOrderId = activeOrderId;
        this.orderStatus = orderStatus;
        this.ncOrderId = ncOrderId;
        this.agreementId = agreementId;
    }

    public long getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(long activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getNcOrderId() {
        return ncOrderId;
    }

    public void setNcOrderId(long ncOrderId) {
        this.ncOrderId = ncOrderId;
    }

    public long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(long agreementId) {
        this.agreementId = agreementId;
    }
}
