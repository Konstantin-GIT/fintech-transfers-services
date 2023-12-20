package com.workout.grpc;

import com.workout.example.AccountBalance;
import io.grpc.stub.StreamObserver;

public interface AccountBalanceServer {

    void changeAccountBalance(AccountBalance.MessageRequest request, StreamObserver<AccountBalance.MessageResponse> responseObserver);
}
