package com.workout;
import com.workout.example.AccountBalance;
import com.workout.example.AccountBalanceServiceGrpc;
import com.workout.example.Echo;
import com.workout.example.EchoServiceGrpc;
import com.workout.grpc.AccountBalanceServerGrpcImpl;
import com.workout.model.Account;
import com.workout.repository.AccountRepository;
import com.workout.service.AccountService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {AccountBalanceServerGrpcImplTest.Initializer.class})
//@TestPropertySource(locations = "classpath:test-application.yml")
@Testcontainers
public class AccountBalanceServerGrpcImplTest {
    private static final int PORT = 6566;
    private static ManagedChannel channel;

    @BeforeAll
    static void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", PORT)
            .usePlaintext()
            .build();
    }

    @BeforeEach
    public void setUpBeforeEach() {
        accountRepository.deleteAll();
    }

    @Autowired
    private AccountRepository accountRepository;
    @AfterAll
    static void tearDown() {
        // Закрываем канал gRPC после завершения тестов
        channel.shutdownNow();
    }

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("fintech-db")
        .withUsername("SU")
        .withPassword("PWD");
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                "spring.liquibase.enabled=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @Test
    void testChangeAccountBalance() {
        AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub stub =
            AccountBalanceServiceGrpc.newBlockingStub(channel);

        // Создаем аккаунт в БД
        Account account = new Account();
        account.setCode("your_account_code");
        account.setBalance(BigDecimal.valueOf(500)); // устанавливаем начальный баланс
        accountRepository.save(account);

        // Выполняем запрос на изменение баланса
        AccountBalance.MessageResponse response = stub.changeAccountBalance(
            AccountBalance.MessageRequest.newBuilder()
                .setCodeAccount("your_account_code")
                .setAmountOfBalanceChange("100")
                .build());

        // Проверяем, что ответ содержит ожидаемый статус
        assertEquals(Status.OK.getCode().name(), response.getStatus());
        assertEquals("Updated account with code = your_account_code balance = 600.00", response.getMessage());
    }

    @Test
    void testAccountNotFound() {
        AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub stub =
            AccountBalanceServiceGrpc.newBlockingStub(channel);

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            stub.changeAccountBalance(AccountBalance.MessageRequest.newBuilder()
                .setCodeAccount("nonexistent_account")
                .setAmountOfBalanceChange("100")
                .build());
        });

        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
        assertEquals("The account with code = nonexistent_account does not exist.",
            exception.getStatus().getDescription());
    }

    @Test
    void testInvalidDebitAmount() {
        AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub stub =
            AccountBalanceServiceGrpc.newBlockingStub(channel);

        Account account = new Account();
        account.setCode("your_account_code");
        account.setBalance(BigDecimal.valueOf(100)); // устанавливаем начальный баланс
        accountRepository.save(account);

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            stub.changeAccountBalance(AccountBalance.MessageRequest.newBuilder()
                .setCodeAccount("your_account_code")
                .setAmountOfBalanceChange("invalid_amount")
                .build());
        });

        assertEquals(Status.INVALID_ARGUMENT.getCode(), exception.getStatus().getCode());
        assertEquals("For an account with code = your_account_code, the debit amount has an incorrect value.",
            exception.getStatus().getDescription());
    }

    @Test
    void testInsufficientFunds() {
        AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub stub =
            AccountBalanceServiceGrpc.newBlockingStub(channel);

        Account account = new Account();
        account.setCode("insufficient_funds_account");
        account.setBalance(BigDecimal.valueOf(50));
        accountRepository.save(account);

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            stub.changeAccountBalance(AccountBalance.MessageRequest.newBuilder()
                .setCodeAccount("insufficient_funds_account")
                .setAmountOfBalanceChange("-100")
                .build());
        });

        assertEquals(Status.RESOURCE_EXHAUSTED.getCode(), exception.getStatus().getCode());
        assertEquals("There is not enough money in the account with the code = insufficient_funds_account.",
            exception.getStatus().getDescription());
    }


    @Test
    void echoTest() {
        final Echo.Message response = EchoServiceGrpc.newBlockingStub(channel)
            .sayHello(Echo.Message.newBuilder()
                .setText("Hello")
                .build());
        assertEquals("HELLO", response.getText());

    }

}
