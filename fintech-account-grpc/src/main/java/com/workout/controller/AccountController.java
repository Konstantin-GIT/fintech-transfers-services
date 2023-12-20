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
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("${base-url}" + ACCOUNT_CONTROLLER_PATH)
public class AccountController {
    public static final String ACCOUNT_CONTROLLER_PATH = "/accounts";
    private final AccountRepository accountRepository;

    @GetMapping("/welcome")
    @ResponseStatus(OK)
    public String getGreeting() {
          return "Welcome to fintech-account-grpc service";
    }

    @GetMapping()
    @ResponseStatus(OK)
    public List<Account> index() {
        //  return "null";
        System.out.println("accountRepository.findAll() = " + accountRepository.findAll());

        return accountRepository.findAll();
    }
}
