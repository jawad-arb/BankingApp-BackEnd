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
 * add the amount to the balance of the accountNumber
 */
public class CreditDebitDTO {
    private String accountNumber;
    private BigDecimal amount;
}
