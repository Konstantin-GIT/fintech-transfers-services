package com.workout;

import com.workout.example.AccountBalance;
import com.workout.example.AccountBalanceServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService

public class AccountService extends AccountBalanceServiceGrpc.AccountBalanceServiceImplBase {


    @Override
    public void changeAccountBalance(AccountBalance.MessageRequest request, StreamObserver<AccountBalance.MessageResponse> responseObserver) {
     /*    try {
             String codeAccountStr = request.getCodeAccount();
             BigDecimal codeAccount = new BigDecimal(codeAccountStr);
         } catch (NumberFormatException e) {
             System.err.println("Ошибка при конвертации строки в BigDecimal: " + e.getMessage());
         }
*/
        int codeAccount = Integer.parseInt(request.getCodeAccount());
        if (100 > codeAccount) {
            // Отправка ошибки, если код меньше 100
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("CodeAccount must be greater than or equal to 100")
                    .asRuntimeException()
            );
        } else {
            // В противном случае, отправка успешного ответа
            responseObserver.onNext(
                AccountBalance.MessageResponse.newBuilder().setStatus(codeAccount + " Status 200").build()
            );
            responseObserver.onCompleted();
        }

    }

/*
        @Override
        public void changeAccountBalance(AccountBalance.MessageRequest request, StreamObserver<AccountBalance.MessageResponse> responseObserver) {
            // super.sayHello(request, responseObserver);
            responseObserver.onNext(
                AccountBalance.MessageResponse.newBuilder().setStatus(new StringBuilder(request.getCodeAccount()).toString() + " Status 200")
                    .build()
            );

            responseObserver.onCompleted();

        }
        */

    }

