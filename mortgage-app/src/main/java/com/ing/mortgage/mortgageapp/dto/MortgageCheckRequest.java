package com.ing.mortgage.mortgageapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO Mortgage check request
 */

@Data
public class MortgageCheckRequest {
    @NotNull(message = "validation.income.required")
    @Positive(message = "validation.income.positive")
    private BigDecimal income;
    @NotNull(message = "validation.maturity.required")
    @Min(value = 1, message = "validation.maturity.min")
    private Integer maturityPeriod;
    @NotNull(message = "validation.loan.required")
    @Positive(message = "validation.loan.positive")
    private BigDecimal loanValue;
    @NotNull(message = "validation.home.required")
    @Positive(message = "validation.home.positive")
    private BigDecimal homeValue;
}
