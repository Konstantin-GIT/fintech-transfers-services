package com.workout.grpc;

import com.workout.example.Echo;
import com.workout.example.EchoServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class EchoService extends  EchoServiceGrpc.EchoServiceImplBase {
    @Override
    public void sayHello(Echo.Message request, StreamObserver<Echo.Message> responseObserver) {
        // super.sayHello(request, responseObserver);
        responseObserver.onNext(
            Echo.Message.newBuilder().setText(new StringBuilder(request.getText().toUpperCase()).toString())
                .build()
        );

        responseObserver.onCompleted();

    }
}
