package com.workout.service;

import com.workout.dto.AccountDto;
import com.workout.model.Account;

import java.util.Optional;

public interface AccountService {
    Account createAccount(AccountDto accountDto);

    Account updateAccount(AccountDto accountDto, Long id);

    Optional<Account> getAccountByCode(String code);
}
