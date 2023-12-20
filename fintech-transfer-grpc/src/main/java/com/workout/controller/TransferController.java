package com.workout.controller;

import com.workout.dto.TransferDto;
import com.workout.exception.TransactionFailedException;
import com.workout.model.Transfer;
import com.workout.service.TransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import static com.workout.controller.TransferController.TRANSFER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import com.workout.exception.TransactionFailedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("${base-url}" + TRANSFER_CONTROLLER_PATH)
public class TransferController {

    public static final String TRANSFER_CONTROLLER_PATH = "/transfers";

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
         System.out.println("transferService.getTransfers() = " + transferService.getTransfers());
        return transferService.getTransfers();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void createPayment(@RequestBody @Valid TransferDto transferDto) throws RuntimeException {
        try {
            System.out.println("createPayment() transferDto = " + transferDto);

            Transfer createdTransfer = transferService.createTransfer(transferDto, "started");
            System.out.println("createPayment() -> createdTransfer = " + createdTransfer);

            String message = transferService.createPayment(transferDto.getDebitAccountCode(),
                transferDto.getCreditAccountCode(),
                transferDto.getTransferAmount(),
                createdTransfer.getId().toString()
            );
            System.out.println(message);

        } catch (RuntimeException e) {
            System.out.println(e);
            throw new TransactionFailedException("An error occurred while executing the transaction, please try again." + e.getMessage());
        }
    }

}
