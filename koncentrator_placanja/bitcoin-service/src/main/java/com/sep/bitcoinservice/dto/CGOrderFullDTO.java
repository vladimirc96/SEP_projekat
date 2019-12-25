package com.sep.bitcoinservice.dto;

import com.sep.bitcoinservice.enums.Enums;

import java.util.Date;

public class CGOrderFullDTO {

    private long id;
    private String status;
    private String payment_address;
    private String payment_url;
    private Date created_at;
    private Date expire_at;
    private double price_amount;
    private Enums.Currency price_currency;
    private Enums.Currency receive_currency;
    private String title;
    private String description;
    private double underpaid_amount;
    private double overpaid_amount;

    public CGOrderFullDTO() {
    }

    public CGOrderFullDTO(long id, String status, String payment_address, String payment_url, Date created_at, Date expire_at, double price_amount, Enums.Currency price_currency, Enums.Currency receive_currency, String title, String description, double underpaid_amount, double overpaid_amount) {
        this.id = id;
        this.status = status;
        this.payment_address = payment_address;
        this.payment_url = payment_url;
        this.created_at = created_at;
        this.expire_at = expire_at;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.receive_currency = receive_currency;
        this.title = title;
        this.description = description;
        this.underpaid_amount = underpaid_amount;
        this.overpaid_amount = overpaid_amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_address() {
        return payment_address;
    }

    public void setPayment_address(String payment_address) {
        this.payment_address = payment_address;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(Date expire_at) {
        this.expire_at = expire_at;
    }

    public double getPrice_amount() {
        return price_amount;
    }

    public void setPrice_amount(double price_amount) {
        this.price_amount = price_amount;
    }

    public Enums.Currency getPrice_currency() {
        return price_currency;
    }

    public void setPrice_currency(Enums.Currency price_currency) {
        this.price_currency = price_currency;
    }

    public Enums.Currency getReceive_currency() {
        return receive_currency;
    }

    public void setReceive_currency(Enums.Currency receive_currency) {
        this.receive_currency = receive_currency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnderpaid_amount() {
        return underpaid_amount;
    }

    public void setUnderpaid_amount(double underpaid_amount) {
        this.underpaid_amount = underpaid_amount;
    }

    public double getOverpaid_amount() {
        return overpaid_amount;
    }

    public void setOverpaid_amount(double overpaid_amount) {
        this.overpaid_amount = overpaid_amount;
    }
}
