package com.sep.bankservice.model;

import com.sep.bankservice.dto.CustomerDTO;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @Column(name = "client_id")
    private Long id; //seller id

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "merchant_password")
    private String merchantPassword;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    public Customer() {
    }

    public Customer(Long id, String merchantId, String merchantPassword, String name) {
        this.id = id;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.name = name;
    }

    public Customer(CustomerDTO customerDTO){
        this(customerDTO.getId(),customerDTO.getMerchantId(),customerDTO.getMerchantPassword(), customerDTO.getName());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setmerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
