package com.sep.sellers.model;

import javax.persistence.*;

@Entity
@Table(name = "seller_payment_method")
public class SellerPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private PaymentMethod paymentMethod;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Seller seller;

    @Column(columnDefinition = "boolean default false")
    private boolean registrationSuccess;

    public SellerPaymentMethod() {
    }

    public SellerPaymentMethod(PaymentMethod paymentMethod, Seller seller, boolean registrationSuccess) {
        this.paymentMethod = paymentMethod;
        this.seller = seller;
        this.registrationSuccess = registrationSuccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public boolean isRegistrationSuccess() {
        return registrationSuccess;
    }

    public void setRegistrationSuccess(boolean registrationSuccess) {
        this.registrationSuccess = registrationSuccess;
    }
}
