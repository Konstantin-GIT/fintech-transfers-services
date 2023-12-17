package com.workout.controller;

import com.workout.dto.PaymentDto;
import com.workout.model.Transfer;
import com.workout.service.TransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import static com.workout.controller.TransferController.TRANSFER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("${base-url}" + TRANSFER_CONTROLLER_PATH)
public class TransferController {

    public static final String TRANSFER_CONTROLLER_PATH = "/payments";

    @Qualifier("TransferServiceImpl")
    private final TransferService transferService;


    @GetMapping("/welcome")
    @ResponseStatus(OK)
    public String getGreeting() {
        return "Welcome to fintech-transfer-grpc service";
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Transfer> index() {
         System.out.println("TEST !!!!!!!!!!!!!!!!!!!!!!!!!!" + transferService.getTransfers());
        return transferService.getTransfers();
    }

    @PutMapping
    @ResponseStatus(OK)
    public String createPayment(@RequestBody @Valid PaymentDto paymentDto) {
        transferService.createPayment(paymentDto.getDebitAccount(), paymentDto.getCreditAccount(), paymentDto.getTransferAmount());
        return "complied method createPayment";
    }


}
