package com.workout.service;

import com.workout.dto.AccountDto;
import com.workout.exception.AccountNotFoundException;
import com.workout.model.Account;
import com.workout.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Optional;


@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account createAccount(AccountDto accountDto) {
        var account = fromDto(accountDto);
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(AccountDto accountDto, Long id) {
        Account account =  accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));
        account.setBalance(accountDto.getBalance());

        return accountRepository.save(account);
    }

    public Optional<Account> getAccountByCode(String code) {
       return accountRepository.findByCode(code);
    }


    private Account fromDto(AccountDto accountDto) {
        return Account.builder()
            .balance(accountDto.getBalance())
            .code(accountDto.getCode())
            .build();
    }
}
