package com.workout.service;

import com.workout.dto.AccountDto;
import com.workout.model.Account;
import com.workout.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Override
    @Transactional
    public Account createAccount(AccountDto accountDto) {
        var account = fromDto(accountDto);
        return accountRepository.save(account);
    }

    private Account fromDto(AccountDto accountDto) {
        return Account.builder()
            .balance(new BigDecimal(accountDto.getAmountOfchange()))
            .code(accountDto.getCode())
            .build();
    }
}
