package com.gkcontas.patterns.prototype;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DocumentTemplateTest {

    @Test
    void deepCopyShouldLeaveTheOriginalAlone() {
        DocumentTemplate original = new DocumentTemplate(
                "Contract", List.of("intro", "terms"), Map.of("version", "1"));

        DocumentTemplate copy = original.deepCopy();
        copy.retitle("Contract — client A");
        copy.sections().add("annex");
        copy.metadata().put("client", "A");

        assertThat(original.title()).isEqualTo("Contract");
        assertThat(original.sections()).containsExactly("intro", "terms");
        assertThat(original.metadata()).containsOnlyKeys("version");
        assertThat(copy.sections()).containsExactly("intro", "terms", "annex");
    }

    @Test
    void shallowCopyShouldCorruptTheOriginalThroughTheSharedList() {
        List<String> sections = new ArrayList<>(List.of("intro"));
        SharedStateTemplate original = new SharedStateTemplate(sections);

        SharedStateTemplate copy = original.shallowCopy();
        copy.sections().add("edited in the copy");

        // The assertion that makes the trap concrete: editing the copy changed the
        // original, because both hold the same list. Object.clone() does exactly this,
        // which is why a class with any mutable member almost never wants it.
        assertThat(original.sections())
                .as("a shallow copy shares its mutable members with the original")
                .containsExactly("intro", "edited in the copy");
        assertThat(original.sections()).isSameAs(copy.sections());
    }

    @Test
    void copyingShouldBeCheaperThanRebuilding() {
        DocumentTemplate expensiveOriginal = new DocumentTemplate(
                "Report", List.of("a", "b", "c"), Map.of("source", "warehouse"));

        DocumentTemplate first = expensiveOriginal.deepCopy();
        DocumentTemplate second = expensiveOriginal.deepCopy();

        // Independent objects, same starting content: the point of keeping a prototype
        // around rather than running the expensive setup once per document.
        assertThat(first.sections()).isEqualTo(second.sections()).isNotSameAs(second.sections());
    }
}
