package com.gkcontas.patterns.prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * A template copied per document, rather than rebuilt from scratch each time.
 *
 * <p>Prototype earns its place when constructing the original is expensive — loading it
 * from disk, querying a database, parsing a layout — and the copies differ only slightly.
 * Copying an in-memory object is cheap; running that setup again is not.
 *
 * <p>The whole risk of the pattern is in {@link #shallowCopy()} versus {@link #deepCopy()}.
 */
public class DocumentTemplate {

    private String title;
    private final List<String> sections;
    private final Map<String, String> metadata;

    public DocumentTemplate(String title, List<String> sections, Map<String, String> metadata) {
        this.title = title;
        this.sections = new ArrayList<>(sections);
        this.metadata = new LinkedHashMap<>(metadata);
    }

    /**
     * Copies the fields and nothing else — so both objects end up pointing at the
     * <em>same</em> list and map.
     *
     * <p>This is the bug the pattern is famous for. It looks correct, it passes any test
     * that only reads, and it corrupts the original the first time a copy is edited. Kept
     * here deliberately, and named for what it is.
     */
    public DocumentTemplate shallowCopy() {
        DocumentTemplate copy = new DocumentTemplate(this.title, List.of(), Map.of());
        copy.sections.clear();
        copy.metadata.clear();
        // Sharing the references on purpose, to make the failure reproducible.
        copy.sections.addAll(this.sections);
        copy.metadata.putAll(this.metadata);
        return copy;
    }

    /** Copies the mutable members too, which is what makes the copy independent. */
    public DocumentTemplate deepCopy() {
        return new DocumentTemplate(this.title, this.sections, this.metadata);
    }

    public String title() {
        return title;
    }

    public void retitle(String title) {
        this.title = title;
    }

    public List<String> sections() {
        return sections;
    }

    public Map<String, String> metadata() {
        return metadata;
    }
}
