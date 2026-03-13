package com.ing.mortgage.mortgageapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model for Mortgage rates
 */
public record MortgageRate(int maturityPeriod, BigDecimal interestRate, LocalDateTime lastUpdate) {
}
