package com.workout;

import com.workout.dto.AccountDto;
import com.workout.example.Echo;
import com.workout.example.EchoServiceGrpc;
import com.workout.model.Account;
import com.workout.repository.AccountRepository;
import com.workout.service.AccountService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {AccountControllerTest.Initializer.class})
@Testcontainers
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

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

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/api/accounts/welcome"))
            .andExpect(status().isOk())
            .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to fintech-account-grpc service");
    }

    @Test
    public void testIndex() throws Exception {

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
        account.setCode("987987");
        accountRepository.save(account);

        var response = mockMvc.perform(get("/api/accounts"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        final List<Account> tasks = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<Account> expected = accountRepository.findAll();

        Assertions.assertThat(tasks).containsAll(expected);

    }

    @Test
    public void createdAccount() throws Exception {

        AccountDto accountDto = new AccountDto();
        accountDto.setCode("code1");
        accountDto.setAmountOfchange("1000.00");

        final Account createdAccount = accountService.createAccount(accountDto);

        final Account expected = accountRepository.findById(createdAccount.getId()).get();

        Assertions.assertThat(createdAccount).isEqualTo(expected);
    }


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

    public static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}