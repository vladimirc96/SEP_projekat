package com.sep.bitcoinservice.controller;

import com.sep.bitcoinservice.dto.OrderDTO;
import com.sep.bitcoinservice.service.contracts.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;

@RestController
@RequestMapping("/payment")
public class BitcoinController {

    @Autowired
    private ITransactionService _transactionService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable long id) {
        return new ResponseEntity<>(_transactionService.getTransaction(id), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getTransactions() {
        return new ResponseEntity<>(_transactionService.getTransactions(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/status")
    public ResponseEntity<?> getTransactionStatus(@PathVariable long id) {
        return new ResponseEntity<>(_transactionService.getTransactionStatusDto(id), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createPayment(@RequestBody OrderDTO oDTO) {
        try {
            return new ResponseEntity<>(_transactionService.createPayment(oDTO), HttpStatus.CREATED);
        } catch (InstanceAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalStateException ise) {
            return new ResponseEntity<>(ise.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/rate/{from}/{to}")
    public ResponseEntity<?> getTransactionStatus(@PathVariable String from, @PathVariable String to) {
        return new ResponseEntity<>(_transactionService.getExchangeRate(from, to), HttpStatus.OK);
    }








}
