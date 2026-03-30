package com.dbs.account.application.service.impl;

import com.dbs.account.application.service.AccountService;
import com.dbs.account.domain.model.Account;
import com.dbs.account.domain.model.Currency;
import com.dbs.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountManager implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account createDefaultAccount(String customerNumber) {
        log.info("Creating default TRY account for customer: {}", customerNumber);

        String generatedIban = generateFakeIban();

        Account account = Account.builder()
                .customerNumber(customerNumber)
                .iban(generatedIban)
                .balance(BigDecimal.ZERO)
                .currency(Currency.TRY)
                .build();

        return accountRepository.save(account);
    }

    private String generateFakeIban() {
        Random random = new Random();
        StringBuilder iban = new StringBuilder("TR");
        for (int i = 0; i < 24; i++) {
            iban.append(random.nextInt(10));
        }
        return iban.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomerNumber(String customerNumber) {
        return accountRepository.findAllByCustomerNumber(customerNumber);
    }

    @Override
    @Transactional
    public Account depositMoney(String iban, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with IBAN: " + iban));

        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }
}