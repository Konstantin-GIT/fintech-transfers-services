package com.workout.dto;

import jakarta.persistence.Column;
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
    @Column(name = "balance", nullable = false, columnDefinition = "NUMERIC(19, 2) DEFAULT 0.00")
    private BigDecimal balance;
}