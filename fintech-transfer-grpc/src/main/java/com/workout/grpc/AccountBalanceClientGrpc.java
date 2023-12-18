package com.workout.grpc;

import com.workout.example.AccountBalance;
import com.workout.example.AccountBalanceServiceGrpc;
import com.workout.example.AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class AccountBalanceClientGrpc {

    public String changeAccountBalance(String codeAccount, String transferAmount, String transferId ) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("fintech-account-grpc", 6566)
            .usePlaintext()
            .build();

        AccountBalanceServiceBlockingStub accountBalanceServiceBlockingStub = AccountBalanceServiceGrpc.newBlockingStub(channel);

        AccountBalance.MessageRequest request = AccountBalance.MessageRequest.newBuilder()
            .setCodeAccount(codeAccount)
            .setAmountOfBalanceChange(transferAmount)
            .setTransferId(transferId)
            .build();

        AccountBalance.MessageResponse response = accountBalanceServiceBlockingStub.changeAccountBalance(request);
        System.out.println("Response from SERVER method changeAccountBalance =  " + response.getStatus());

        channel.shutdown();

        return response.getStatus();
    }
}

