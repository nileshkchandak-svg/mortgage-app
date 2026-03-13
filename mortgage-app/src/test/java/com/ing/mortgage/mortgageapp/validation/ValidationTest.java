package com.ing.mortgage.mortgageapp.validation;

import com.ing.mortgage.mortgageapp.dto.MortgageCheckRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testMissingIncome() {
        MortgageCheckRequest req = new MortgageCheckRequest();
        req.setLoanValue(new BigDecimal("100000.0"));
        req.setHomeValue(new BigDecimal("200000.0"));        req.setMaturityPeriod(30);

        Set<ConstraintViolation<MortgageCheckRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testNegativeLoanValue() {
        MortgageCheckRequest req = new MortgageCheckRequest();
        req.setIncome(new BigDecimal("5000.0"));
        req.setLoanValue(BigDecimal.valueOf(-10.0));
        req.setHomeValue(new BigDecimal("200000.0"));
        req.setMaturityPeriod(30);

        Set<ConstraintViolation<MortgageCheckRequest>> violations = validator.validate(req);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("positive")));
    }

    @Test
    void testMultipleValidationErrors() {
        MortgageCheckRequest req = new MortgageCheckRequest();

        Set<ConstraintViolation<MortgageCheckRequest>> violations = validator.validate(req);

        assertTrue(violations.size() >= 3);
    }
}