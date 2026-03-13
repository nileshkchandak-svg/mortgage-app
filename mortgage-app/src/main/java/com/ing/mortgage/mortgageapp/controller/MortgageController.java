package com.ing.mortgage.mortgageapp.controller;

import com.ing.mortgage.mortgageapp.dto.InterestRateResponse;
import com.ing.mortgage.mortgageapp.dto.MortgageCheckRequest;
import com.ing.mortgage.mortgageapp.dto.MortgageCheckResponse;
import com.ing.mortgage.mortgageapp.model.MortgageCheckInput;
import com.ing.mortgage.mortgageapp.model.MortgageCheckResult;
import com.ing.mortgage.mortgageapp.service.MortgageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for Mortgage application, having 2 end points
 * 1. to fetch interest rate
 * 2. To check Mortgage eligibility
 */
@RestController
@RequestMapping("/api")
public class MortgageController {
    private final MortgageService service;

    public MortgageController(MortgageService service) {
        this.service = service;
    }

    /**
     *
     * @return list of interest rate objects
     */
    @GetMapping("/interest-rates")
    public List<InterestRateResponse> getInterestRates() {
        return service.getCurrentRates().stream()
                .map(rate -> new InterestRateResponse(
                        rate.maturityPeriod(),
                        rate.interestRate(),
                        rate.lastUpdate()
                ))
                .toList();
    }

    /**
     *
     * @param request
     * @return result of mortgage eligibility
     */
    @PostMapping("/mortgage-check")
    public MortgageCheckResponse check(@Valid @RequestBody MortgageCheckRequest request) {

        MortgageCheckInput input = new MortgageCheckInput(
                request.getIncome(),
                request.getMaturityPeriod(),
                request.getLoanValue(),
                request.getHomeValue()
        );

        MortgageCheckResult result = service.checkMortgage(input);

        return new MortgageCheckResponse(
                result.feasible(),
                result.monthlyCost()
        );
    }


}
