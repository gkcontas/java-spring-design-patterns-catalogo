package com.gkcontas.patterns.iterator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class PagedIterableTest {

    private final AtomicInteger pagesFetched = new AtomicInteger();

    private final PagedSource<String> source = pageNumber -> {
        pagesFetched.incrementAndGet();
        return switch (pageNumber) {
            case 0 -> new Page<>(List.of("a", "b"), true);
            case 1 -> new Page<>(List.of("c", "d"), true);
            case 2 -> new Page<>(List.of("e"), false);
            default -> new Page<>(List.of(), false);
        };
    };

    @Test
    void shouldWalkEveryPageAsOneSequence() {
        List<String> seen = new ArrayList<>();
        for (String item : new PagedIterable<>(source)) {
            seen.add(item);
        }

        // The caller writes a for-each and never learns that pagination exists.
        assertThat(seen).containsExactly("a", "b", "c", "d", "e");
        assertThat(pagesFetched).hasValue(3);
    }

    @Test
    void shouldFetchLazilySoAnEarlyStopCostsNothing() {
        List<String> seen = new ArrayList<>();
        for (String item : new PagedIterable<>(source)) {
            seen.add(item);
            if (seen.size() == 2) {
                break;
            }
        }

        // Stopping after the first page means the other two were never requested. Over a
        // remote source that is the difference between one call and three.
        assertThat(seen).containsExactly("a", "b");
        assertThat(pagesFetched).hasValue(1);
    }

    @Test
    void shouldHandleAnEmptySource() {
        PagedIterable<String> empty = new PagedIterable<>(pageNumber -> new Page<>(List.of(), false));

        assertThat(empty.iterator().hasNext()).isFalse();
    }
}
