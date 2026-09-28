package com.gkcontas.patterns.templatemethod;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class FileImporterTest {

    private final CsvCustomerImporter importer = new CsvCustomerImporter();

    @Test
    void shouldRunTheFixedSequenceWithTheSubclassSteps() {
        ImportReport report = importer.importFrom(List.of(
                "# name,email",
                "Ana, ANA@EXAMPLE.COM",
                "",
                "broken line",
                "Bruno, bruno@example.com"));

        assertThat(report.read()).isEqualTo(5);
        assertThat(report.imported()).isEqualTo(2);
        assertThat(report.rejected()).containsExactly("broken line: expected two comma-separated fields");
        assertThat(importer.stored()).containsExactly("Ana <ana@example.com>", "Bruno <bruno@example.com>");
    }

    @Test
    void shouldLetAHookExtendTheDefaultRatherThanReplaceIt() {
        // The subclass called super.shouldSkip, so blank lines are still skipped along
        // with the comment. A hook that forgets super is where inherited behaviour
        // silently disappears — the fragile base class problem in one line.
        ImportReport report = importer.importFrom(List.of("", "  ", "# header"));

        assertThat(report.imported()).isZero();
        assertThat(report.rejected()).isEmpty();
    }

    @Test
    void shouldKeepTheSequenceIdenticalAcrossSubclasses() {
        FileImporter upperCaseImporter = new FileImporter() {
            private final java.util.List<String> stored = new java.util.ArrayList<>();

            @Override
            protected String validate(String line) {
                return line.isEmpty() ? "empty" : null;
            }

            @Override
            protected String transform(String line) {
                return line.toUpperCase(java.util.Locale.ROOT);
            }

            @Override
            protected void store(String value) {
                stored.add(value);
            }
        };

        // Different steps, same algorithm — and importFrom being final is what guarantees
        // the second half of that sentence.
        assertThat(upperCaseImporter.importFrom(List.of("a", "b")).imported()).isEqualTo(2);
    }
}
