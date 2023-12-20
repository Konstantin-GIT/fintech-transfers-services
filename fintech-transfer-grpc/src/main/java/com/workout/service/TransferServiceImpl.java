package com.workout.service;

import com.workout.dto.TransferDto;
import com.workout.grpc.AccountBalanceClientGrpc;
import com.workout.model.Transfer;
import com.workout.repository.TransferRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.workout.service.Utils.containsOnlyDigitsAndNotEmpty;

@AllArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {

    private final AccountBalanceClientGrpc accountBalanceClientGrpc;

    private final TransferRepository transferRepository;
    @Override
    public String createPayment(String debitAccountCode, String creditAccountCode, String transferAmount, String transferId) {
        if (!containsOnlyDigitsAndNotEmpty(transferAmount)) {
            return "Not correct transfer amount";
        }

        String debitAmount = toDebitAmount(transferAmount);
        String creditAmount = transferAmount;

        try {
            accountBalanceClientGrpc.changeAccountBalance(debitAccountCode, debitAmount, transferId);

            try {
                accountBalanceClientGrpc.changeAccountBalance(creditAccountCode, creditAmount, transferId);

            } catch (StatusRuntimeException e) {
                rollbackDebitOperation(debitAccountCode, transferAmount, transferId);
                System.out.println("--> Rollback for debit code");
                return "Credit operation failed" + e;
            }

        } catch (StatusRuntimeException e) {
            return "Debit operation failed" + e;
        }

        return "transfer created";


    }

    private void rollbackDebitOperation(String debitAccountCode, String transferAmount, String transferId) {
        try {
            accountBalanceClientGrpc.changeAccountBalance(debitAccountCode, transferAmount, transferId);
        } catch (StatusRuntimeException ex) {
        }
    }


    public Transfer createTransfer(TransferDto transferDto, String transferStatus) {
        var transfer = fromDto(transferDto);
        transfer.setTransferStatus(transferStatus);
        return transferRepository.save(transfer);

    }

    private Transfer fromDto(TransferDto transferDto) {
        return Transfer.builder()
            .debitAccountCode(transferDto.getDebitAccountCode())
            .creditAccountCode(transferDto.getCreditAccountCode())
            .transferAmount(new BigDecimal(transferDto.getTransferAmount()))
            .build();
    }

    private String toDebitAmount(String transferAmount) {

        return "-" + transferAmount;
    }

    @Override
    public List<Transfer> getTransfers() {
        return transferRepository.findAll();
    }
}
