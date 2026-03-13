package com.ing.mortgage.mortgageapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.mortgage.mortgageapp.dto.MortgageCheckRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testGetInterestRates() throws Exception {
        mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].maturityPeriod").exists());
    }

    @Test
    void testMortgageCheckHappyPath() throws Exception {
        MortgageCheckRequest req = new MortgageCheckRequest();
        req.setIncome(new BigDecimal("50000.0"));
        req.setLoanValue(new BigDecimal("150000.0"));
        req.setHomeValue(new BigDecimal("300000.0"));
        req.setMaturityPeriod(30);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(true))
                .andExpect(jsonPath("$.monthlyCost").exists());
    }

    @Test
    void testMortgageCheckBusinessRuleViolation() throws Exception {
        MortgageCheckRequest req = new MortgageCheckRequest();
        req.setIncome(new BigDecimal("3000.0"));
        req.setLoanValue(new BigDecimal("200000.0")); // violates rule 1
        req.setHomeValue(new BigDecimal("250000.0"));
        req.setMaturityPeriod(30);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BUSINESS_RULE_VIOLATION"));
    }
}
