package com.sep.sellers.dto;

import com.sep.sellers.enums.Enums;

public class FinalizeOrderDTO {

    private long activeOrderId;
    private Enums.OrderStatus orderStatus;
    private long ncOrderId;

    public FinalizeOrderDTO() {
    }

    public FinalizeOrderDTO(long activeOrderId, Enums.OrderStatus orderStatus, long ncOrderId) {
        this.activeOrderId = activeOrderId;
        this.orderStatus = orderStatus;
        this.ncOrderId = ncOrderId;
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
}
