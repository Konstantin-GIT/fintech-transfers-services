package com.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    @NotBlank
    @Size(min = 1)
    private String debitAccountCode;

    @NotBlank
    @Size(min = 1)
    private String creditAccountCode;

    @NotBlank
    @Size(min = 1)
    private String transferAmount;



}