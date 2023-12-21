package com.workout.service;

import com.workout.dto.TransferDto;
import com.workout.grpc.AccountBalanceClientGrpc;
import com.workout.model.Transfer;
import com.workout.repository.TransferRepository;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.workout.exception.TransferFailedException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.workout.service.Utils.containsOnlyDigitsAndNotEmpty;
import static io.grpc.Status.fromThrowable;

@AllArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {

    private final AccountBalanceClientGrpc accountBalanceClientGrpc;

    private final TransferRepository transferRepository;

    @Override
    public Transfer createPayment(TransferDto transferDto)
    {

        String transferAmount = transferDto.getTransferAmount();
        String debitAccountCode = transferDto.getDebitAccountCode();
        String creditAccountCode = transferDto.getCreditAccountCode();
        String transferId = "transferId";

        if (!containsOnlyDigitsAndNotEmpty(transferAmount)) {
            throw new TransferFailedException("An incorrect transferId transfer amount was entered");
        }

        String debitAmount = toDebitAmount(transferAmount);
        String creditAmount = transferAmount;

        try {
            accountBalanceClientGrpc.changeAccountBalance(debitAccountCode, debitAmount, transferId);

            try {
                accountBalanceClientGrpc.changeAccountBalance(creditAccountCode, creditAmount, transferId);

            } catch (StatusRuntimeException e) {
                rollbackDebitOperation(debitAccountCode, transferAmount, transferId);
                throw new TransferFailedException(fromThrowable(e).getDescription());
            }

        } catch (StatusRuntimeException e) {
            throw new TransferFailedException(fromThrowable(e).getDescription());

        }

        return  createTransfer(transferDto);


    }

    private void rollbackDebitOperation(String debitAccountCode, String transferAmount, String transferId) {
        try {
            accountBalanceClientGrpc.changeAccountBalance(debitAccountCode, transferAmount, transferId);
        } catch (StatusRuntimeException e) {
            throw  new TransferFailedException(fromThrowable(e).getDescription());
        }
    }


    public Transfer createTransfer(TransferDto transferDto) {
        var transfer = fromDto(transferDto);
        return transferRepository.save(transfer);

    }

    private Transfer fromDto(TransferDto transferDto) {
        return Transfer.builder()
            .debitAccountCode(transferDto.getDebitAccountCode())
            .creditAccountCode(transferDto.getCreditAccountCode())
            .transferAmount(new BigDecimal(transferDto.getTransferAmount()))
            .build();
    }

    private TransferDto toDto(Transfer transfer) {
        return TransferDto.builder()
            .debitAccountCode(transfer.getDebitAccountCode())
            .creditAccountCode(transfer.getCreditAccountCode())
            .transferAmount(transfer.getTransferAmount().toString())
            .creationDate(transfer.getCreationDate())
            .build();
    }


    private String toDebitAmount(String transferAmount) {

        return "-" + transferAmount;
    }

    @Override
    public List<TransferDto> getTransfers() {
        List<Transfer> transfers = transferRepository.findAll();

        return transfers.stream().filter(Objects::nonNull).map(t -> toDto(t))
            .collect(Collectors.toList());

    }
}
