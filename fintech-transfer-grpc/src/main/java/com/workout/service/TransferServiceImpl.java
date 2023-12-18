package com.workout.service;

import com.workout.dto.TransferDto;
import com.workout.grpc.AccountBalanceClientGrpc;
import com.workout.model.Transfer;
import com.workout.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {

    private final AccountBalanceClientGrpc accountBalanceClientGrpc;

    private final TransferRepository transferRepository;
    @Override
    public void createPayment(String debitAccountCode, String creditAccountCode, String transferAmount, String transferId) {

        String resultDebitAccount = accountBalanceClientGrpc.changeAccountBalance(debitAccountCode, transferAmount, transferId);

        String resultCreditAccount = accountBalanceClientGrpc.changeAccountBalance(creditAccountCode, transferAmount, transferId);

        System.out.println("resultDebitAccount = " + resultDebitAccount);

        System.out.println("resultCreditAccount = " + resultCreditAccount);
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
    @Override
    public List<Transfer> getTransfers() {
        return transferRepository.findAll();
    }
}
