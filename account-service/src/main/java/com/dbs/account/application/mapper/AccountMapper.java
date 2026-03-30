package com.dbs.account.application.mapper;

import com.dbs.account.application.dto.AccountResponse;
import com.dbs.account.domain.model.Account;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
    List<AccountResponse> toResponseList(List<Account> accounts);
}