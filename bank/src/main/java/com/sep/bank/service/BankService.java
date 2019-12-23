package com.sep.bank.service;

import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.Bank;
import com.sep.bank.model.Customer;
import com.sep.bank.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BankRepository bankRepo;

    public Bank findOneById(Long id){
        return bankRepo.findOneById(id);
    }

    public List<Bank> findAll(){
        return bankRepo.findAll();
    }

    public Bank save(Bank bank){
        return bankRepo.save(bank);
    }

    public void remove(Long id){
        bankRepo.deleteById(id);
    }

    public boolean isPaymentValid(PaymentRequestDTO paymentRequest){
        Customer customer = customerService.findByMerchantIdAndMerchantPassword(paymentRequest.getMerchantId(), paymentRequest.getMerchantPassword());
        if(customer == null){
            return false;
        }
        return true;
    }

}
