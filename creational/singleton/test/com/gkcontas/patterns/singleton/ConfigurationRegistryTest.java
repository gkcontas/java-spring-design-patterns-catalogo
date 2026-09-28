package com.gkcontas.patterns.singleton;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationRegistryTest {

    @BeforeEach
    void reset() {
        // Shared mutable state that survives the test — the cost of a singleton, visible
        // in the very first thing its test has to do.
        ConfigurationRegistry.INSTANCE.clear();
    }

    @Test
    void shouldAlwaysHandBackTheSameInstance() {
        ConfigurationRegistry.INSTANCE.set("timeout", "30s");

        assertThat(ConfigurationRegistry.INSTANCE.get("timeout", "none")).isEqualTo("30s");
        assertThat(ConfigurationRegistry.valueOf("INSTANCE"))
                .isSameAs(ConfigurationRegistry.INSTANCE);
    }

    @Test
    void shouldSurviveConcurrentFirstAccess() throws InterruptedException {
        int writers = 50;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(writers);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, writers).forEach(index -> executor.submit(() -> {
                try {
                    start.await();
                    ConfigurationRegistry.INSTANCE.set("key-" + index, String.valueOf(index));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            }));
            start.countDown();
            done.await();
        }

        // The classic lazy-init singleton needs explicit synchronisation to guarantee
        // this. The enum needs none: class initialisation is already thread-safe.
        assertThat(ConfigurationRegistry.INSTANCE.size()).isEqualTo(writers);
    }
}
