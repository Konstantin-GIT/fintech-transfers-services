package com.workout.controller;

import com.workout.dto.TransferDto;
import com.workout.exception.TransferFailedException;
import com.workout.service.TransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import static com.workout.controller.TransferController.TRANSFER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
    public List<TransferDto> index() {
        return transferService.getTransfers();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void createPayment(@RequestBody @Valid TransferDto transferDto) throws RuntimeException {
        try {
            transferService.createPayment(transferDto);

        } catch (RuntimeException e) {

            throw new TransferFailedException(e.getMessage());
        }
    }


}
