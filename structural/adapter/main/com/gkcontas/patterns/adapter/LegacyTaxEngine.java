package com.gkcontas.patterns.adapter;

/**
 * The third-party library, exactly as it arrived: cents as {@code long}, state as an int
 * code, percentages as basis points. It cannot be changed, and pretending otherwise is
 * how its vocabulary leaks into every class that touches it.
 */
public class LegacyTaxEngine {

    public long computeTaxInCents(long amountInCents, int stateCode) {
        int basisPoints = switch (stateCode) {
            case 35 -> 1800; // SP
            case 33 -> 2000; // RJ
            default -> 1700;
        };
        return amountInCents * basisPoints / 10_000;
    }
}
