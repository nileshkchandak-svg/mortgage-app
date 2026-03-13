package com.ing.mortgage.mortgageapp;

import com.ing.mortgage.mortgageapp.config.InterestRateProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(InterestRateProperties.class)
public class MortgageAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MortgageAppApplication.class, args);
    }

}
