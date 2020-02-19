package com.sep.bitcoinservice.service.contracts;

import com.sep.bitcoinservice.dto.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public interface ITransactionService {

    TransactionDTO getTransaction(long id);

    List<TransactionDTO> getTransactions();

    TransactionDTO createPayment(OrderDTO order) throws InstanceAlreadyExistsException;

    TransactionStatusDTO getTransactionStatusDto(long id);

    RateDTO getExchangeRate(String from, String to);

    FinalizeOrderDTO getOrderStatus(long activeOrderId);
}
