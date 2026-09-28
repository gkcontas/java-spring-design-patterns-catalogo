package com.gkcontas.patterns.flyweight;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class CurrencyTest {

    @Test
    void shouldHandBackTheSameInstanceForTheSameCode() {
        // Same instance, not merely equal. That identity is the entire memory saving.
        assertThat(Currency.of("BRL")).isSameAs(Currency.of("BRL"));
        assertThat(Currency.of("BRL")).isNotSameAs(Currency.of("USD"));
    }

    @Test
    void shouldShareOneFlyweightAcrossManyAmounts() {
        List<MonetaryAmount> amounts = IntStream.range(0, 10_000)
                .mapToObj(index -> new MonetaryAmount(BigDecimal.valueOf(index), Currency.of("BRL")))
                .toList();

        assertThat(amounts).hasSize(10_000);
        // Ten thousand amounts, one currency object between them.
        assertThat(amounts).allSatisfy(amount ->
                assertThat(amount.currency()).isSameAs(amounts.getFirst().currency()));
    }

    @Test
    void shouldKeepIntrinsicStateOutOfTheAmount() {
        MonetaryAmount yen = new MonetaryAmount(new BigDecimal("1500"), Currency.of("JPY"));
        MonetaryAmount real = new MonetaryAmount(new BigDecimal("1500"), Currency.of("BRL"));

        // Decimal places belong to the currency, not to the amount — which is why they can
        // be shared at all.
        assertThat(yen.formatted()).isEqualTo("¥ 1500");
        assertThat(real.formatted()).isEqualTo("R$ 1500.00");
    }

    @Test
    void shouldNotGrowTheCacheOnRepeatedLookups() {
        int before = Currency.cachedInstances();
        IntStream.range(0, 100).forEach(index -> Currency.of("BRL"));

        assertThat(Currency.cachedInstances()).isEqualTo(before);
    }
}
