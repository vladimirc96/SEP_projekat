package com.sep.paymentcardcenter.handlers;

import com.sep.paymentcardcenter.model.PaymentStatus;
import com.sep.paymentcardcenter.model.Transaction;
import com.sep.paymentcardcenter.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class InitContextHandler implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TransactionService transactionService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("****************************************");
        System.out.println("PODIZANJE KONTEKSTA APLIKACIJE - PROVERA STATUSA TRANSAKCIJA I TIMESTAMP-a");
        List<Transaction> transactionList = transactionService.findAllByPaymentStatus(PaymentStatus.PROCESSING);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if(!transactionList.isEmpty()) {
            for (Transaction transaction : transactionList) {
                System.out.println("CURRENT DATE: " + formatter.format(date));
                System.out.println("TRANSACTION TIMESTAMP: " + formatter.format(transaction.getTimestamp()));
                long diffInMillies = Math.abs(date.getTime() - transaction.getTimestamp().getTime());
                long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
                System.out.println("RAZLIKA U MINUTIMA: " + diff);
                if (diff >= 10) {
                    transaction.setPaymentStatus(PaymentStatus.FAILURE);
                    transaction = transactionService.save(transaction);
                }
            }
        }
        System.out.println("****************************************");
    }
}
