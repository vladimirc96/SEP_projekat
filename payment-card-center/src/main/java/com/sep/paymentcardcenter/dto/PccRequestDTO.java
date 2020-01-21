package com.sep.paymentcardcenter.dto;

import com.sep.paymentcardcenter.model.PaymentStatus;

import java.util.Date;

public class PccRequestDTO {

    private Long acquirerOrderId;
    private Date acquirerTimepstamp;
    private double amount;
    private PaymentStatus paymentStatus;
    private BankAccountDTO bankAccountDTO;

    public PccRequestDTO(Long acquirerOrderId, Date acquirerTimepstamp, double amount, PaymentStatus paymentStatus, BankAccountDTO bankAccountDTO) {
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimepstamp = acquirerTimepstamp;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.bankAccountDTO = bankAccountDTO;
    }

    public Long getAcquirerOrderId() {
        return acquirerOrderId;
    }

    public void setAcquirerOrderId(Long acquirerOrderId) {
        this.acquirerOrderId = acquirerOrderId;
    }

    public Date getAcquirerTimepstamp() {
        return acquirerTimepstamp;
    }

    public void setAcquirerTimepstamp(Date acquirerTimepstamp) {
        this.acquirerTimepstamp = acquirerTimepstamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BankAccountDTO getBankAccountDTO() {
        return bankAccountDTO;
    }

    public void setBankAccountDTO(BankAccountDTO bankAccountDTO) {
        this.bankAccountDTO = bankAccountDTO;
    }
}