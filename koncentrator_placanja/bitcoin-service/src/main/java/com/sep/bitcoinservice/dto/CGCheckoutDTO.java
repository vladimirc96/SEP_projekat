package com.sep.bitcoinservice.dto;

import com.sep.bitcoinservice.enums.Enums;

public class CGCheckoutDTO {

    private Enums.Currency pay_currency;

    public CGCheckoutDTO() {
    }

    public CGCheckoutDTO(Enums.Currency pay_currency) {
        this.pay_currency = pay_currency;
    }

    public Enums.Currency getPay_currency() {
        return pay_currency;
    }

    public void setPay_currency(Enums.Currency pay_currency) {
        this.pay_currency = pay_currency;
    }
}
