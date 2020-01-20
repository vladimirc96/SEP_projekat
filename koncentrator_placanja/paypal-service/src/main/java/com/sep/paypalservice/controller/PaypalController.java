package com.sep.paypalservice.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.dto.OrderDTO;
import com.sep.paypalservice.service.CryptoService;
import com.sep.paypalservice.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paypal")
public class PaypalController {

    @Autowired
    PaypalService service;

    @RequestMapping(value = "", method = RequestMethod.POST)
    private String payment(@RequestBody OrderDTO orderDTO) {
            return service.payment(orderDTO);
    }

    @RequestMapping(value = "/success/{payment}/{payer}", method = RequestMethod.GET)
    public String successPay(@PathVariable("payment") String payment, @PathVariable("payer") String payer) {
        return service.successPay(payment, payer);
    }

    @RequestMapping(value = "/status/{paymentId}", method = RequestMethod.GET)
    public String paymentStatus(@PathVariable("paymentId") String payment) {
        return service.checkStatus(payment);
    }

}
