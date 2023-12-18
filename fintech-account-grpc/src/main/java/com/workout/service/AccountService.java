package com.workout.service;

import com.workout.dto.AccountDto;
import com.workout.model.Account;

public interface AccountService {
    Account createAccount(AccountDto accountDto);
}
