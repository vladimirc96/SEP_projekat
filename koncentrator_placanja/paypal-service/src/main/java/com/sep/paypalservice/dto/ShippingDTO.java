package com.sep.paypalservice.dto;

public class ShippingDTO {

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
    private long planId;
    private long id;

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public long getId() {
        return id;
    }

    public long getPlanId() {
        return planId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }
}
