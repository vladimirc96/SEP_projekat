package com.sep.paypalservice.service;

import com.paypal.api.payments.Currency;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.sep.paypalservice.client.OrderClient;
import com.sep.paypalservice.dto.*;
import com.sep.paypalservice.enums.Enums;
import com.sep.paypalservice.model.BillingPlan;
import com.sep.paypalservice.model.PPAgreement;
import com.sep.paypalservice.model.PPClient;
import com.sep.paypalservice.model.PPTransaction;
import com.sep.paypalservice.repository.ClientsRepository;
import com.sep.paypalservice.repository.TransactionRepository;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

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

    @Autowired
    OrderClient orderClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PPAgreementService agreementService;

    private Logging logger = new Logging(this);

    private static String RETURL = "http://192.168.43.86:4201";

    private static String kpAppUrl = "https://192.168.43.124:4200";




    private final long paymentMethodId = 2;

    public String payment(OrderDTO orderDTO) {
        logger.logInfo("PP_PAYMENT");

        // Inform Seller-service that status is PENDING
        try {
            orderClient.setActiveOrderStatus(new ActiveOrderDTO(orderDTO.getActiveOrderId(), Enums.OrderStatus.PENDING,
                    this.paymentMethodId));
        } catch (HttpClientErrorException ex) {
            logger.logWarning("PP_PAYMENT - Active order status is already PENDING.");
            throw new IllegalStateException("Active order status is already PENDING.");
        }

        try {
            Payment payment = createPayment(orderDTO.getPrice(), orderDTO.getCurrency(), orderDTO.getDescription(), orderDTO.getId());
            PPTransaction tr = new PPTransaction();
            tr.setOrderId(payment.getId());
            tr.setCreatedAt(payment.getCreateTime());
            tr.setStatus(payment.getState());
            PPClient cl = repo.findOneById(orderDTO.getId());
            tr.setClient(cl);
            tr.setActiveOrderId(orderDTO.getActiveOrderId());
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    String temp =  link.getHref();
                    String[] split1 = temp.split("\\?");
                    String[] split2 = split1[1].split("&");
                    String[] split3 = split2[1].split("=");
                    String tkn = split3[1];
                    tr.setPaymentToken(tkn);
                }
            }
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
                                    FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                                    foDTO.setOrderStatus(convertStatus(t.getStatus()));
                                    foDTO.setActiveOrderId(t.getActiveOrderId());
                                    foDTO.setAgreementID((long) 0);
                                    orderClient.finalizeOrder(foDTO);
                                    timer.cancel();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                                foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
                                foDTO.setActiveOrderId(transaction.getActiveOrderId());
                                orderClient.finalizeOrder(foDTO);
                                timer.cancel();
                            }
                        } else {
                            PPTransaction t = transacRepo.findOneByOrderId(transaction.getOrderId());
                            t.setStatus("suspended");
                            t = transacRepo.save(t);
                            FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                            foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
                            foDTO.setActiveOrderId(t.getActiveOrderId());
                            orderClient.finalizeOrder(foDTO);
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

    public TextLinkDTO cancelPayment(String token) {
        logger.logInfo("PP_PAYMENT_CANCEL");
        PPTransaction tr = transacRepo.findOneByPaymentToken(token);
        tr.setStatus("canceled");
        transacRepo.save(tr);

        long sellerID = tr.getClient().getId();
        ResponseEntity response = restTemplate.getForEntity("https://localhost:8500/sellers/sellers/getWebsiteURL/" + sellerID,
                String.class);

        String link = (String) response.getBody();
        TextLinkDTO ret = new TextLinkDTO("done", link);
        logger.logInfo("PP_PAYMENT_CANCEL_SUCCESS");
        return ret;
    }

    public TextLinkDTO cancelSubscription(String token) {
        PPAgreement ag = agreementService.findOneByTokenn(token);
        logger.logInfo("PP_SUBSCRIPTION_CANCEL - cancel made by user with username '" + ag.getUsername() + "'");
        ag.setStatus("canceled");
        agreementService.save(ag);

        ResponseEntity response = restTemplate.getForEntity("https://localhost:8500/sellers/sellers/getWebsiteURL/" + ag.getSellerId(),
                String.class);

        String link = (String) response.getBody();
        TextLinkDTO ret = new TextLinkDTO("done", link);
        logger.logInfo("PP_SUBSCRIPTION_CANCEL_SUCCESS");
        return ret;
    }

    public String cancelBillingPlan(long planID, long sellerId) throws PayPalRESTException {
        logger.logInfo("PP_PLAN_CANCEL");
        BillingPlan plan = billingPlanService.findOneById(planID);
        Plan p = new Plan();
        p.setId(plan.getPlanId());
        APIContext apiContext = getContextAndMerchant(sellerId);

        List<Patch> patchRequestList = new ArrayList<>();
        Map<String, String> value = new HashMap<>();
        value.put("state", "INACTIVE");

        Patch patch = new Patch();
        patch.setOp("replace");
        patch.setValue(value);
        patch.setPath("/");
        patchRequestList.add(patch);

        p.update(apiContext, patchRequestList);
        billingPlanService.remove(plan.getId());
        logger.logInfo("PP_PLAN_CANCEL_SUCCESS");
        return "done";
    }

    public String successPay(String paymentId, String payerId) {
        logger.logInfo("PP_PAY_CONFIRM");
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
                return this.kpAppUrl +  "/paypal/success/payment/" + tran.getClient().getId();
            }
        } catch (PayPalRESTException e) {
            logger.logError("PP_CONFIRM_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return RETURL;
    }

    public TextLinkDTO checkStatus(String id) {
        PPTransaction t = transacRepo.findOneByOrderId(id);
        TextLinkDTO ret = new TextLinkDTO();

        ResponseEntity response = restTemplate.getForEntity("https://localhost:8500/sellers/sellers/getWebsiteURL/" + t.getClient().getId(),
                String.class);

        String link = (String) response.getBody();
        ret.setWebsiteLink(link);
        if(t.getStatus().equals("suspended")) {
            ret.setText("invalid");
            return ret;
        }
        ret.setText("valid");
        return ret;
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

    public String agreement(ShippingDTO dto, String username) {
        logger.logInfo("PP_AGREEMENT");
        BillingPlan bp = billingPlanService.findOneById(dto.getPlanId());
        try {
            Agreement agreement = createAgreement(dto, bp.getPlanId());
            PPAgreement agr = new PPAgreement();
            agr.setBillingPlan(bp);
            agr.setTokenn(agreement.getToken());
            agr.setSellerId(bp.getSellerId());
            agr.setStatus("created");
            agr.setActiveOrderId(dto.getActiveOrderId());
            agr.setUsername(username);
            agr = agreementService.save(agr);
            final PPAgreement agriment = agr;

            CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
                Timer timer = new Timer();
                long startTime = System.currentTimeMillis();
                timer.scheduleAtFixedRate(new TimerTask(){
                    @Override
                    public void run(){

                        System.out.println("Loop ping agreement");
                        long stopTime = System.currentTimeMillis();
                        if(stopTime - startTime < 600000) {
                            try {
                                PPAgreement pa = agreementService.findOneByTokenn(agriment.getTokenn());

                                if (!pa.getStatus().equals("created")) {
                                    System.out.println("Loop cancel agreement");
                                    FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                                    foDTO.setOrderStatus(convertStatus(pa.getStatus()));
                                    foDTO.setActiveOrderId(pa.getActiveOrderId());
                                    foDTO.setAgreementID(pa.getId());
                                    try {
                                        foDTO.setFinalDate(new SimpleDateFormat("yyyy-MM-dd").parse(pa.getFinalPaymentDate().split("T")[0]));
                                    } catch (Exception e) {

                                    }
                                    orderClient.finalizeOrder(foDTO);
                                    timer.cancel();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                                foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
                                foDTO.setActiveOrderId(agriment.getActiveOrderId());
                                orderClient.finalizeOrder(foDTO);
                                timer.cancel();
                            }
                        } else {
                            PPAgreement pa = agreementService.findOneByTokenn(agriment.getTokenn());
                            pa.setStatus("suspended");
                            pa = agreementService.save(pa);
                            FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                            foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
                            foDTO.setActiveOrderId(pa.getActiveOrderId());
                            orderClient.finalizeOrder(foDTO);
                            System.out.println("Loop cancel agreement TIMER");
                            timer.cancel();
                        }
                    }
                },5000,10000);
                return "OK";
            });

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

    public TextLinkDTO executePlan(String token) {
        Agreement agreement =  new Agreement();
        agreement.setToken(token);
        TextLinkDTO retVal = new TextLinkDTO();
        PPAgreement agr = agreementService.findOneByTokenn(token);
        try {
            logger.logInfo("PP_EXEPLAN");
            APIContext apiContext = getContextAndMerchant(agr.getSellerId());
            Agreement activeAgreement = agreement.execute(apiContext, agreement.getToken());
            System.out.println(activeAgreement.toJSON());
            agr.setActiveAgreementId(activeAgreement.getId());
            agr.setStatus("approved");
            agr.setPayerEmail(activeAgreement.getPayer().getPayerInfo().getEmail());
            agr.setPayerId(activeAgreement.getPayer().getPayerInfo().getPayerId());
            agr.setStartDate(activeAgreement.getStartDate());
            agr.setFinalPaymentDate(activeAgreement.getAgreementDetails().getFinalPaymentDate());
            agreementService.save(agr);

            ResponseEntity response = restTemplate.getForEntity("https://localhost:8500/sellers/sellers/getWebsiteURL/" + agr.getSellerId(),
                    String.class);

            String link = (String) response.getBody();

            retVal.setText("success");
            retVal.setWebsiteLink(link);
            logger.logInfo("PP_EXEPLAN_SUCCESS");
            return retVal;
        } catch (PayPalRESTException e) {
            logger.logError("PP_EXEPLAN_ERR: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        retVal.setText("error");
        return retVal;
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
        redirectUrls.setCancelUrl(this.kpAppUrl + "/cancel/payment/paypal");
        redirectUrls.setReturnUrl(this.kpAppUrl  + "/payment/verifying");
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

        //  ChargeModels chargeModels = new ChargeModels(); //ZA SHIPPING ITD

        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setReturnUrl(this.kpAppUrl + "/paypal/execute/plan");
        merchantPreferences.setCancelUrl(this.kpAppUrl + "/cancel/paypal/plan");
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

        APIContext apiContext = getContextAndMerchant(dto.getId());

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

    public List<ShowPlansDTO> getPlans(long sellerID) {
        ArrayList<ShowPlansDTO> planovi = new ArrayList<>();
        List<BillingPlan> bilplans = billingPlanService.findBySeller(sellerID);
        for(BillingPlan bp : bilplans) {
            if(bp.getState().equals("ACTIVE")) {
                ShowPlansDTO temp = new ShowPlansDTO(bp.getId(), bp.getName(), bp.getFrequency(), bp.getFreqInterval(), bp.getCycles(), bp.getAmount(), bp.getCurrency(), bp.getAmountStart(), sellerID);
                planovi.add(temp);
            }
        }

        return planovi;
    }

    public List<ShowPlansDTO> getAllPlans(long sellerID) throws PayPalRESTException {
        ArrayList<ShowPlansDTO> planovi = new ArrayList<>();
        APIContext apiContext = getContextAndMerchant(sellerID);

        Map<String, String> value = new HashMap<>();
        value.put("page_size", "20");
        value.put("total_required", "yes");
        value.put("status", "ACTIVE");
        PlanList planList = Plan.list(apiContext, value);
        for(Plan plan1 : planList.getPlans()) {
            Plan plan = Plan.get(apiContext, plan1.getId());
            BillingPlan ppp = billingPlanService.findByPlanId(plan.getId());
            ShowPlansDTO s = new ShowPlansDTO(ppp.getId(), plan.getName(), plan.getPaymentDefinitions().get(0).getFrequency(), plan.getPaymentDefinitions().get(0).getFrequencyInterval(),
                    plan.getPaymentDefinitions().get(0).getCycles(), Double.parseDouble(plan.getPaymentDefinitions().get(0).getAmount().getValue()),
                    plan.getPaymentDefinitions().get(0).getAmount().getCurrency(), Double.parseDouble(plan.getMerchantPreferences().getSetupFee().getValue()), sellerID);
            planovi.add(s);
        }

        return planovi;
    }


    public AgreementListDTO getUserAgreements(String korisnik) throws ParseException, PayPalRESTException {
        List<AgreementDTO> lista = new ArrayList<>();
        List<PPAgreement> agrimenti = agreementService.findAllByUsername(korisnik);
        for(PPAgreement a : agrimenti) {
            APIContext apiContext = getContextAndMerchant(a.getSellerId());
            Agreement agrmnt = Agreement.get(apiContext, a.getActiveAgreementId());
            if(agrmnt.getState().equals("Cancelled") || agrmnt.getState().equals("Expired")) {
                a.setStatus(agrmnt.getState());
                a = agreementService.save(a);
            }
            if(a.getStatus().equals("approved")) {
                AgreementDTO dto = new AgreementDTO();
                dto.setId(a.getId());
                dto.setAmount(String.valueOf(a.getBillingPlan().getAmount()));
                dto.setCurrency(a.getBillingPlan().getCurrency());
                dto.setCycles(a.getBillingPlan().getCycles());
                String SD1[] = a.getStartDate().split("T");
                String SD = SD1[0];
                dto.setStartDate(SD);
                dto.setStatus(a.getStatus());
                dto.setFrequency(a.getBillingPlan().getFrequency());
                dto.setFreqInterval(a.getBillingPlan().getFreqInterval());
                dto.setName(a.getBillingPlan().getName());
                dto.setSellerId(a.getSellerId());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                final String NEW_FORMAT = "yyyy-MM-dd";
                String FD1[] = a.getFinalPaymentDate().split("T");
                String FD = FD1[0];
                Date finalPayment=formatter.parse(FD);
                Date newDate;
                String finalni;
                if(a.getBillingPlan().getFrequency().equals("MONTH")) {
                    newDate = DateUtils.addMonths(finalPayment, 1);
                    finalni = formatter.format(newDate);
                } else if (a.getBillingPlan().getFrequency().equals("YEAR")) {
                    newDate = DateUtils.addYears(finalPayment, 1);
                    finalni = formatter.format(newDate);
                } else if (a.getBillingPlan().getFrequency().equals("WEEK")) {
                    newDate = DateUtils.addWeeks(finalPayment, 1);
                    finalni = formatter.format(newDate);
                } else {
                    newDate = DateUtils.addDays(finalPayment, 1);
                    finalni = formatter.format(newDate);
                }
                dto.setEndDate(finalni);
                String td = formatter.format(new Date());
                Date today = formatter.parse(td);
                Date start = formatter.parse(SD);
                if(start.compareTo(today) < 0) {
                    lista.add(dto);
                }
            }
        }
        AgreementListDTO ret = new AgreementListDTO(lista);
        return ret;
    }

    public String cancelAgreement(long agrID) throws PayPalRESTException {
        PPAgreement a = agreementService.findOneById(agrID);
        APIContext apiContext = getContextAndMerchant(a.getSellerId());
        Agreement agrmnt = Agreement.get(apiContext, a.getActiveAgreementId());
        if(agrmnt.getState().equals("Cancelled") || agrmnt.getState().equals("Expired")) {
            a.setStatus(agrmnt.getState());
            a = agreementService.save(a);
        }
        if(a.getStatus().equals("approved")) {
            AgreementStateDescriptor asd = new AgreementStateDescriptor();
            asd.setNote("Cancel the contract");
            agrmnt.cancel(apiContext, asd);
            a.setStatus("Cancelled");
            agreementService.save(a);
            return "done";
        }
        return "error";
    }

    public String agreementExistsOnPP(long agrID) throws PayPalRESTException {
        PPAgreement a = agreementService.findOneById(agrID);
        APIContext apiContext = getContextAndMerchant(a.getSellerId());
        Agreement agrmnt = Agreement.get(apiContext, a.getActiveAgreementId());
        if(agrmnt.getState().equals("Cancelled") || agrmnt.getState().equals("Expired")) {
            if(!a.getStatus().equals("Cancelled") && !a.getStatus().equals("Expired")) {
                a.setStatus(agrmnt.getState());
                a = agreementService.save(a);
            }
            return "cancelled";
        }
        return "exists";
    }


    private Enums.OrderStatus convertStatus(String status) {
        if (status.equals("approved")) {
            return Enums.OrderStatus.SUCCESS;
        } else {
            return Enums.OrderStatus.FAILED;
        }
    }

}
