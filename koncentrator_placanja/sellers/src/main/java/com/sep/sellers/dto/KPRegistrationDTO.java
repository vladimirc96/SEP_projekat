package com.sep.sellers.dto;

public class KPRegistrationDTO {

    private long sellerId;
    private String registrationStatusCallbackUrl;
    private String registrationPageRedirectUrl;
    private boolean status;


    public KPRegistrationDTO() {

    }

    public KPRegistrationDTO(long sellerId, String registrationStatusCallbackUrl, String registrationPageRedirectUrl, boolean status) {
        this.sellerId = sellerId;
        this.registrationStatusCallbackUrl = registrationStatusCallbackUrl;
        this.registrationPageRedirectUrl = registrationPageRedirectUrl;
        this.status = status;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getRegistrationStatusCallbackUrl() {
        return registrationStatusCallbackUrl;
    }

    public void setRegistrationStatusCallbackUrl(String registrationStatusCallbackUrl) {
        this.registrationStatusCallbackUrl = registrationStatusCallbackUrl;
    }

    public String getRegistrationPageRedirectUrl() {
        return registrationPageRedirectUrl;
    }

    public void setRegistrationPageRedirectUrl(String registrationPageRedirectUrl) {
        this.registrationPageRedirectUrl = registrationPageRedirectUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
