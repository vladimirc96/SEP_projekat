package com.sep.bankservice.controller;


import com.sep.bankservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-status")
public class OrderStatusController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrderStatus(@PathVariable long id) {
        return new ResponseEntity<>(transactionService.getOrderStatus(id), HttpStatus.OK);
    }
}
