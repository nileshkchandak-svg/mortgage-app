package com.ing.mortgage.mortgageapp.model;


import java.math.BigDecimal;

/**
 * Domain model for Mortgage check input
 */
public record MortgageCheckInput(BigDecimal income, int maturityPeriod, BigDecimal loanValue, BigDecimal homeValue) {

}
