package com.dbs.account.application.service;

import com.dbs.account.application.dto.MoneyTransferRequest;
import com.dbs.account.domain.model.Account;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createDefaultAccount(String customerNumber);
    List<Account> getAccountsByCustomerNumber(String customerNumber);
    Account depositMoney(String iban, BigDecimal amount);
    void transferMoney(MoneyTransferRequest request);
}