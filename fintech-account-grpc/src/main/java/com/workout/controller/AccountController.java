package com.workout.controller;

import com.workout.model.Account;
import com.workout.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static com.workout.controller.AccountController.ACCOUNT_CONTROLLER_PATH;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + ACCOUNT_CONTROLLER_PATH)
public class AccountController {
    public static final String ACCOUNT_CONTROLLER_PATH = "/accounts";
    public static final String BALANCES = "/balances";
    private final AccountRepository accountRepository;

    @GetMapping(BALANCES)
    @ResponseStatus(OK)
    public List<Account> index() {
        //  return "null";
        return accountRepository.findAll();
    }
}
