package com.workout.service;

import com.workout.dto.TransferDto;
import com.workout.model.Transfer;

import java.util.List;

public interface TransferService {
    Transfer createPayment(TransferDto transferDto);
    List<TransferDto> getTransfers();
    Transfer createTransfer(TransferDto transferDto);


}
