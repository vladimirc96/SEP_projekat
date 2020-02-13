package org.sep.newtestservice.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "nts_seller")
public class Seller {

    @Id
    private long id;

    @Column
    private String someToken;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Set<Transaction> transactions = new HashSet<>();

    public Seller() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSomeToken() {
        return someToken;
    }

    public void setSomeToken(String someToken) {
        this.someToken = someToken;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
