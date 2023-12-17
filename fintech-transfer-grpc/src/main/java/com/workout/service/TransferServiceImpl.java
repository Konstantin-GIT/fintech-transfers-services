package com.workout.service;

import com.workout.grpc.AccountBalanceServiceClient;
import com.workout.model.Transfer;
import com.workout.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {

    private final AccountBalanceServiceClient accountBalanceServiceClient;

    private final TransferRepository transferRepository;
    @Override
    public void createPayment(String debitAccount, String creditAccount, String transferAmount) {

        String resultDebitAccount = accountBalanceServiceClient.changeAccountBalance(debitAccount, transferAmount );

        String resultCreditAccount = accountBalanceServiceClient.changeAccountBalance(creditAccount, transferAmount );







        System.out.println("resultDebitAccount = " + resultDebitAccount);

        System.out.println("resultCreditAccount = " + resultCreditAccount);

    }

    @Override
    public List<Transfer> getTransfers() {
        return transferRepository.findAll();
    }
}
