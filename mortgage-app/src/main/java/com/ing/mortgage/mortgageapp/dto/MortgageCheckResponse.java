package com.ing.mortgage.mortgageapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for Mortgage check response
 */
@Data
@AllArgsConstructor
public class MortgageCheckResponse {
    private boolean feasible;
    private BigDecimal monthlyCost;
}
