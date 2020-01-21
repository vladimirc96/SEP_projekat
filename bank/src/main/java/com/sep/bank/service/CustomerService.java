package com.sep.bank.service;

import com.sep.bank.model.Customer;
import com.sep.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    public Customer findOneById(Long id){
        return customerRepo.findOneById(id);
    }

    public List<Customer> findAll(){
        return customerRepo.findAll();
    }

    public Customer save(Customer customer){
        return customerRepo.save(customer);
    }

    public void remove(Long id){
        customerRepo.deleteById(id);
    }

    public Customer findByMerchantId(String id){
        return customerRepo.findByMerchantId(id);
    }

    public Customer findByMerchantIdAndMerchantPassword(String id, String password){
        return customerRepo.findByMerchantIdAndMerchantPassword(id, password);
    }
//
//    public Customer findOneByPan(String pan){
//        return customerRepo.findOneByPan(pan);
//    }



}
