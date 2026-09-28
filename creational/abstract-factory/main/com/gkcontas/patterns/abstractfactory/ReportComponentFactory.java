package com.gkcontas.patterns.abstractfactory;

/**
 * The abstract factory: one interface producing a whole <em>family</em> of related parts.
 *
 * <p>This is the difference from Factory Method, and it is the only one that matters:
 * Factory Method creates one product, Abstract Factory guarantees that several products
 * belong together. Nothing here lets a caller combine an HTML header with a PDF table —
 * and that impossibility is the point, not a side effect.
 */
public interface ReportComponentFactory {

    ReportHeader createHeader();

    ReportTable createTable();
}
