package com.ing.mortgage.mortgageapp.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for interest rate
 */
public class InterestRateResponse {
    @Getter
    private final int maturityPeriod;
    @Getter
    private final BigDecimal interestRate;
    @Getter
    private final LocalDateTime lastUpdate;

    public InterestRateResponse(int maturityPeriod, BigDecimal interestRate, LocalDateTime lastUpdate) {
        this.maturityPeriod = maturityPeriod;
        this.interestRate = interestRate;
        this.lastUpdate = lastUpdate;
    }

}
