package com.gkcontas.patterns.abstractfactory;

public class PlainTextReportFactory implements ReportComponentFactory {

    @Override
    public ReportHeader createHeader() {
        return title -> "%s%n%s".formatted(title, "=".repeat(title.length()));
    }

    @Override
    public ReportTable createTable() {
        return rows -> String.join(System.lineSeparator(),
                rows.stream().map("  - %s"::formatted).toList());
    }
}
