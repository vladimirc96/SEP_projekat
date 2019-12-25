package com.sep.bitcoinservice.service.contracts;

import com.sep.bitcoinservice.dto.OrderDTO;
import com.sep.bitcoinservice.dto.TransactionDTO;
import com.sep.bitcoinservice.dto.TransactionStatusDTO;
import com.sep.bitcoinservice.enums.Enums;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public interface ITransactionService {

    TransactionDTO getTransaction(long id);

    List<TransactionDTO> getTransactions();

    TransactionDTO createPayment(OrderDTO order) throws InstanceAlreadyExistsException;

    TransactionStatusDTO getTransactionStatusDto(long id);


}