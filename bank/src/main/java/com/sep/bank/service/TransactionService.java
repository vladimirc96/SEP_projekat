package com.sep.bank.service;

import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.PaymentStatus;
import com.sep.bank.model.Transaction;
import com.sep.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BankAccountService bankAccountService;

    public Transaction executePayment(Transaction transaction, BankAccount bankAccount){
        // skinuti sredstva sa racuna
        //bankAccount.setBalance(bankAccount.getBalance()-bankAccount.getReserved());
        //bankAccount = bankAccountService.save(bankAccount);
        // obraditi transakciju i proslediti podatke MERCHANT_ORDER_ID, ACQUIRER_ORDER_ID, ACQUIRER_TIMESTAMP i PAYMENT_ID
        // ne salju se za sada svi ti podaci posto nema Issuer banke
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    public Transaction findOneById(Long id){
        return transactionRepo.findOneById(id);
    }

    public List<Transaction> findAll(){
        return transactionRepo.findAll();
    }

    public Transaction save(Transaction transaction){
        return transactionRepo.save(transaction);
    }

    public void remove(Long id){
        transactionRepo.deleteById(id);
    }

}
