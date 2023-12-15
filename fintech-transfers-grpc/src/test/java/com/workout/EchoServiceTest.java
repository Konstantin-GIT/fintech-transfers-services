package com.workout;


import com.workout.example.Echo;
import com.workout.example.EchoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "grpc.port=0")
public class EchoServiceTest {
    @LocalRunningGrpcPort
    int port;

    @Test
    void echoTest() {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port)
            .usePlaintext()
            .build();
        final Echo.Message response = EchoServiceGrpc.newBlockingStub(channel)
            .sayHello(Echo.Message.newBuilder()
                .setText("Hello")
                .build());
        assertEquals("HELLO", response.getText());

    }

}
