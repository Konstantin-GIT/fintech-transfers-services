package com.workout.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Data
@EqualsAndHashCode(of = {"id", "debitAccountCode", "creditAccountCode", "transferAmount"})
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "debit_account_code", nullable = false)
    @Size(min = 1)
    private String debitAccountCode;

    @NotBlank
    @Column(name = "credit_account_code", nullable = false)
    @Size(min = 1)
    private String creditAccountCode;

    @Column(name = "transfer_amount", nullable = false)
    private BigDecimal transferAmount;

    @Column(name = "transfer_status", nullable = true)
    private String transferStatus;

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

}
