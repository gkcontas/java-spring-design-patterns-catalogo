package com.gkcontas.patterns.abstractfactory;

import java.util.List;

public class HtmlReportFactory implements ReportComponentFactory {

    @Override
    public ReportHeader createHeader() {
        return title -> "<h1>%s</h1>".formatted(title);
    }

    @Override
    public ReportTable createTable() {
        return rows -> rows.stream()
                .map("<tr><td>%s</td></tr>"::formatted)
                .reduce("<table>", String::concat) + "</table>";
    }

    static List<String> supportedElements() {
        return List.of("h1", "table");
    }
}
