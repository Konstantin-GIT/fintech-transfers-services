package com.workout.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = {"id", "debitAccountCode", "creditAccountCode", "transferAmount"})
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

}