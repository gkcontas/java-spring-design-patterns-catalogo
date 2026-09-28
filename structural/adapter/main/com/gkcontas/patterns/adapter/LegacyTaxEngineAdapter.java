package com.gkcontas.patterns.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * The adapter, and the whole point of it: this is the <em>only</em> class in the
 * application that knows the legacy engine counts in cents and identifies states by
 * number. Without it, that vocabulary spreads to every caller, and replacing the engine
 * later means touching all of them instead of one.
 */
public class LegacyTaxEngineAdapter implements TaxCalculator {

    private static final Map<String, Integer> STATE_CODES = Map.of("SP", 35, "RJ", 33, "MG", 31);

    private final LegacyTaxEngine legacyEngine;

    public LegacyTaxEngineAdapter(LegacyTaxEngine legacyEngine) {
        this.legacyEngine = legacyEngine;
    }

    @Override
    public BigDecimal taxFor(BigDecimal amount, String state) {
        long amountInCents = amount.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
        int stateCode = STATE_CODES.getOrDefault(state, 0);

        long taxInCents = legacyEngine.computeTaxInCents(amountInCents, stateCode);
        return BigDecimal.valueOf(taxInCents).movePointLeft(2).setScale(2, RoundingMode.HALF_UP);
    }
}
