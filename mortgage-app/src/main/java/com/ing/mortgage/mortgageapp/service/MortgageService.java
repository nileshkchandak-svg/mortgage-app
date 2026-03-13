package com.ing.mortgage.mortgageapp.service;

import com.ing.mortgage.mortgageapp.config.InterestRateProperties;
import com.ing.mortgage.mortgageapp.exception.MortgageValidationException;
import com.ing.mortgage.mortgageapp.model.MortgageCheckInput;
import com.ing.mortgage.mortgageapp.model.MortgageCheckResult;
import com.ing.mortgage.mortgageapp.model.MortgageRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Mortgage service
 * 1. to return interest rate
 *  * 2. To return Mortgage eligibility
 */
@Service
@Slf4j
public class MortgageService {
    private final List<MortgageRate> rates;

    public MortgageService(InterestRateProperties properties) {
        // Convert YAML config → domain model
        this.rates = properties.toDomainRates();
    }

    //  return all current interest rates
    public List<MortgageRate> getCurrentRates() {
        return rates;
    }
    //check the mortgage eligibility
    public MortgageCheckResult checkMortgage(MortgageCheckInput input) {

       validateBusinessRules(input);

        MortgageRate rate = rates.stream()
                .filter(r -> r.maturityPeriod() == input.maturityPeriod())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No rate found"));

        BigDecimal annualRate = rate.interestRate();
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        //calculations for monthly emi
        int months = input.maturityPeriod() * 12;

        // numerator = P * r
        BigDecimal numerator = input.loanValue().multiply(monthlyRate);

        // denominator = 1 - (1+r)^(-n)
        BigDecimal base = BigDecimal.ONE.add(monthlyRate);
        BigDecimal power = base.pow(-months, MathContext.DECIMAL64); // negative exponent
        BigDecimal denominator = BigDecimal.ONE.subtract(power);

        BigDecimal monthlyCost = numerator
                .divide(denominator, 2,RoundingMode.HALF_UP);


        return new MortgageCheckResult(true, monthlyCost);
    }

    // Validate business rules
        private void validateBusinessRules(MortgageCheckInput input) {

        // loanValue > 4 * income
        BigDecimal maxLoan = input.income().multiply(BigDecimal.valueOf(4));
        if (input.loanValue().compareTo(maxLoan) > 0) {
            log.warn("Business rule violated: loanValue > 4 * income");
            throw new MortgageValidationException("business.loan.multiplier");
        }

        // loanValue > homeValue
        if (input.loanValue().compareTo(input.homeValue()) > 0) {
            log.warn("Business rule violated: loanValue > homeValue");
            throw new MortgageValidationException("business.loan.homevalue");
        }


        boolean maturityExists = rates.stream()
                .anyMatch(r -> r.maturityPeriod() == input.maturityPeriod());

        if (!maturityExists) {
            throw new MortgageValidationException("business.maturity.notfound");
        }
    }



}
