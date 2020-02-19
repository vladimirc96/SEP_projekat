package com.sep.paypalservice.controller;

import com.sep.paypalservice.service.PaypalService;
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
    PaypalService _paypalService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrderStatus(@PathVariable long id) throws Exception {
        return new ResponseEntity<>(_paypalService.getOrderStatus(id), HttpStatus.OK);
    }
}
