package com.sep.bank.repository;

import com.sep.bank.model.Bank;
import com.sep.bank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    BankAccount findOneById(Long id);

    @Query("select bank_account from BankAccount bank_account where bank_account.pan = :pan and bank_account.serviceCode = :serviceCode" +
            " and  bank_account.cardholderName = :cardholderName and bank_account.expirationDate = :expirationDate")
    BankAccount validate(@Param("pan") String pan,@Param("serviceCode") String serviceCode,
                        @Param("cardholderName") String cardholderName, @Param("expirationDate") Date expirationDate);

}
