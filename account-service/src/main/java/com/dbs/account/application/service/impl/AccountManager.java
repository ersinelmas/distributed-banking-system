package com.dbs.account.application.service.impl;

import com.dbs.account.application.dto.MoneyTransferRequest;
import com.dbs.account.application.service.AccountService;
import com.dbs.account.domain.event.MoneyTransferredEvent;
import com.dbs.account.domain.model.Account;
import com.dbs.account.domain.model.Currency;
import com.dbs.account.domain.repository.AccountRepository;
import com.dbs.account.infrastructure.kafka.producer.TransactionEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountManager implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionEventProducer transactionEventProducer;

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

        MoneyTransferredEvent event = new MoneyTransferredEvent(
                "CASH_DESK", iban, amount, "DEPOSIT", LocalDateTime.now()
        );
        transactionEventProducer.sendTransactionEvent(event);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void transferMoney(MoneyTransferRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        if (request.fromIban().equals(request.toIban())) {
            throw new RuntimeException("Sender and receiver accounts cannot be the same");
        }

        String firstLock = request.fromIban().compareTo(request.toIban()) < 0 ? request.fromIban() : request.toIban();
        String secondLock = firstLock.equals(request.fromIban()) ? request.toIban() : request.fromIban();

        log.info("Locking in order: {} then {}", firstLock, secondLock);

        accountRepository.findByIbanWithLock(firstLock)
                .orElseThrow(() -> new RuntimeException("Account not found: " + firstLock));
        accountRepository.findByIbanWithLock(secondLock)
                .orElseThrow(() -> new RuntimeException("Account not found: " + secondLock));

        Account fromAccount = accountRepository.findByIban(request.fromIban())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        Account toAccount = accountRepository.findByIban(request.toIban())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
        toAccount.setBalance(toAccount.getBalance().add(request.amount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Transfer of {} completed from {} to {}", request.amount(), request.fromIban(), request.toIban());

        MoneyTransferredEvent event = new MoneyTransferredEvent(
                request.fromIban(), request.toIban(), request.amount(), "MONEY_TRANSFER", LocalDateTime.now()
        );
        transactionEventProducer.sendTransactionEvent(event);
    }
}