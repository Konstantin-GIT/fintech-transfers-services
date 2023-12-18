package com.workout.grpc;

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

import java.sql.SQLException;

@GRpcService
public class AccountBalanceServerGrpc extends AccountBalanceServiceGrpc.AccountBalanceServiceImplBase {

@Autowired
private AccountService accountService;

    @Override
    public void changeAccountBalance(AccountBalance.MessageRequest request, StreamObserver<AccountBalance.MessageResponse> responseObserver) {
     /*    try {
             String codeAccountStr = request.getCodeAccount();
             BigDecimal codeAccount = new BigDecimal(codeAccountStr);
         } catch (NumberFormatException e) {
             System.err.println("Ошибка при конвертации строки в BigDecimal: " + e.getMessage());
         }
         */


        String code = request.getCodeAccount();
        String balance = request.getAmountOfBalanceChange();

        AccountDto accountDto = new AccountDto();
        accountDto.setCode(code);
        accountDto.setAmountOfchange(balance);

        Account createdAccount = accountService.createAccount(accountDto);


        int codeAccount = Integer.parseInt(request.getCodeAccount());
        if (100 > codeAccount) {
            // Отправка ошибки, если код меньше 100
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("CodeAccount must be greater than or equal to 100")
                    .asRuntimeException()
            );
        } else {
            responseObserver.onNext(
                AccountBalance.MessageResponse.newBuilder()
                    .setStatus("created account with id = " + createdAccount.getId()
                                + " code = " + createdAccount.getCode()
                                + " balance = " + createdAccount.getBalance())
                    .build()
            );
            responseObserver.onCompleted();
        }

    }


    }

