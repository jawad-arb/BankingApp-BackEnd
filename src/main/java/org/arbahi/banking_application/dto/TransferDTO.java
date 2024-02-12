package org.arbahi.banking_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Debit : دَين
 * credit : ائتمان
 * samir --> jawad
 */
public class TransferDTO {
    private String debitedAccountNumber;
    private String creditedAccountNumber;
    private BigDecimal amount;
}
