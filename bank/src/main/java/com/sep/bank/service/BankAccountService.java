package com.sep.bank.service;

import com.sep.bank.client.TransactionClient;
import com.sep.bank.dto.*;
import com.sep.bank.model.*;
import com.sep.bank.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class BankAccountService {

    private static final String updateUrl = "https://localhost:8451/bank/transaction-failed";

    public Logging logger = new Logging(this);

    @Autowired
    private BankAccountRepository bankAccountRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionClient transactionClient;

    public boolean isBankSame(Transaction transaction,BankAccount bankAccountAcquirer, BankAccountDTO bankAccountDTO){
        String acquirerBIN = bankAccountAcquirer.getPan().substring(0,5);
        String issuerBIN = bankAccountDTO.getPan().substring(0,5);
        if(!acquirerBIN.equals(issuerBIN)){
            logger.logInfo("INFO: Banka prodavca i bank kupca nisu iste. Prosledjivanje zahteva PCC-u. Transaction: " + transaction.toString() +
                    "; bank account data: " + bankAccountAcquirer.toString());
            return false;
        }
        return true;
    }

    public Transaction acquirerValidateAndReserve(Transaction transaction, BankAccount bankAccount, BankAccountDTO bankAccountDTO, Payment payment){
        logger.logInfo("INFO: Validacija podataka kartice. Transcation: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        try {
            validation(bankAccountDTO, bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
            return transaction;
        }
        logger.logInfo("SUCCESS: Podaci kartice su validni. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());

        logger.logInfo("INFO: Rezervacija sredstava. Transcation: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        try {
            reserveFunds(bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            return transaction;
        }
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction = transactionService.save(transaction);
        logger.logInfo("SUCCESS: Zahtev za placanje uspesno obradjen, sredstva su rezervisana. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return transaction;
    }

    public Transaction acquirerValidate(Transaction transaction, BankAccount bankAccount, BankAccountDTO bankAccountDTO, Payment payment){
        logger.logInfo("INFO: Validacija podataka kartice. Transcation: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        try {
            validation(bankAccountDTO, bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
            return transaction;
        }
        transaction.setPaymentStatus(PaymentStatus.PROCESSING);
        transaction = transactionService.save(transaction);
        logger.logInfo("SUCCESS: Podaci kartice su validni. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return transaction;
    }

    public Transaction acquirerReserveFunds(Transaction transaction, BankAccount bankAccount, BankAccountDTO bankAccountDTO){
        logger.logInfo("INFO: Rezervacija sredstava. Transcation: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        try {
            reserveFunds(bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            return transaction;
        }
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction = transactionService.save(transaction);
        logger.logInfo("SUCCESS: Zahtev za placanje uspesno obradjen, sredstva su rezervisana. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return transaction;
    }


    public ResponseEntity<IssuerResponseDTO> issuerValidateAndReserve(Transaction transaction, BankAccount bankAccount, PccRequestDTO pccRequestDTO){
        BankAccountDTO bankAccountDTO = pccRequestDTO.getBankAccountDTO();
        logger.logInfo("INFO: Validacija podataka kartice. Issuer banka. Transcation: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        try {
            validation(bankAccountDTO, bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), pccRequestDTO.getAcquirerOrderId(),
                    pccRequestDTO.getAcquirerTimepstamp(), transaction.getId(), transaction.getTimestamp(), e.getMessage(), this.updateUrl, "", null), HttpStatus.OK);
        }

        try {
            reserveFunds(bankAccount, transaction);
        } catch (Exception e) {
            logger.logError("ERROR: " + e.getMessage() + ". Transaction: " + transaction.toString());
            e.printStackTrace();
            return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), pccRequestDTO.getAcquirerOrderId(),
                    pccRequestDTO.getAcquirerTimepstamp(), transaction.getId(), transaction.getTimestamp(), e.getMessage(), this.updateUrl, "", null), HttpStatus.OK);
        }

        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction = transactionService.save(transaction);
        logger.logInfo("SUCCESS: Zahtev za placanje uspesno obradjen, sredstva su rezervisana. Issuer banka. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), pccRequestDTO.getAcquirerOrderId(),
                pccRequestDTO.getAcquirerTimepstamp(), transaction.getId(), transaction.getTimestamp(), "SUCCESS", this.updateUrl, "", null), HttpStatus.OK);
    }

    public void validation(BankAccountDTO bankAccountDTO, BankAccount bankAccount, Transaction transaction) throws Exception {
        bankAccount = validate(bankAccountDTO);
        if(bankAccount == null){
            transaction.setPaymentStatus(PaymentStatus.ERROR);
            transaction = transactionService.save(transaction);
           throw new Exception("Podaci koji su uneti za karticu nisu validni.");

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
            transaction.setPaymentStatus(PaymentStatus.INSUFFICIENT_FUNDS);
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
                    ba.setReserved(0);
                    bankAccountRepo.save(ba);
                    logger.logInfo("INFO: Stanje racuna nakon rezervacije: "+ ba.getBalance() + ". Transaction " + t.toString() + "; bank account data: ");
                    timer.cancel();
                }
            }, 10000);
        }
    }

    public void addFunds(BankAccount bankAccount, Transaction transaction){
        bankAccount.setBalance(bankAccount.getBalance() + transaction.getAmount());
        bankAccount = bankAccountRepo.save(bankAccount);
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

    public BankAccountDTO parseDate(BankAccountDTO bankAccountDTO) throws ParseException {
        System.out.println("********************************************** DATE FORMAT **********************************************");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(bankAccountDTO.getExpirationDate());
        System.out.println("DATE: " + date);
        date = date.concat(" 00:00:00");
        System.out.println("DATE AND TIME: " + date);
        System.out.println("********************************************** DATE FORMAT **********************************************");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tempDate = simpleDateFormat.parse(date);
        bankAccountDTO.setExpirationDate(tempDate);
        return bankAccountDTO;
    }
}
