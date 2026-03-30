package com.dbs.account.api.controller;

import com.dbs.account.application.dto.AccountResponse;
import com.dbs.account.application.mapper.AccountMapper;
import com.dbs.account.application.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/customer/{customerNumber}")
    public List<AccountResponse> getAccounts(@PathVariable String customerNumber) {
        var accounts = accountService.getAccountsByCustomerNumber(customerNumber);
        return accountMapper.toResponseList(accounts);
    }

    @PostMapping("/{iban}/deposit")
    public AccountResponse deposit(@PathVariable String iban, @RequestParam BigDecimal amount) {
        var account = accountService.depositMoney(iban, amount);
        return accountMapper.toResponse(account);
    }
}