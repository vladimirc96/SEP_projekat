package com.sep.bank.service;

import com.sep.bank.dto.BankAccountDTO;
import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.Bank;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.Customer;
import com.sep.bank.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {


    @Autowired
    private BankAccountRepository bankAccountRepo;

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

}
