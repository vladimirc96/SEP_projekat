package com.sep.paypalservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PPClient {

    @Id
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name="client_secret", nullable = false)
    private String clientSecret;

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
}
