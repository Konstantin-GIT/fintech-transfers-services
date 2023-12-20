package com.workout.exception;

import com.workout.example.AccountBalance;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class ErrorHandler {

    public static void handleError(StreamObserver<AccountBalance.MessageResponse> responseObserver, Status status, String description) {
        responseObserver.onError(status.withDescription(description).asRuntimeException());
        responseObserver.onCompleted();
    }
}