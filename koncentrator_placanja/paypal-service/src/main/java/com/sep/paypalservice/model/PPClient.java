package com.sep.paypalservice.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class PPClient {

    @Id
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name="client_secret", nullable = false)
    private String clientSecret;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PPTransaction> transactions;

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Set<PPTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<PPTransaction> transactions) {
        this.transactions = transactions;
    }
}
