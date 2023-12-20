package com.workout;

import com.workout.dto.TransferDto;
import com.workout.model.Transfer;
import com.workout.repository.TransferRepository;
import com.workout.service.TransferService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.assertj.core.api.Assertions;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {TransferControllerTest.Initializer.class})
@Testcontainers
public class TransferControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferService transferService;

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
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/api/transfers/welcome"))
            .andExpect(status().isOk())
            .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to fintech-transfer-grpc service");
    }

    @Test
    public void testIndex() throws Exception {

        Transfer transfer = new Transfer();
        transfer.setCreditAccountCode("code1");
        transfer.setDebitAccountCode("code2");
        transfer.setTransferStatus("started");
        transfer.setTransferAmount(BigDecimal.valueOf(10));
        transferRepository.save(transfer);

        var response = mockMvc.perform(get("/api/transfers"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        final List<Transfer> transfers = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<Transfer> expected = transferRepository.findAll();

        Assertions.assertThat(transfers).containsAll(expected);

    }

    @Test
    public void createdTransfer() throws Exception {

        TransferDto transferDto = new TransferDto();
        transferDto.setCreditAccountCode("code1");
        transferDto.setDebitAccountCode("code2");
        transferDto.setTransferAmount("9999.00");


     /*   var response = mockMvc.perform(get("/api/transfers")
            .content(MAPPER.writeValueAsString(transferDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse();
*/
        final Transfer createdTransfer = transferService.createTransfer(transferDto, "started");

     /*   final Transfer createdTransfer = fromJson(response.getContentAsString(), new TypeReference<>() { });*/
        final Transfer expected = transferRepository.findById(createdTransfer.getId()).get();

        Assertions.assertThat(createdTransfer).isEqualTo(expected);
    }

    public static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}