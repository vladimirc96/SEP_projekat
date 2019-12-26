package com.sep.bitcoinservice.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "seller_")
public class Seller {

    @Id
    private long id;

    @Column
    private String authToken;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Set<Transaction> transactions = new HashSet<>();

    public Seller() {
    }

    public Seller(String authToken, Set<Transaction> transactions) {
        this.authToken = authToken;
        this.transactions = transactions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
