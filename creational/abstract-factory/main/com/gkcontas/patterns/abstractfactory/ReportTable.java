package com.gkcontas.patterns.abstractfactory;

import java.util.List;

public interface ReportTable {

    String render(List<String> rows);
}
