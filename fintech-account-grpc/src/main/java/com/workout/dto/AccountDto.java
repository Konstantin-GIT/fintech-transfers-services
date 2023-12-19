package com.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @NotBlank
    @Size(min = 1)
    private String code;

    @NotBlank
    @Size(min = 1)
    private BigDecimal balance;
}