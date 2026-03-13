package com.ing.mortgage.mortgageapp.model;

import java.math.BigDecimal;

/**
 * Domain model for mortgage check response
 */
public record MortgageCheckResult(boolean feasible, BigDecimal monthlyCost) {

}
