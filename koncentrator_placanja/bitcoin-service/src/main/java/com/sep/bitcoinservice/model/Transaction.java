package com.sep.bitcoinservice.model;

import com.sep.bitcoinservice.dto.CGOrderFullDTO;
import com.sep.bitcoinservice.enums.Enums;
import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction_")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private long orderId;

    @Column
    private String status;

    @Column
    private double amount;

    @Column
    private double amountDifference;

    @Column
    private String paymentUrl;

    @Column
    private String paymentAddress;
    @Column
    @Enumerated(EnumType.STRING)
    private Enums.Currency currency;

    @Column
    private Date createdAt;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Seller seller;

    public Transaction() {
    }

    public Transaction(long orderId, String status, double amount, double amountDifference, String paymentUrl, String paymentAddress, Enums.Currency currency, Date createdAt, Seller seller) {
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
        this.amountDifference = amountDifference;
        this.paymentUrl = paymentUrl;
        this.paymentAddress = paymentAddress;
        this.currency = currency;
        this.createdAt = createdAt;
        this.seller = seller;
    }


    public static Transaction convertObjects(Transaction t, CGOrderFullDTO cgDTO) {

        t.createdAt = cgDTO.getCreated_at();
        t.amount = cgDTO.getPrice_amount();
        t.amountDifference = cgDTO.getOverpaid_amount() + cgDTO.getUnderpaid_amount();
        t.currency = cgDTO.getPrice_currency();
        t.orderId = cgDTO.getId();
        t.status = cgDTO.getStatus();
        t.paymentAddress = cgDTO.getPayment_address();
        t.paymentUrl = cgDTO.getPayment_url();


        return t;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountDifference() {
        return amountDifference;
    }

    public void setAmountDifference(double amountDifference) {
        this.amountDifference = amountDifference;
    }

    public Enums.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Enums.Currency currency) {
        this.currency = currency;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentAddress() {
        return paymentAddress;
    }

    public void setPaymentAddress(String paymentAddress) {
        this.paymentAddress = paymentAddress;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "  id=" + id  +
                ",orderId=" + orderId +
                ",\\n status='" + status + '\'' +
                ",\\n amount=" + amount +
                ",\\n amountDifference=" + amountDifference +
                ",\\n paymentUrl='" + paymentUrl + '\'' +
                ",\\n paymentAddress='" + paymentAddress + '\'' +
                ",\\n currency=" + currency +
                ",\\n createdAt=" + createdAt +
                ",\\n seller=" + seller.getId() +
                '}';
    }
}
