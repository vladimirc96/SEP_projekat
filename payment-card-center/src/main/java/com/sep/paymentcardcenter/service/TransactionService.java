package com.sep.paymentcardcenter.service;

import com.sep.paymentcardcenter.dto.IssuerResponseDTO;
import com.sep.paymentcardcenter.dto.PccRequestDTO;
import com.sep.paymentcardcenter.model.PaymentStatus;
import com.sep.paymentcardcenter.model.Transaction;
import com.sep.paymentcardcenter.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository  transactionRepo;

    public Transaction findOneById(Long id){
        return transactionRepo.findOneById(id);
    }

    public List<Transaction> findAll(){
        return transactionRepo.findAll();
    }

    public Transaction save(Transaction transaction){
        return transactionRepo.save(transaction);
    }

    public void remove(Transaction transaction){
        transactionRepo.delete(transaction);
    }

    public Transaction findOneByPaymentId(Long id){
        return transactionRepo.findOneByPaymentId(id);
    }

    public Transaction create(PccRequestDTO pccRequestDTO, Long paymentId){
        Transaction transaction = new Transaction();
        transaction.setTimestamp(new Date());
        transaction.setAmount(pccRequestDTO.getAmount());
        transaction.setPaymentStatus(pccRequestDTO.getPaymentStatus());
        transaction.setPaymentId(paymentId);
        transaction = transactionRepo.save(transaction);
        return  transaction;
    }

    public Transaction update(IssuerResponseDTO issuerResponseDTO, Transaction transaction){
        transaction.setPaymentStatus(issuerResponseDTO.getPaymentStatus());
        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    public List<Transaction> findAllByPaymentStatus(PaymentStatus paymentStatus){
        return transactionRepo.findAllByPaymentStatus(paymentStatus);
    }

}
