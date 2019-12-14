package com.sep.bankservice.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "merchant_id")
    private String merchanId;

    @Column(name = "merchant_password")
    private String merchantPassword;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    public Client(String merchanId, String merchantPassword, String name) {
        this.merchanId = merchanId;
        this.merchantPassword = merchantPassword;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchanId() {
        return merchanId;
    }

    public void setMerchanId(String merchanId) {
        this.merchanId = merchanId;
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

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
