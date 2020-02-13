package com.sep.sellers.dto;

import com.sep.sellers.model.PaymentMethod;
import com.sep.sellers.model.SellerPaymentMethod;

public class PaymentMethodDTO {

    private long id;
    private String name;
    private String registrationLink;
    private String serviceBaseUrl;
    private boolean registerSuccess;

    public PaymentMethodDTO() {
    }

    public PaymentMethodDTO(long id, String name, String registrationLink, String serviceBaseUrl, boolean registerSuccess) {
        this.id = id;
        this.name = name;
        this.registrationLink = registrationLink;
        this.serviceBaseUrl = serviceBaseUrl;
        this.registerSuccess = registerSuccess;
    }

    public static PaymentMethodDTO formDto(SellerPaymentMethod pm) {
        if (pm == null) {
            return new PaymentMethodDTO();
        } else {
            PaymentMethodDTO pmDTO = new PaymentMethodDTO(pm.getPaymentMethod().getId(),
                    pm.getPaymentMethod().getName(),
                    pm.getPaymentMethod().getRegistrationUrl(),
                    pm.getPaymentMethod().getServiceBaseUrl(),
                    pm.isRegistrationSuccess());
            return pmDTO;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRegisterSuccess() {
        return registerSuccess;
    }

    public void setRegisterSuccess(boolean registerSuccess) {
        this.registerSuccess = registerSuccess;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegistrationLink() {
        return registrationLink;
    }

    public void setRegistrationLink(String registrationLink) {
        this.registrationLink = registrationLink;
    }

    public String getServiceBaseUrl() {
        return serviceBaseUrl;
    }

    public void setServiceBaseUrl(String serviceBaseUrl) {
        this.serviceBaseUrl = serviceBaseUrl;
    }
}
