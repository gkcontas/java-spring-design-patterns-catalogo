package com.gkcontas.patterns.templatemethod;

import java.util.ArrayList;
import java.util.List;

/**
 * The skeleton of the algorithm, fixed here; the steps that differ are left abstract.
 *
 * <p>{@code importFrom} is {@code final} on purpose. A template method that a subclass can
 * override is not a template: the guarantee being sold is that the sequence — read,
 * validate, transform, collect — happens in that order every time, and an override takes
 * it away.
 */
public abstract class FileImporter {

    /** The template method. */
    public final ImportReport importFrom(List<String> lines) {
        List<String> rejected = new ArrayList<>();
        int imported = 0;

        for (String line : lines) {
            if (shouldSkip(line)) {
                continue;
            }
            String problem = validate(line);
            if (problem != null) {
                rejected.add("%s: %s".formatted(line, problem));
                continue;
            }
            store(transform(line));
            imported++;
        }
        return new ImportReport(lines.size(), imported, List.copyOf(rejected));
    }

    /** A hook, not an abstract step: subclasses override it only if they care. */
    protected boolean shouldSkip(String line) {
        return line.isBlank();
    }

    protected abstract String validate(String line);

    protected abstract String transform(String line);

    protected abstract void store(String value);
}
