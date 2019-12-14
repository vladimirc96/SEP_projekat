package com.sep.bankservice.model;

import java.util.Date;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    @Column(name = "pan")
    private String pan;

    @Column(name = "cardholder_name")
    private String cardholderName;

    @Column(name = "expirationDate")
    private Date expirationDate;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "balance")
    private double balance;

    @Column(name = "reserved")
    private double reserved;

    @OneToOne
    @JoinColumn(referencedColumnName = "client_id", name="bank_account_client_id")
    private Client client;

    public BankAccount(String pan, String cardholderName, Date expirationDate, String serviceCode, double balance, double reserved, Client client) {
        this.pan = pan;
        this.cardholderName = cardholderName;
        this.expirationDate = expirationDate;
        this.serviceCode = serviceCode;
        this.balance = balance;
        this.reserved = reserved;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
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

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getReserved() {
        return reserved;
    }

    public void setReserved(double reserved) {
        this.reserved = reserved;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
