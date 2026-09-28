package com.gkcontas.patterns.prototype;

import java.util.List;

/**
 * The genuinely shallow copy, isolated so its failure mode can be demonstrated.
 *
 * <p>Java's {@code Object.clone()} behaves exactly like this: it copies each field, and a
 * field holding a reference is copied as a reference. For a class with any mutable member
 * that is almost never what the caller wanted.
 */
public class SharedStateTemplate {

    private final List<String> sections;

    public SharedStateTemplate(List<String> sections) {
        this.sections = sections;
    }

    /** Hands over the same list the original holds. */
    public SharedStateTemplate shallowCopy() {
        return new SharedStateTemplate(this.sections);
    }

    public List<String> sections() {
        return sections;
    }
}
