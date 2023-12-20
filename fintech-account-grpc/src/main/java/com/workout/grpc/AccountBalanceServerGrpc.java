package com.workout.grpc;

import static com.workout.exception.ErrorHandler.handleError;

import com.workout.dto.AccountDto;
import com.workout.example.AccountBalance;
import com.workout.example.AccountBalanceServiceGrpc;
import com.workout.model.Account;
import com.workout.repository.AccountRepository;
import com.workout.service.AccountService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static com.workout.grpc.Utils.containsOnlyDigitsAndNotEmpty;

@GRpcService
public class AccountBalanceServerGrpc extends AccountBalanceServiceGrpc.AccountBalanceServiceImplBase {

@Autowired
private AccountService accountService;

    @Override
    public void changeAccountBalance(AccountBalance.MessageRequest request, StreamObserver<AccountBalance.MessageResponse> responseObserver) {
        String code = request.getCodeAccount();
        String amountOfBalanceChange = request.getAmountOfBalanceChange();
        Optional<Account> accountUpdate = accountService.getAccountByCode(code);

        if (accountUpdate.isEmpty()) {
            handleError(responseObserver, Status.NOT_FOUND, "The account with code = " + code + " does not exist.");
            return;
        }

        if (!containsOnlyDigitsAndNotEmpty(amountOfBalanceChange)) {
            handleError(responseObserver, Status.INVALID_ARGUMENT, "For an account with code = " + code + ", the debit amount has an incorrect value.");
            return;
        }

        BigDecimal balanceUpdate = accountUpdate.get().getBalance();
        Long accountUpdateId = accountUpdate.get().getId();

        BigDecimal updatedBalance = balanceUpdate.add(new BigDecimal(amountOfBalanceChange));

        if (!(updatedBalance.compareTo(BigDecimal.ZERO) >= 0)) {
            handleError(responseObserver, Status.RESOURCE_EXHAUSTED, "There is not enough money in the account with the code = " + code + ".");
            return;
        }

        AccountDto accountDto = new AccountDto();
        accountDto.setCode(code);
        accountDto.setBalance(updatedBalance);

        Account updatedAccount = accountService.updateAccount(accountDto, accountUpdateId);

        responseObserver.onNext(
            AccountBalance.MessageResponse.newBuilder()
                .setStatus(Status.OK.getCode().name())
                .setMessage("Updated account with code = " + updatedAccount.getCode() + " balance = " + updatedAccount.getBalance())
                .build()
        );
        responseObserver.onCompleted();
    }


    }


