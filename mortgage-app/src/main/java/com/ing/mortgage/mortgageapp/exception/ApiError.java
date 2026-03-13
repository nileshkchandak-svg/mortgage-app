package com.ing.mortgage.mortgageapp.exception;

import java.time.Instant;
import java.util.List;

public record ApiError(
        List<String> message,
        String code,
        Instant timestamp
) {
}

