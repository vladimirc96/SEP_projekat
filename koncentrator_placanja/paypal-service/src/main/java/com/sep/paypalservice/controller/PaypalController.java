package com.sep.paypalservice.controller;

import com.sep.paypalservice.dto.OrderDTO;
import com.sep.paypalservice.dto.PlanDTO;
import com.sep.paypalservice.dto.ShippingDTO;
import com.sep.paypalservice.dto.ShowPlansDTO;
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

    @RequestMapping(value = "/plan/execute/{tokenn}/{planID}", method = RequestMethod.GET)
    public String executeAgreement(@PathVariable("tokenn") String tokenn, @PathVariable("planID") String planID) {
        return service.executePlan(tokenn, planID);
    }

    @RequestMapping(value = "/getSpecificPlans/{sellerId}", method = RequestMethod.GET)
    public List<ShowPlansDTO> getSpecificPlans(@PathVariable("sellerId") long sellerId) {
        return service.getPlans(sellerId);
    }

}
