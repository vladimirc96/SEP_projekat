package com.sep.bankservice.dto;

public class RedirectDTO {

    private String url;
    private Long paymentId;

    public RedirectDTO(){}

    public RedirectDTO(String url, Long paymentId) {
        this.url = url;
        this.paymentId = paymentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
