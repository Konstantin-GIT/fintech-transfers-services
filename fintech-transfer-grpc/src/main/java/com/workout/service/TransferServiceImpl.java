package com.workout.service;

import com.workout.dto.TransferDto;
import com.workout.grpc.AccountBalanceClientGrpc;
import com.workout.model.Transfer;
import com.workout.repository.TransferRepository;
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
    public void createPayment(String debitAccountCode, String creditAccountCode, String transferAmount, String transferId) {
        if (!containsOnlyDigitsAndNotEmpty(transferAmount)) {
            throw new RuntimeException("Not correct transferAmount ");
        }
        String debitAmount = toDebitAmount(transferAmount);
        String creditAmount = transferAmount;

        try {
            String resultDebitAccount = accountBalanceClientGrpc.changeAccountBalance(debitAccountCode, debitAmount, transferId);
        } catch (Exception e) {
            throw new RuntimeException("First method failed");
        }
        try {
            String resultCreditAccount = accountBalanceClientGrpc.changeAccountBalance(creditAccountCode, creditAmount, transferId);
        } catch ( Exception e) {
            accountBalanceClientGrpc.changeAccountBalance(debitAccountCode, transferAmount, transferId);
            System.out.println("!!!!!!!!!!!!!! ROLLBACK  DEBIT CODE");
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
