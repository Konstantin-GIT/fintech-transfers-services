package com.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentDto {

    @NotBlank
    @Size(min = 1)
    private String debitAccount;

    @NotBlank
    @Size(min = 1)
    private String creditAccount;

    @NotBlank
    @Size(min = 1)
    private String transferAmount;


}