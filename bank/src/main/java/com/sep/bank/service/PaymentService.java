package com.sep.bank.service;

import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.Customer;
import com.sep.bank.model.Payment;
import com.sep.bank.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    public Payment findOneById(Long id){
        return paymentRepo.findOneById(id);
    }

    public Payment save(Payment payment){
        return paymentRepo.save(payment);
    }

    public Payment create(PaymentRequestDTO paymentRequest, Long acquirerOrderId, Long merchantOrderId){
        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setAcquirerOrderId(acquirerOrderId);
        payment.setMerchantOrderId(merchantOrderId);
        payment = paymentRepo.save(payment);
        return payment;
    }

}
