package com.sep.bank.service;

import com.sep.bank.dto.*;
import com.sep.bank.model.*;
import com.sep.bank.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Service
public class BankAccountService {

    public Logging logger = new Logging(this);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepo;

    @Autowired
    private TransactionService transactionService;


    public boolean isBankSame(Transaction transaction, BankAccount bankAccount){
        Customer acquirer = transaction.getCustomer();
        Customer issuer = bankAccount.getCustomer();
        if(issuer.getBankAccount().getBank().getId() != acquirer.getBankAccount().getBank().getId()){
            logger.logInfo("INFO: Banka prodavca i bank kupca nisu iste. Prosledjivanje zahteva PCC-u. Transaction: " + transaction.toString() +
                    "; bank account data: " + bankAccount.toString());
            return false;
        }
        return true;
    }

    public ResponseEntity<AcquirerResponseDTO> acquirerValidateAndReserve(Transaction transaction, BankAccount bankAccount, BankAccountDTO bankAccountDTO){
        logger.logInfo("INFO: Validacija podataka kartice. Transcation: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        try {
            validation(bankAccountDTO, bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(new AcquirerResponseDTO(transaction.getPaymentStatus(), transaction.getId(), new Date(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        try {
            reserveFunds(bankAccount,transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(new AcquirerResponseDTO(transaction.getPaymentStatus(), transaction.getId(), new Date(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        logger.logInfo("SUCCESS: Zahtev za placanje uspesno obradjen, sredstva su rezervisana. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return new ResponseEntity<>(new AcquirerResponseDTO(transaction.getPaymentStatus(), transaction.getId(), new Date(), "Success"), HttpStatus.OK);
    }

    public ResponseEntity<IssuerResponseDTO> issuerValidateAndReserve(Transaction transaction, BankAccount bankAccount, BankAccountDTO bankAccountDTO){
        logger.logInfo("INFO: Validacija podataka kartice. Issuer banka. Transcation: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        try {
            validation(bankAccountDTO, bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            requestUpdateTransactionPcc(transaction);
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), transaction.getId(),
                    transaction.getTimestamp(), transaction.getId(), new Date(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        try {
            reserveFunds(bankAccount,transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            requestUpdateTransactionPcc(transaction);
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), transaction.getId(),
                    transaction.getTimestamp(), transaction.getId(), new Date(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        logger.logInfo("SUCCESS: Zahtev za placanje uspesno obradjen, sredstva su rezervisana. Issuer banka. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), transaction.getId(),
                transaction.getTimestamp(), transaction.getId(), new Date(), "Success"), HttpStatus.OK);
    }

    public void validation(BankAccountDTO bankAccountDTO, BankAccount bankAccount, Transaction transaction) throws Exception {
        // provera ostalih podataka
        bankAccount = validate(bankAccountDTO);
        if(bankAccount == null){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionService.save(transaction);
           throw new Exception("Podaci koji su uneti za karticu nisu validni. Transaction: " + transaction.toString());
        }
        if(isExpired(bankAccountDTO.getExpirationDate())){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionService.save(transaction);
            throw new Exception("Kartica je istekla.");
        }
    }

    public void reserveFunds(BankAccount bankAccount, Transaction transaction) throws Exception {
        final BankAccount ba = bankAccount;
        final Transaction t = transaction;
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
                    logger.logInfo("INFO: Rezervacija sredstava. Transaction " + t.toString() + "; bank account data: ");
                    logger.logInfo("INFO: Stanje racuna:" + ba.getBalance() + ". Transaction " + t.toString() + "; bank account data: ");
                    logger.logInfo("INFO: Rezervisana sredstva: "+ ba.getReserved() + ". Transaction " + t.toString() + "; bank account data: ");
                    ba.setBalance(ba.getBalance()-ba.getReserved());
                    bankAccountRepo.save(ba);
                    logger.logInfo("INFO: Stanje racuna nakon rezervacije: "+ ba.getBalance() + ". Transaction " + t.toString() + "; bank account data: ");
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

    private void requestUpdateTransactionBankService(Transaction transaction){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(transaction.getPaymentStatus()));
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://localhost:8500/bank-service/bank/transaction/" + transaction.getId(),
                HttpMethod.PUT, entity, String.class);
    }
    private void requestUpdateTransactionPcc(Transaction transaction){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(transaction.getPaymentStatus()));
        ResponseEntity<String> responseEntityPcc = restTemplate.exchange("https://localhost:8452/pcc/transaction/" + transaction.getId(),
                HttpMethod.PUT, entity, String.class);
    }

}
