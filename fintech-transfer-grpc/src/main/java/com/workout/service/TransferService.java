package com.workout.service;

import com.workout.model.Transfer;

import java.util.List;

public interface TransferService {
    void createPayment(String debitAccount, String creditAccount, String transferAmount);
    List<Transfer> getTransfers();


}
