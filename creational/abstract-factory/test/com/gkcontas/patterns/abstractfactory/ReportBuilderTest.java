package com.gkcontas.patterns.abstractfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ReportBuilderTest {

    private static final List<String> ROWS = List.of("first", "second");

    @Test
    void shouldProduceAConsistentFamilyPerFactory() {
        String html = new ReportBuilder(new HtmlReportFactory()).build("Sales", ROWS);
        String plain = new ReportBuilder(new PlainTextReportFactory()).build("Sales", ROWS);

        // Every part of the HTML report is HTML, and every part of the text report is
        // text. Mixing them is not merely discouraged: the client has no way to express it.
        assertThat(html).contains("<h1>Sales</h1>").contains("<table>").doesNotContain("=====");
        assertThat(plain).contains("Sales").contains("=====").doesNotContain("<");
    }

    @Test
    void shouldLetTheClientStayIgnorantOfConcreteTypes() {
        ReportComponentFactory factory = new PlainTextReportFactory();
        ReportBuilder builder = new ReportBuilder(factory);

        // Swapping the family is one argument; no line inside ReportBuilder changes.
        assertThat(builder.build("Q1", List.of("row"))).contains("  - row");
        assertThat(new ReportBuilder(new HtmlReportFactory()).build("Q1", List.of("row")))
                .contains("<td>row</td>");
    }
}
