package com.sep.bankservice.controller;

import com.netflix.discovery.converters.Auto;
import com.sep.bankservice.dto.CustomerDTO;
import com.sep.bankservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/registration")
public class BankRegistrationController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(path = "/")
    public @ResponseBody
    ResponseEntity registerBank(@RequestBody CustomerDTO cDTO) {
        try {
            return new ResponseEntity(customerService.registerCustomer(cDTO), HttpStatus.OK);
        } catch (AccessDeniedException ade) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
