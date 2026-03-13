package com.ing.mortgage.mortgageapp.service;

import com.ing.mortgage.mortgageapp.config.InterestRateProperties;
import com.ing.mortgage.mortgageapp.exception.MortgageValidationException;
import com.ing.mortgage.mortgageapp.model.MortgageCheckInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MortgageServiceTest {

    private MortgageService service;

    @BeforeEach
    void setup() {
        InterestRateProperties props = new InterestRateProperties();

        InterestRateProperties.RateConfig rate30 = new InterestRateProperties.RateConfig();
        rate30.setMaturityPeriod(30);
        rate30.setInterestRate(new BigDecimal("5.0"));
        rate30.setLastUpdate(LocalDateTime.now());

        InterestRateProperties.RateConfig rate20 = new InterestRateProperties.RateConfig();
        rate20.setMaturityPeriod(20);
        rate20.setInterestRate(new BigDecimal("4.5"));
        rate20.setLastUpdate(LocalDateTime.now());

        props.setInterestRates(List.of(rate30, rate20));

        service = new MortgageService(props);
    }

    @Test
    void testMortgageFeasible() {
        MortgageCheckInput input = new MortgageCheckInput(
                new BigDecimal("50000"),   // income
                30,
                new BigDecimal("150000"),  // loanValue < 4 * income
                new BigDecimal("300000")
        );

        var result = service.checkMortgage(input);

        assertTrue(result.feasible());
        assertNotNull(result.monthlyCost());
        assertTrue(result.monthlyCost().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testLoanExceedsIncomeRule() {
        MortgageCheckInput input = new MortgageCheckInput(
                new BigDecimal("3000"),
                30,
                new BigDecimal("200000"), // > 4 * income
                new BigDecimal("250000")
        );

        MortgageValidationException ex = assertThrows(
                MortgageValidationException.class,
                () -> service.checkMortgage(input)
        );

        assertEquals("business.loan.multiplier", ex.getMessage());
    }

    @Test
    void testLoanExceedsHomeValueRule() {
        MortgageCheckInput input = new MortgageCheckInput(
                new BigDecimal("50000"),   // income → 4× = 200000
                30,
                new BigDecimal("150000"),  // valid for rule 1
                new BigDecimal("100000")   // violates home value rule
        );

        MortgageValidationException ex = assertThrows(
                MortgageValidationException.class,
                () -> service.checkMortgage(input)
        );

        assertEquals("business.loan.homevalue", ex.getMessage());
    }

    @Test
    void testMaturityNotFound() {
        MortgageCheckInput input = new MortgageCheckInput(
                new BigDecimal("50000"),   // valid
                99,                        // invalid maturity
                new BigDecimal("100000"),  // valid
                new BigDecimal("200000")
        );

        MortgageValidationException ex = assertThrows(
                MortgageValidationException.class,
                () -> service.checkMortgage(input)
        );

        assertEquals("business.maturity.notfound", ex.getMessage());
    }

    @Test
    void testEMICalculationAccuracy() {
        MortgageCheckInput input = new MortgageCheckInput(
                new BigDecimal("50000"),   // valid
                20,
                new BigDecimal("100000"),
                new BigDecimal("200000")
        );

        var result = service.checkMortgage(input);

        BigDecimal expected = new BigDecimal("632.65");

        assertEquals(0, result.monthlyCost().compareTo(expected));
    }
}