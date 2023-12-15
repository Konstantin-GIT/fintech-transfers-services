package com.workout.grpc;

import com.workout.example.AccountBalance;
import com.workout.example.AccountBalanceServiceGrpc;
import com.workout.example.AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class AccountBalanceServiceClient {

    public String changeAccountBalance(String codeAccount, String transferAmount) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("fintech-account-grpc", 6566)
            .usePlaintext()
            .build();

        AccountBalanceServiceBlockingStub accountBalanceServiceBlockingStub = AccountBalanceServiceGrpc.newBlockingStub(channel);

        // Создаем объект запроса
        AccountBalance.MessageRequest request = AccountBalance.MessageRequest.newBuilder()
            .setCodeAccount(codeAccount)
            .setAmountOfBalanceChange(transferAmount)
            .build();

        // Вызываем удаленный метод и получаем ответ
        AccountBalance.MessageResponse response = accountBalanceServiceBlockingStub.changeAccountBalance(request);
        System.out.println("Response from SERVER method changeAccountBalance =  " + response.getStatus());

        channel.shutdown();
        // Возвращаем строковое представление ответа
        return response.getStatus();
    }
}

