package com.sep.paypalservice.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.dto.OrderDTO;
import com.sep.paypalservice.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/paypal")
public class PaypalController {

    @Autowired
    PaypalService service;

    @RequestMapping(value = "/cao", method = RequestMethod.GET)
    private String cao() {
        return "Cao matori";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private String payment(@RequestBody OrderDTO orderDTO) {
        try {
            Payment payment = service.createPayment(orderDTO.getPrice(), orderDTO.getCurrency(), orderDTO.getMethod(),
                    orderDTO.getIntent(), orderDTO.getDescription(), "http://localhost:4200/payment/cancel",
                    "http://localhost:4200/payment/success");
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "http://localhost:4200/";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String successPay(@RequestParam("paymentID") String paymentId, @RequestParam("payerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "http://localhost:4200/payment/success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "http://localhost:4200/";
    }

}
