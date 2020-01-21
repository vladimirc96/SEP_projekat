package com.sep.bank.service;

import com.sep.bank.dto.BankAccountDTO;
import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.*;
import com.sep.bank.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Service
public class BankAccountService {

    public Logging logger = new Logging(this);

    @Autowired
    private BankAccountRepository bankAccountRepo;

    @Autowired
    private TransactionService transactionService;



    public void validation(BankAccountDTO bankAccountDTO, BankAccount bankAccount, Transaction transaction) throws Exception {
        // provera ostalih podataka
        bankAccount = validate(bankAccountDTO);
        if(bankAccount == null){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionService.save(transaction);
           throw new Exception("Podaci koji su uneti za karticu nisu validni.");
        }
        // proveriti da li je istekla kartica
        if(isExpired(bankAccountDTO.getExpirationDate())){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionService.save(transaction);
            throw new Exception("Kartica je istekla.");
        }
    }

    public void reserveFunds(BankAccount bankAccount, Transaction transaction) throws Exception {
        final BankAccount ba = bankAccount;
        //provera raspolozivih sredstava
        if(!hasFunds(bankAccount.getBalance(), transaction.getAmount())){
            transaction.setPaymentStatus(PaymentStatus.INSUFFCIENT_FUNDS);
            transaction = transactionService.save(transaction);
            throw new Exception("Nedovoljno sredstava.");
        }else{
            // rezervisi sredstva
            bankAccount.setReserved(transaction.getAmount());
            bankAccount = bankAccountRepo.save(bankAccount);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("****************************************");
                    System.out.println("STANJE RACUNA: " + ba.getBalance());
                    System.out.println("REZERVISANA SREDSTVA: " + ba.getReserved());
                    ba.setBalance(ba.getBalance()-ba.getReserved());
                    bankAccountRepo.save(ba);
                    System.out.println("SREDSTVA SKINUTA SA RACUNA: " + ba.getBalance());
                    System.out.println("****************************************");
                    timer.cancel();
                }
            }, 10000);
        }
    }

    public BankAccount findOneById(Long id){
        return bankAccountRepo.findOneById(id);
    }

    public List<BankAccount> findAll(){
        return bankAccountRepo.findAll();
    }

    public BankAccount save(BankAccount bank){
        return bankAccountRepo.save(bank);
    }

    public void remove(Long id){
        bankAccountRepo.deleteById(id);
    }

    public BankAccount validate(BankAccountDTO bankAccountDTO){
        return bankAccountRepo.validate(bankAccountDTO.getPan(), bankAccountDTO.getServiceCode(),
                bankAccountDTO.getCardholderName(), bankAccountDTO.getExpirationDate());
    }

    public BankAccount findOneByPan(String pan){
        return bankAccountRepo.findOneByPan(pan);
    }


    public boolean isExpired(Date expirationDate){
        Date today = new Date();
        if(today.after(expirationDate)){
            return true;
        }else{
            return false;
        }
    }

    public boolean hasFunds(double balance, double amount){
        if(balance >= amount){
            return true;
        }else{
            return false;
        }
    }

}
