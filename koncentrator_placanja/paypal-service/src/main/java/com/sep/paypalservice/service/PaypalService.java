package com.sep.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.dto.OrderDTO;
import com.sep.paypalservice.model.PPClient;
import com.sep.paypalservice.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {

    @Autowired
    private ClientsRepository repo;
    private Long ide;

    private Logging logger = new Logging(this);

    public String payment(OrderDTO orderDTO) {
        logger.logInfo("PP_PAYMENT");
        try {
            Payment payment = createPayment(orderDTO.getPrice(), orderDTO.getCurrency(), orderDTO.getDescription(), orderDTO.getId());
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            logger.logError("PP_PAYMENT_ERR: " + e.getMessage());
            e.printStackTrace();
        }
        return "http://localhost:4200/";
    }

    public String successPay(String paymentId, String payerId) {
        logger.logInfo("PP_CONFIRM");
        try {
            Payment payment = executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                logger.logInfo("PP_CONFIRM_SUCCESS");
                return "http://localhost:4200/paypal/success";
            }
        } catch (PayPalRESTException e) {
            logger.logError("PP_CONFIRM_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return "http://localhost:4200/";
    }

    private APIContext getContextAndMerchant(Long id) {
        PPClient cl = repo.findOneById(id);
        APIContext context = new APIContext(cl.getClientId(), cl.getClientSecret(), "sandbox");
        return context;
    }

    public Payment createPayment( Double total, String currency, String description, Long id) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:4200/centrala");
        redirectUrls.setReturnUrl("http://localhost:4200/payment/verifying");
        payment.setRedirectUrls(redirectUrls);

        ide = id;
        APIContext apiContext = getContextAndMerchant(id);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        APIContext apiContext = getContextAndMerchant(ide);

        return payment.execute(apiContext, paymentExecute);
    }


}
