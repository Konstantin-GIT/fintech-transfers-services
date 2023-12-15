package com.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountBalanceDto {

    @NotBlank
    @Size(min = 1)
    private String code;

    @NotBlank
    @Size(min = 1)
    private String amountOfchange;
}