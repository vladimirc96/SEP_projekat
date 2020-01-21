package com.sep.paymentcardcenter.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Bank bank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
