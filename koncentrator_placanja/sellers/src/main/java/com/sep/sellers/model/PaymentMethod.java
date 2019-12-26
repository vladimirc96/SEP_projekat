package com.sep.sellers.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PaymentMethod {

    @Id
    private String method;

    @ManyToMany(mappedBy = "paymentMethods")
    private Set<Seller> seller = new HashSet<>();

    public PaymentMethod() {
    }

    public PaymentMethod(String method, Set<Seller> seller) {
        this.method = method;
        this.seller = seller;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Set<Seller> getSeller() {
        return seller;
    }

    public void setSeller(Set<Seller> seller) {
        this.seller = seller;
    }
}
