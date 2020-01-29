package com.sep.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.dto.OrderDTO;
import com.sep.paypalservice.dto.PlanDTO;
import com.sep.paypalservice.dto.ShippingDTO;
import com.sep.paypalservice.dto.ShowPlansDTO;
import com.sep.paypalservice.model.BillingPlan;
import com.sep.paypalservice.model.PPClient;
import com.sep.paypalservice.model.PPTransaction;
import com.sep.paypalservice.repository.ClientsRepository;
import com.sep.paypalservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
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

    @Autowired
    private BillingPlanService billingPlanService;

    private Logging logger = new Logging(this);

    private static String RETURL = "http://localhost:4201";

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
            tr.setActiveOrderId(orderDTO.getActiveOrderId());
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
        return RETURL;
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
                return "https://localhost:4200/paypal/success/payment";
            }
        } catch (PayPalRESTException e) {
            logger.logError("PP_CONFIRM_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return RETURL;
    }

    public String checkStatus(String id) {
        PPTransaction t = transacRepo.findOneByOrderId(id);
        if(t.getStatus().equals("suspended")) {
            return "invalid";
        }
        return "valid";
    }

    public String plan(PlanDTO dto) {
        logger.logInfo("PP_PLAN");
        try {
            Plan plan = createPlan(dto);
            BillingPlan pln = new BillingPlan(plan.getId(), plan.getName(), "ACTIVE", plan.getCreateTime(), plan.getUpdateTime(), plan.getDescription(), dto.getFrequency(), dto.getFreqInterval(), dto.getCycles(), Double.parseDouble(dto.getAmount()), dto.getCurrency(), Double.parseDouble(dto.getAmountStart()), dto.getMerchantId());
            billingPlanService.save(pln);
            return "PlanCreated";
        } catch (PayPalRESTException e) {
            logger.logError("PP_PLAN_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        return "PlanNotCreated";
    }

    public String agreement(ShippingDTO dto) {
        logger.logInfo("PP_AGREEMENT");
        BillingPlan bp = billingPlanService.findOneById(dto.getPlanId());
        try {
            Agreement agreement = createAgreement(dto, bp.getPlanId());
            for (Links links : agreement.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            logger.logError("PP_AGREEMENT_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }  catch (MalformedURLException e) {
            logger.logError("PP_AGREEMENT_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.logError("PP_AGREEMENT_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        return RETURL;
    }

    public String executePlan(String token, String planID) {
        Agreement agreement =  new Agreement();
        agreement.setToken(token);
        try {
            logger.logInfo("PP_EXEPLAN");
            long ide = Long.parseLong(planID);
            APIContext apiContext = getContextAndMerchant(ide);
            Agreement activeAgreement = agreement.execute(apiContext, agreement.getToken());
            System.out.println(activeAgreement.toJSON());
            //TODO: Zabeleziti u bazi agreement
            return "success";
        } catch (PayPalRESTException e) {
            logger.logError("PP_EXEPLAN_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return "error";
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
        redirectUrls.setCancelUrl("http://localhost:4201");
        redirectUrls.setReturnUrl("https://localhost:4200/payment/verifying");
        payment.setRedirectUrls(redirectUrls);

        APIContext apiContext = getContextAndMerchant(id);

        return payment.create(apiContext);
    }

    public Plan createPlan(PlanDTO dto) throws PayPalRESTException {
        Plan plan = new Plan();
        plan.setType("fixed");
        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Regular Payments");
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency(dto.getFrequency());
        paymentDefinition.setFrequencyInterval(dto.getFreqInterval());
        paymentDefinition.setCycles(dto.getCycles());
        paymentDefinition.setAmount(new Currency(dto.getCurrency(), dto.getAmount()));

//        ChargeModels chargeModels = new ChargeModels(); //ZA SHIPPING ITD

        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setReturnUrl("https://localhost:4200/paypal/execute/plan");
        merchantPreferences.setCancelUrl("http://localhost:4201");
        merchantPreferences.setAutoBillAmount("yes");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");
        merchantPreferences.setMaxFailAttempts("1");
        merchantPreferences.setSetupFee(new Currency(dto.getCurrency(), dto.getAmountStart()));
        ArrayList<PaymentDefinition> pd = new ArrayList<>();
        pd.add(paymentDefinition);
        plan.setPaymentDefinitions(pd);
        plan.setMerchantPreferences(merchantPreferences);

        APIContext apiContext = getContextAndMerchant(dto.getMerchantId());

        Plan createdPlan = plan.create(apiContext);

        //Changing plan from created to active
        List<Patch> patchRequestList = new ArrayList<>();
        Map<String, String> value = new HashMap<>();
        value.put("state", "ACTIVE");

        Patch patch = new Patch();
        patch.setOp("replace");
        patch.setValue(value);
        patch.setPath("/");
        patchRequestList.add(patch);

        createdPlan.update(apiContext, patchRequestList);

        return createdPlan;
    }

    public Agreement createAgreement(ShippingDTO dto, String planId) throws UnsupportedEncodingException, PayPalRESTException, MalformedURLException {

        Agreement agreement = new Agreement();
        agreement.setName("Base Agreement");
        agreement.setDescription("Naucna centrala - pretplata na casopis");
        Instant i = java.time.Clock.systemUTC().instant();
        Instant j = i.plusSeconds(300);
        String dandt = (j.toString());
        String[] split = dandt.split("\\.");
        String vreme = split[0] + "Z";
        agreement.setStartDate(vreme);

        Plan plan = new Plan();
        plan.setId(planId);
        agreement.setPlan(plan);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        agreement.setPayer(payer);

        ShippingAddress shipping = new ShippingAddress();
        shipping.setLine1(dto.getStreet());
        shipping.setCity(dto.getCity());
        shipping.setState(dto.getState());
        shipping.setPostalCode(dto.getPostalCode());
        shipping.setCountryCode(dto.getCountryCode());
        agreement.setShippingAddress(shipping);

        byte[] actualByte = Base64.getDecoder().decode(dto.getId());
        String dec = new String(actualByte);
        long actualID = Long.parseLong(dec);

        APIContext apiContext = getContextAndMerchant(actualID);

        return agreement.create(apiContext);
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

    public List<ShowPlansDTO> getPlansEnc(String selID) {
        byte[] actualByte = Base64.getDecoder().decode(selID);
        String dec = new String(actualByte);
        ArrayList<ShowPlansDTO> planovi = new ArrayList<>();
        if(!dec.equals("")) {
            long sellerID = Long.parseLong(dec);
            List<BillingPlan> bilplans = billingPlanService.findBySeller(sellerID);
            for(BillingPlan bp : bilplans) {
                ShowPlansDTO temp = new ShowPlansDTO(bp.getId(), bp.getName(), bp.getFrequency(), bp.getFreqInterval(), bp.getCycles(), bp.getAmount(), bp.getCurrency(), bp.getAmountStart());
                planovi.add(temp);
            }
        }

        return planovi;
    }


}
