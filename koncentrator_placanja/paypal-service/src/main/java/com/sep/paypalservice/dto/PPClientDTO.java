package com.sep.paypalservice.dto;

import com.sep.paypalservice.model.PPClient;

import javax.persistence.Column;

public class PPClientDTO {

    private long id;
    private String password;
    private String clientId;
    private String clientSecret;

    public PPClientDTO() {
    }

    public PPClientDTO(long id, String password, String clientId, String clientSecret) {
        this.id = id;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public static PPClientDTO formDTO(PPClient ppClient){
        return new PPClientDTO(ppClient.getId(), "", "", "");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
