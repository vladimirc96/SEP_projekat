package com.sep.paymentcardcenter.dto;

import java.util.Date;

public class BankAccountDTO {

    private String pan;
    private String serviceCode;
    private String cardholderName;
    private Date expirationDate;

    public BankAccountDTO() {
    }

    public BankAccountDTO(String pan, String serviceCode, String cardholderName, Date expirationDate) {
        this.pan = pan;
        this.serviceCode = serviceCode;
        this.cardholderName = cardholderName;
        this.expirationDate = expirationDate;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String securityCode) {
        this.serviceCode = securityCode;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
