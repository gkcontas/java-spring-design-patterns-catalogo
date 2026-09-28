package com.gkcontas.patterns.templatemethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvCustomerImporter extends FileImporter {

    private final List<String> stored = new ArrayList<>();

    @Override
    protected boolean shouldSkip(String line) {
        // Overrides the hook to also skip the header, and keeps the inherited blank check.
        return super.shouldSkip(line) || line.startsWith("#");
    }

    @Override
    protected String validate(String line) {
        return line.contains(",") ? null : "expected two comma-separated fields";
    }

    @Override
    protected String transform(String line) {
        String[] parts = line.split(",", 2);
        return "%s <%s>".formatted(parts[0].trim(), parts[1].trim().toLowerCase(Locale.ROOT));
    }

    @Override
    protected void store(String value) {
        stored.add(value);
    }

    public List<String> stored() {
        return List.copyOf(stored);
    }
}
