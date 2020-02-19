package com.sep.bankservice.service;

import com.netflix.discovery.converters.Auto;
import com.sep.bankservice.client.RegistrationClient;
import com.sep.bankservice.dto.CustomerDTO;
import com.sep.bankservice.model.Customer;
import com.sep.bankservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private RegistrationClient registrationClient;

    @Autowired
    private CryptoService cryptoService;

    @Transactional(rollbackFor = AccessDeniedException.class)
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) throws AccessDeniedException {

        Customer customer = new Customer(customerDTO);
        customer.setId(customerDTO.getId());
        customer.setMerchantPassword(cryptoService.encrypt(customer.getMerchantPassword()));
        customer = customerRepo.save(customer);

        registrationClient.approveRegistration(customerDTO);

        return CustomerDTO.formDTO(customer);
    }

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



}
