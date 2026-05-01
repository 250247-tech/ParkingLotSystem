package com.parkinglot.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Calculates parking fee based on the per-hour model:
 *   Hour 1      → $4.00
 *   Hours 2–3   → $3.50 each
 *   Hour 4+     → $2.50 each
 */
public class FeeCalculator {

    public static double calculate(LocalDateTime issuedAt, LocalDateTime exitAt) {
        if (issuedAt == null || exitAt == null) return 0.0;

        long minutes = ChronoUnit.MINUTES.between(issuedAt, exitAt);
        if (minutes <= 0) return 0.0;

        // Round up to the nearest hour
        long hours = (long) Math.ceil(minutes / 60.0);

        double total = 0.0;
        for (long h = 1; h <= hours; h++) {
            if (h == 1)          total += 4.00;
            else if (h <= 3)     total += 3.50;
            else                 total += 2.50;
        }
        return total;
    }

    /** Convenience overload: calculates fee up to now */
    public static double calculateUntilNow(LocalDateTime issuedAt) {
        return calculate(issuedAt, LocalDateTime.now());
    }
}
