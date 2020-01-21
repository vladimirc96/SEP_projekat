package com.sep.bitcoinservice.service;

import com.sep.bitcoinservice.dto.*;
import com.sep.bitcoinservice.enums.Enums;
import com.sep.bitcoinservice.model.Seller;
import com.sep.bitcoinservice.model.Transaction;
import com.sep.bitcoinservice.repository.SellerRepository;
import com.sep.bitcoinservice.repository.TransactionRepository;
import com.sep.bitcoinservice.service.contracts.ITransactionService;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.hibernate.criterion.Order;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.InstanceAlreadyExistsException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class TransactionService implements ITransactionService {

    @Autowired
    TransactionRepository transactionRepo;

    @Autowired
    SellerRepository sellerRepo;

    @Autowired
    HttpComponentsClientHttpRequestFactory requestFactory;

    @Autowired
    CryptoService cryptoService;

    private Logging logger = new Logging(this);

    private final String CoinGateAPI = "https://api-sandbox.coingate.com/v2/orders";
    private final String CoinGateExchangeRateAPI = "https://api-sandbox.coingate.com/v2/rates/merchant";

    @Override
    public TransactionDTO getTransaction(long id) {
        return TransactionDTO.formDto(transactionRepo.findById(id).get());
    }

    @Override
    public List<TransactionDTO> getTransactions() {
        return transactionRepo.findAll().stream().map(t -> TransactionDTO.formDto(t)).collect(Collectors.toList());
    }

    @Override
    public TransactionDTO createPayment(OrderDTO order) throws InstanceAlreadyExistsException {

        Seller s = sellerRepo.findById(order.getSellerId()).get();
        String authToken = s.getAuthToken();

        HttpEntity<?> request = formPostRequest(authToken, new CGOrderDTO(order));
        HttpEntity<?> checkoutRequest = formPostRequest(authToken, new CGCheckoutDTO(Enums.Currency.BTC));

        RestTemplate restTemplate = new RestTemplate(this.requestFactory);

        // create order
        ResponseEntity<CGOrderFullDTO> response = restTemplate.postForEntity(this.CoinGateAPI, request,
                CGOrderFullDTO.class);

        // checkout
        response = restTemplate.postForEntity(this.CoinGateAPI + "/" + response.getBody().getId() + "/checkout",
                checkoutRequest,
                CGOrderFullDTO.class);

        Transaction t = Transaction.convertObjects(new Transaction(), response.getBody());

        if (transactionRepo.existsById(t.getOrderId())) {
            throw new InstanceAlreadyExistsException("Order is already created.");
        }

        final Transaction transaction = t;

        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){

                    System.out.println("Loop ping");

                    try {
                        Transaction t = transactionRepo.findByOrderId(transaction.getOrderId()).get();
                        System.out.println(t.toString());
                        TransactionStatusDTO tDTO = getTransactionStatusDto(t.getId());

                        if (!tDTO.getStatus().equals("new") && !tDTO.getStatus().equals("pending")) {

                            System.out.println("Loop cancel");
                            timer.cancel();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        timer.cancel();
                    }

                }
            },5000,10000);
            return "OK";
        });

        t.setSeller(s);
        t = transactionRepo.save(t);
        logger.logInfo("Transaction saved: " + t.getId());

        return TransactionDTO.formDto(t);
    }

    @Override
    public TransactionStatusDTO getTransactionStatusDto(long id) {

        Transaction t = transactionRepo.findById(id).get();
        String statusBefore = t.getStatus();

        t = getTransactionStatus(t);

        String statusAfter = t.getStatus();

        if (!statusAfter.equals(statusBefore)) {
            t = transactionRepo.save(t);
            logger.logInfo("Transaction (id: " + t.getId() + ") status changed from '" + statusBefore +
                    "' to '" + statusAfter + "'");
        }


        return new TransactionStatusDTO(t.getId(), t.getStatus(), t.getAmountDifference());
    }

    @Override
    public RateDTO getExchangeRate(String from, String to) {

        try {
            RestTemplate restTemplate = new RestTemplate(this.requestFactory);


            ResponseEntity<Double> response = restTemplate.exchange(this.CoinGateExchangeRateAPI + "/" + from +
                            "/" + to,
                    HttpMethod.GET, HttpEntity.EMPTY,
                    Double.class);

            return new RateDTO(response.getBody().doubleValue());
        } catch (Exception ex) {
            return new RateDTO(0.00009315);
        }

    }

    private Transaction getTransactionStatus(Transaction t) {
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpEntity<?> request = formGetRequest(t.getSeller().getAuthToken());

        ResponseEntity<CGOrderFullDTO> response = restTemplate.exchange(this.CoinGateAPI + "/" + t.getOrderId(),
                HttpMethod.GET, request,
                CGOrderFullDTO.class);

        return Transaction.convertObjects(t, (CGOrderFullDTO) response.getBody());
    }


    private HttpEntity<?> formPostRequest(String authToken, Object obj) {
        if (!authToken.equals("")) {

            authToken = cryptoService.decrypt(authToken);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + authToken);

            return new HttpEntity<>(obj, headers);
        }

        return new HttpEntity<>(obj);
    }

    private HttpEntity<?> formGetRequest(String authToken) {

        authToken = cryptoService.decrypt(authToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);

        return new HttpEntity<>(headers);
    }


}
