package com.sep.bitcoinservice.dto;

import com.sep.bitcoinservice.model.Seller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SellerDTO {

    private long id;
    private String authToken;

    public SellerDTO() {
    }

    public static SellerDTO formDto(Seller s) {
        return new SellerDTO(s.getId(), "");
    }

    public SellerDTO(long id, String authToken) {
        this.id = id;
        this.authToken = authToken;

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


}
