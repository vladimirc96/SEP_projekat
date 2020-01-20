package com.sep.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.dto.OrderDTO;
import com.sep.paypalservice.model.PPClient;
import com.sep.paypalservice.model.PPTransaction;
import com.sep.paypalservice.repository.ClientsRepository;
import com.sep.paypalservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class PaypalService {

    @Autowired
    private ClientsRepository repo;

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private TransactionRepository transacRepo;

    private Logging logger = new Logging(this);

    public String payment(OrderDTO orderDTO) {
        logger.logInfo("PP_PAYMENT");
        try {
            Payment payment = createPayment(orderDTO.getPrice(), orderDTO.getCurrency(), orderDTO.getDescription(), orderDTO.getId());
            PPTransaction tr = new PPTransaction();
            tr.setOrderId(payment.getId());
            tr.setCreatedAt(payment.getCreateTime());
            tr.setStatus(payment.getState());
            PPClient cl = repo.findOneById(orderDTO.getId());
            tr.setClient(cl);
            transacRepo.save(tr);

            final PPTransaction transaction = tr;

            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                Timer timer = new Timer();
                long startTime = System.currentTimeMillis();
                timer.scheduleAtFixedRate(new TimerTask(){
                    @Override
                    public void run(){

                        System.out.println("Loop ping");
                        long stopTime = System.currentTimeMillis();
                        if(stopTime - startTime < 600000) {
                            try {
                                PPTransaction t = transacRepo.findOneByOrderId(transaction.getOrderId());

                                if (!t.getStatus().equals("created")) {
                                    System.out.println("Loop cancel");
                                    timer.cancel();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                timer.cancel();
                            }
                        } else {
                            PPTransaction t = transacRepo.findOneByOrderId(transaction.getOrderId());
                            t.setStatus("suspended");
                            transacRepo.save(t);
                            System.out.println("Loop cancel TIMER");
                            timer.cancel();
                        }
                    }
                },5000,10000);
                return "OK";
            });

            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            logger.logError("PP_PAYMENT_ERR: " + e.getMessage());
            e.printStackTrace();
        }
        return "https://localhost:4200/";
    }

    public String successPay(String paymentId, String payerId) {
        logger.logInfo("PP_CONFIRM");
        try {
            Payment payment = executePayment(paymentId, payerId);

            PPTransaction tran = transacRepo.findOneByOrderId(paymentId);
            tran.setPayerId(payment.getPayer().getPayerInfo().getPayerId());
            tran.setPayerEmail(payment.getPayer().getPayerInfo().getEmail());
            tran.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
            tran.setAmount(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()));
            tran.setStatus(payment.getState());
            tran.setPayee(payment.getTransactions().get(0).getPayee().getEmail());
            transacRepo.save(tran);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                logger.logInfo("PP_CONFIRM_SUCCESS");
                return "https://localhost:4200/paypal/success";
            }
        } catch (PayPalRESTException e) {
            logger.logError("PP_CONFIRM_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return "https://localhost:4200/";
    }

    public String checkStatus(String id) {
        PPTransaction t = transacRepo.findOneByOrderId(id);
        if(t.getStatus().equals("suspended")) {
            return "invalid";
        }
        return "valid";
    }

    private APIContext getContextAndMerchant(Long id) {
        PPClient cl = repo.findOneById(id);
        String secret = cryptoService.decrypt(cl.getClientSecret());
        APIContext context = new APIContext(cl.getClientId(), secret, "sandbox");
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
        redirectUrls.setCancelUrl("https://localhost:4200/centrala");
        redirectUrls.setReturnUrl("https://localhost:4200/payment/verifying");
        payment.setRedirectUrls(redirectUrls);

        APIContext apiContext = getContextAndMerchant(id);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        PPTransaction transaction = transacRepo.findOneByOrderId(paymentId);
        APIContext apiContext = getContextAndMerchant(transaction.getClient().getId());

        return payment.execute(apiContext, paymentExecute);
    }


}
