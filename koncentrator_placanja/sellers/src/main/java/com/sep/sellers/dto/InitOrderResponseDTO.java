package com.sep.sellers.dto;

public class InitOrderResponseDTO {

    private String redirectUrl;

    public InitOrderResponseDTO(){}

    public InitOrderResponseDTO(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
