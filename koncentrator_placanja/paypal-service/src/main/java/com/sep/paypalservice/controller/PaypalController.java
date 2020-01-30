package com.sep.paypalservice.controller;

import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.dto.*;
import com.sep.paypalservice.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/plan", method = RequestMethod.POST)
    private String plan(@RequestBody PlanDTO planDTO) {
        return service.plan(planDTO);
    }

    @RequestMapping(value = "/plan/agreement", method = RequestMethod.POST)
    private String planAgreement(@RequestBody ShippingDTO dto) {
        return service.agreement(dto);
    }

    @RequestMapping(value = "/plan/execute/{tokenn}", method = RequestMethod.GET)
    public String executeAgreement(@PathVariable("tokenn") String tokenn) {
        return service.executePlan(tokenn);
    }

    @RequestMapping(value = "/getSpecificPlans/{sellerId}", method = RequestMethod.GET)
    public List<ShowPlansDTO> getSpecificPlans(@PathVariable("sellerId") long sellerId) {
        return service.getPlans(sellerId);
    }

    @RequestMapping(value = "/cancelPayment/{token}", method = RequestMethod.GET)
    public String cancelPayment(@PathVariable("token") String token) {
        return service.cancelPayment(token);
    }

    @RequestMapping(value = "/cancelPlan/{token}", method = RequestMethod.GET)
    public String cancelPlan(@PathVariable("token") String token) {
        return service.cancelPlan(token);
    }

    @RequestMapping(value = "/getAllPlans/{sellerId}", method = RequestMethod.GET)
    public List<ShowPlansDTO> cancelPlan(@PathVariable("sellerId") long sellerId) throws PayPalRESTException {
        return service.getAllPlans(sellerId);
    }
}
