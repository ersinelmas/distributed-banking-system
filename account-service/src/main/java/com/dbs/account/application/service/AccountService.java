package com.dbs.account.application.service;

import com.dbs.account.domain.model.Account;

import java.util.List;

public interface AccountService {
    Account createDefaultAccount(String customerNumber);
    List<Account> getAccountsByCustomerNumber(String customerNumber);
    Account depositMoney(String iban, java.math.BigDecimal amount);
}
