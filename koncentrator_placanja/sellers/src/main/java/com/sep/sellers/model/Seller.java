package com.sep.sellers.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Seller {


    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "seller_paymentMethod",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "paymentmethod_method")
    )
    private Set<PaymentMethod> paymentMethods = new HashSet<>();


    public Seller() {
    }

    public Seller(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
