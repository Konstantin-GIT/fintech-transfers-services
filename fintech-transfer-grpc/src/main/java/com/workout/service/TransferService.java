package com.workout.service;

import com.workout.dto.TransferDto;
import com.workout.model.Transfer;

import java.util.List;

public interface TransferService {
    String createPayment(String debitAccount, String creditAccount, String transferAmount, String transferId);
    List<Transfer> getTransfers();
    Transfer createTransfer(TransferDto transferDto, String transferStatus);


}
