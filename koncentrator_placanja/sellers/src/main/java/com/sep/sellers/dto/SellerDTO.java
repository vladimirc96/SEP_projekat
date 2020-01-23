package com.sep.sellers.dto;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.sep.sellers.model.Seller;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SellerDTO {


    private long id;
    private List<PaymentMethodDTO> paymentMethods = new ArrayList<>();
    private String email;
    private String password;
    private String name;
    private String organization;

    public SellerDTO() {
    }

    public static SellerDTO formDto(Seller s) {
        if (s == null) {
            return new SellerDTO();
        } else {
            SellerDTO sDTO = new SellerDTO();
            sDTO.setId(s.getId());
            sDTO.setEmail(s.getEmail());
            sDTO.setName(s.getName());
            sDTO.setOrganization(s.getOrganization());
            sDTO.setPaymentMethods(s.getPaymentMethods()
                    .stream()
                    .map(p -> PaymentMethodDTO.formDto(p)).collect(Collectors.toList()));
            return sDTO;
        }
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PaymentMethodDTO> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethodDTO> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }


}
