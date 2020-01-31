package com.sep.bankservice.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING) // omogucava cuvanje enum vrednosti kao string u bazi
    private PaymentStatus paymentStatus;

    @Column(name = "active_order_id")
    private Long activeOrderId;

    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Customer customer;

    public Transaction() {
    }

    public Transaction(double amount, Date timestamp, Customer customer) {
        this.amount = amount;
        this.timestamp = timestamp;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(Long activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", paymentStatus=" + paymentStatus +
                ", activeOrderId=" + activeOrderId +
                ", paymentId=" + paymentId +
                ", customer=" + customer +
                '}';
    }
}
