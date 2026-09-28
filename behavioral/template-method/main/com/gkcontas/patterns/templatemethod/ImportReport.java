package com.gkcontas.patterns.templatemethod;

import java.util.List;

public record ImportReport(int read, int imported, List<String> rejected) {
}
