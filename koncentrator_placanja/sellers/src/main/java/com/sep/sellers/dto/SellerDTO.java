package com.sep.sellers.dto;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.sep.sellers.model.Seller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SellerDTO {


    private long id;
    private List<String> paymentMethods = new ArrayList<>();

    public SellerDTO() {
    }

    public static SellerDTO formDto(Seller s) {
        SellerDTO sDTO = new SellerDTO();
        sDTO.setId(s.getId());
        sDTO.setPaymentMethods(s.getPaymentMethods().stream().map(p -> p.getMethod()).collect(Collectors.toList()));
        return sDTO;
    }

    public SellerDTO(long id, List<String> paymentMethods) {
        this.id = id;
        this.paymentMethods = paymentMethods;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
