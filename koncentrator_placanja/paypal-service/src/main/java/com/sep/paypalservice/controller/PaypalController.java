package com.sep.paypalservice.controller;

import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.dto.*;
import com.sep.paypalservice.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
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

    @RequestMapping(value = "/plan/agreement/{username}", method = RequestMethod.POST)
    private String planAgreement(@RequestBody ShippingDTO dto, @PathVariable("username") String username) {
        return service.agreement(dto, username);
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

    @RequestMapping(value = "/getAllPlans/{sellerId}", method = RequestMethod.GET)
    public List<ShowPlansDTO> cancelPlan(@PathVariable("sellerId") long sellerId) throws PayPalRESTException {
        return service.getAllPlans(sellerId);
    }

    @RequestMapping(value = "/cancelBillingPlan/{planid}/{sellerId}", method = RequestMethod.PUT)
    public String cancelBillingPlan(@PathVariable("planid") long planid, @PathVariable("sellerId") long sellerId) throws PayPalRESTException {
        return service.cancelBillingPlan(planid, sellerId);
    }

    @RequestMapping(value = "/getUserAgreements/{username}", method = RequestMethod.GET)
    public AgreementListDTO getUserAgreements(@PathVariable("username") String username) throws ParseException, PayPalRESTException {
        return service.getUserAgreements(username);
    }

    @RequestMapping(value = "/cancelSubscription/{token}", method = RequestMethod.GET)
    public String cancelPlan(@PathVariable("token") String token) {
        return service.cancelSubscription(token);
    }

    @RequestMapping(value = "/cancelAgreement/{agrID}", method = RequestMethod.GET)
    public String cancelAgreement(@PathVariable("agrID") long agrID) throws PayPalRESTException {
        return service.cancelAgreement(agrID);
    }
}
