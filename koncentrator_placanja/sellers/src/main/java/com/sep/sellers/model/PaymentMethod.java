package com.sep.sellers.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String registrationUrl;

    @Column
    private String serviceBaseUrl;

    @OneToMany(mappedBy = "paymentMethod", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SellerPaymentMethod> sellerPaymentMethods = new ArrayList<>();

    public PaymentMethod() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SellerPaymentMethod> getSellerPaymentMethods() {
        return sellerPaymentMethods;
    }

    public void setSellerPaymentMethods(List<SellerPaymentMethod> sellerPaymentMethods) {
        this.sellerPaymentMethods = sellerPaymentMethods;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegistrationUrl() {
        return registrationUrl;
    }

    public void setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
    }

    public String getServiceBaseUrl() {
        return serviceBaseUrl;
    }

    public void setServiceBaseUrl(String serviceBaseUrl) {
        this.serviceBaseUrl = serviceBaseUrl;
    }
}
