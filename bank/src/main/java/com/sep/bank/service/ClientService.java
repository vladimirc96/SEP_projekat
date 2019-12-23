package com.sep.bank.service;

import com.sep.bank.model.Customer;
import com.sep.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepo;

    public Customer findOneById(Long id){
        return clientRepo.findOneById(id);
    }

    public List<Customer> findAll(){
        return clientRepo.findAll();
    }

    public Customer save(Customer customer){
        return clientRepo.save(customer);
    }

    public void remove(Long id){
        clientRepo.deleteById(id);
    }

    public Customer findByMerchantId(String id){
        return clientRepo.findByMerchantId(id);
    }

    public Customer findByMerchantIdAndMerchantPassword(String id, String password){
        return clientRepo.findByMerchantIdAndMerchantPassword(id, password);
    }



}
