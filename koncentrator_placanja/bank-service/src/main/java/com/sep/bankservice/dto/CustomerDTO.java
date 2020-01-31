package com.sep.bankservice.dto;

import com.sep.bankservice.model.Customer;

import javax.persistence.Column;

public class CustomerDTO {

    private long id;
    private String password;
    private String merchantId;
    private String merchantPassword;
    private String name;

    public CustomerDTO() {
    }

    public CustomerDTO(long id, String password, String merchantId, String merchantPassword, String name) {
        this.id = id;
        this.password = password;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.name = name;
    }

    public static CustomerDTO formDTO(Customer customer){
        return new CustomerDTO(customer.getId(), "", "", "", customer.getName());
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
