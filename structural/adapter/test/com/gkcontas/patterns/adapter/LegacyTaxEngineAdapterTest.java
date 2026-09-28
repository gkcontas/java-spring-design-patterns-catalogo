package com.gkcontas.patterns.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class LegacyTaxEngineAdapterTest {

    private final TaxCalculator calculator = new LegacyTaxEngineAdapter(new LegacyTaxEngine());

    @Test
    void shouldSpeakTheApplicationsVocabularyOnBothSides() {
        // BigDecimal in, BigDecimal out, state as a string. Nothing about cents or numeric
        // state codes escapes the adapter.
        assertThat(calculator.taxFor(new BigDecimal("100.00"), "SP"))
                .isEqualByComparingTo("18.00");
        assertThat(calculator.taxFor(new BigDecimal("100.00"), "RJ"))
                .isEqualByComparingTo("20.00");
    }

    @Test
    void shouldFallBackToTheDefaultRateForAnUnknownState() {
        assertThat(calculator.taxFor(new BigDecimal("100.00"), "ZZ"))
                .isEqualByComparingTo("17.00");
    }

    @Test
    void shouldLetTheEngineBeReplacedWithoutTouchingCallers() {
        // The client depends on TaxCalculator, so a different implementation is a
        // one-line change. That is what the indirection bought.
        TaxCalculator flatRate = (amount, state) -> amount.multiply(new BigDecimal("0.10"));

        assertThat(flatRate.taxFor(new BigDecimal("100.00"), "SP"))
                .isEqualByComparingTo("10.00");
    }
}
