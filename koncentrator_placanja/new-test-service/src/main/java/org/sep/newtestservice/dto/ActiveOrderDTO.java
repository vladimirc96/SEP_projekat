package org.sep.newtestservice.dto;

import org.sep.newtestservice.enums.Enums;

public class ActiveOrderDTO {
    private long id;
    private Enums.OrderStatus orderStatus;
    private long paymentMethodId;

    public ActiveOrderDTO() {
    }

    public ActiveOrderDTO(long id, Enums.OrderStatus orderStatus, long paymentMethodId) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.paymentMethodId = paymentMethodId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
