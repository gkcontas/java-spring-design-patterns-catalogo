package com.gkcontas.patterns.abstractfactory;

import java.util.List;

/** The client. It never names a concrete component, only the factory it was handed. */
public class ReportBuilder {

    private final ReportComponentFactory factory;

    public ReportBuilder(ReportComponentFactory factory) {
        this.factory = factory;
    }

    public String build(String title, List<String> rows) {
        return factory.createHeader().render(title)
                + System.lineSeparator()
                + factory.createTable().render(rows);
    }
}
