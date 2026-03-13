package com.ing.mortgage.mortgageapp.config;

import com.ing.mortgage.mortgageapp.model.MortgageRate;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Responsible for holding the interest rates for different terms
 */

@ConfigurationProperties(prefix = "mortgage")
public class InterestRateProperties {

    private List<RateConfig> interestRates;

    public List<RateConfig> getInterestRates() {
        return interestRates;
    }

    public void setInterestRates(List<RateConfig> interestRates) {
        this.interestRates = interestRates;
    }

    public static class RateConfig {
        private int maturityPeriod;
        private BigDecimal interestRate;
        private LocalDateTime lastUpdate;

        public int getMaturityPeriod() {
            return maturityPeriod;
        }

        public BigDecimal getInterestRate() {
            return interestRate;
        }

        public LocalDateTime getLastUpdate() {
            return lastUpdate;
        }

        public void setMaturityPeriod(int maturityPeriod) {
            this.maturityPeriod = maturityPeriod;
        }

        public void setInterestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
        }

        public void setLastUpdate(LocalDateTime lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }

    public List<MortgageRate> toDomainRates() {
        return interestRates.stream()
                .map(r -> new MortgageRate(r.getMaturityPeriod(), r.getInterestRate(), r.getLastUpdate()))
                .toList();
    }

}
